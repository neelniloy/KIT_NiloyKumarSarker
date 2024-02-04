package com.niloythings.kittask

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.niloythings.kittask.databinding.FragmentRegisterUserBinding
import com.niloythings.kittask.entities.User
import com.niloythings.kittask.repository.UserRepository
import com.niloythings.kittask.view_model.UserViewModel
import com.niloythings.kittask.view_model.UserViewModelFactory
import java.io.ByteArrayOutputStream
import java.io.IOException

class RegisterUserFragment : Fragment() {

    private lateinit var binding: FragmentRegisterUserBinding
    private lateinit var viewModel: UserViewModel
    private val PICK_IMAGE_REQUEST = 1
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2
    private lateinit var _imageUrl : ByteArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentRegisterUserBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = MyApp.database.userDao()
        val userRepository = UserRepository(userDao)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]


        binding.cardChooseImage.setOnClickListener {
            if (checkReadExternalStoragePermission()) {
                chooseImage()
            } else {
                requestReadExternalStoragePermission()
            }
        }

        binding.btnRegister.setOnClickListener {
            onRegisterPerform()
        }


    }

    private fun onRegisterPerform() {
        val firstName = binding.firstNameEditText.text.toString().trim()
        val lastName = binding.lastNameEditText.text.toString().trim()
        val ageText = binding.ageEditText.text.toString().trim()
        val gender = getSelectedGender()
        val address = binding.addressEditText.text.toString().trim()
        val occupation = binding.occupationEditText.text.toString().trim()
        val imageUrl = _imageUrl

        if (imageUrl.isEmpty()){
            Toast.makeText(requireContext(), "Choose a image first", Toast.LENGTH_SHORT).show()
            return

        }else if (firstName.isEmpty() || lastName.isEmpty() || ageText.isEmpty() || gender.isEmpty() || address.isEmpty() || occupation.isEmpty()) {
                Toast.makeText(requireContext(), "Field can not be empty", Toast.LENGTH_SHORT).show()
                return
        }

        val age = try {
            ageText.toInt()
        } catch (e: NumberFormatException) {

            return
        }

        val user = User(

            firstName = firstName,
            lastName = lastName,
            age = age,
            gender = gender,
            address = address,
            occupation = occupation,
            imageUrl = imageUrl,

        )
        viewModel.registerUser(user)

        Toast.makeText(requireContext(), "User has been registered successfully", Toast.LENGTH_SHORT).show()

        findNavController().popBackStack()
    }

    private fun getSelectedGender(): String {
        return when (binding.genderRadioGroup.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri = data.data!!

            binding.userImage.setImageURI(selectedImageUri)

            try {
                val inputStream = requireContext().contentResolver?.openInputStream(selectedImageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                _imageUrl =  byteArrayOutputStream.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    private fun checkReadExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        }else
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Read external storage permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}
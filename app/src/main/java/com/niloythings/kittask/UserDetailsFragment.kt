package com.niloythings.kittask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.niloythings.kittask.databinding.FragmentUserDetailsBinding
import com.niloythings.kittask.repository.UserRepository
import com.niloythings.kittask.view_model.UserViewModel
import com.niloythings.kittask.view_model.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserDetailsFragment : Fragment() {

    private lateinit var binding:FragmentUserDetailsBinding
    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return FragmentUserDetailsBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getInt("userId")

        val userDao = MyApp.database.userDao()
        val userRepository = UserRepository(userDao)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]

        CoroutineScope(Dispatchers.IO).launch {
            val user = userId?.let { viewModel.getUserById(it) }
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "$user", Toast.LENGTH_SHORT).show()
            }

        }

    }

}
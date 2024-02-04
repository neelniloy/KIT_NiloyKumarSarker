package com.niloythings.kittask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.niloythings.kittask.databinding.FragmentUserListBinding
import com.niloythings.kittask.entities.User
import com.niloythings.kittask.repository.UserRepository
import com.niloythings.kittask.view_model.UserViewModel
import com.niloythings.kittask.view_model.UserViewModelFactory

class UserListFragment : Fragment() {

    private lateinit var binding:FragmentUserListBinding
    private lateinit var viewModel: UserViewModel
    private val userAdapter = UserAdapter { user -> onUserItemClick(user) }

    private fun onUserItemClick(user: User) {
        findNavController().navigate(R.id.action_userListFragment_to_userDetailsFragment,
            bundleOf("userId" to user.id)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return FragmentUserListBinding.inflate(inflater, container, false).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userDao = MyApp.database.userDao()
        val userRepository = UserRepository(userDao)
        viewModel = ViewModelProvider(
            this,
            UserViewModelFactory(userRepository)
        )[UserViewModel::class.java]

        viewModel.userList.observe(viewLifecycleOwner) { userList ->
            userAdapter.userList = userList
        }

        // Set up RecyclerView
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }

        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_userListFragment_to_registerUserFragment)
        }
    }

}
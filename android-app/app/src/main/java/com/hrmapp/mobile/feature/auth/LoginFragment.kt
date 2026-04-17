package com.hrmapp.mobile.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                username = binding.etUsername.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

        observeUi()
    }

    private fun observeUi() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressLogin.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            binding.btnLogin.isEnabled = !state.isLoading
            binding.etUsername.isEnabled = !state.isLoading
            binding.etPassword.isEnabled = !state.isLoading

            binding.tvLoginMessage.text = state.message

            if (state.isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Xin chào ${state.username} (${state.roleCode})",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
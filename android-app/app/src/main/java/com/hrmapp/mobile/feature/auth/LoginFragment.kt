package com.hrmapp.mobile.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private val splashGateViewModel: SplashGateViewModel by viewModels()

    private var didAutoNavigate = false
    private lateinit var savedAccountAdapter: SavedAccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        savedAccountAdapter = SavedAccountAdapter(
            onUseClick = { account ->
                binding.etUsername.setText(account.username)
                binding.etPassword.requestFocus()
                Snackbar.make(binding.root, "Đã chọn tài khoản ${account.username}", Snackbar.LENGTH_SHORT).show()
            },
            onRemoveClick = { account ->
                loginViewModel.removeSavedAccount(account.username)
                Snackbar.make(binding.root, "Đã xóa ${account.username}", Snackbar.LENGTH_SHORT).show()
            }
        )

        binding.rvSavedAccounts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSavedAccounts.adapter = savedAccountAdapter

        observeGate()
        observeLogin()

        val logoutMessage = arguments?.getString("logout_message")
        if (!logoutMessage.isNullOrBlank()) {
            binding.tvLoginMessage.text = logoutMessage
        }

        splashGateViewModel.checkSession()

        binding.btnLogin.setOnClickListener {
            loginViewModel.login(
                username = binding.etUsername.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun observeGate() {
        splashGateViewModel.gateState.observe(viewLifecycleOwner) { state ->
            val navController = findNavController()
            val currentId = navController.currentDestination?.id

            if (
                state.checked &&
                state.loggedIn &&
                !didAutoNavigate &&
                currentId == R.id.loginFragment
            ) {
                didAutoNavigate = true
                navController.navigate(R.id.homeFragment)
            }
        }
    }

    private fun observeLogin() {
        loginViewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressLogin.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            binding.btnLogin.isEnabled = !state.isLoading
            binding.etUsername.isEnabled = !state.isLoading
            binding.etPassword.isEnabled = !state.isLoading

            binding.tvLoginMessage.text = state.message

            val hasSavedAccounts = state.savedAccounts.isNotEmpty()
            binding.tvSavedAccountTitle.visibility = if (hasSavedAccounts) View.VISIBLE else View.GONE
            binding.rvSavedAccounts.visibility = if (hasSavedAccounts) View.VISIBLE else View.GONE
            savedAccountAdapter.submitList(state.savedAccounts)

            if (state.isSuccess) {
                Toast.makeText(
                    requireContext(),
                    "Xin chào ${state.username} (${state.roleCode})",
                    Toast.LENGTH_SHORT
                ).show()

                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.loginFragment) {
                    navController.navigate(R.id.homeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
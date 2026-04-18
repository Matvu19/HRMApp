package com.hrmapp.mobile.feature.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.hrmapp.mobile.R
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileBinding.bind(view)

        binding.btnBackProfile.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEditProfile.setSafeClickListener {
            viewModel.setEditing(true)
        }

        binding.btnSaveProfile.setSafeClickListener {
            viewModel.updateProfile(
                fullName = binding.etProfileName.text.toString(),
                personalPhone = binding.etProfilePhone.text.toString(),
                gender = binding.etProfileGender.text.toString()
            )
        }

        binding.btnChangePassword.setSafeClickListener {
            ChangePasswordDialogFragment().show(childFragmentManager, "change_password")
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val profile = state.profile

            if (profile != null) {
                binding.etProfileName.setText(profile.fullName)
                binding.tvProfileCode.text = "Mã nhân viên: ${profile.employeeCode}"
                binding.tvProfileEmail.text = "Email công việc: ${profile.workEmail ?: "-"}"
                binding.etProfilePhone.setText(profile.personalPhone ?: "")
                binding.etProfileGender.setText(profile.gender ?: "")
                binding.tvProfileJoinDate.text = "Ngày vào làm: ${profile.joinDate ?: "-"}"
                binding.tvProfileStatus.text = "Trạng thái: ${profile.status ?: "-"}"
                binding.tvProfileRole.text = "Vai trò hệ thống: ${state.roleCode}"
                binding.tvProfileUsername.text = "Tài khoản: ${state.username}"
            }

            binding.etProfileName.isEnabled = state.isEditing
            binding.etProfilePhone.isEnabled = state.isEditing
            binding.etProfileGender.isEnabled = state.isEditing
            binding.btnSaveProfile.visibility = if (state.isEditing) View.VISIBLE else View.GONE
            binding.tvProfileMessage.text = state.message

            if (state.message == "Đã cập nhật thông tin cá nhân") {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
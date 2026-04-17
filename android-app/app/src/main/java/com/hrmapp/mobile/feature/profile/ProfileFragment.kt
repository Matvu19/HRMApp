package com.hrmapp.mobile.feature.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileBinding.bind(view)

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val profile = state.profile

            if (profile != null) {
                binding.tvProfileName.text = profile.fullName
                binding.tvProfileCode.text = "Mã nhân viên: ${profile.employeeCode}"
                binding.tvProfileEmail.text = "Email công việc: ${profile.workEmail ?: "-"}"
                binding.tvProfilePhone.text = "Số điện thoại: ${profile.personalPhone ?: "-"}"
                binding.tvProfileGender.text = "Giới tính: ${profile.gender ?: "-"}"
                binding.tvProfileJoinDate.text = "Ngày vào làm: ${profile.joinDate ?: "-"}"
                binding.tvProfileStatus.text = "Trạng thái: ${profile.status ?: "-"}"
                binding.tvProfileRole.text = "Vai trò hệ thống: ${state.roleCode}"
                binding.tvProfileUsername.text = "Tài khoản: ${state.username}"
                binding.tvProfileMessage.text = ""
            } else {
                binding.tvProfileMessage.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
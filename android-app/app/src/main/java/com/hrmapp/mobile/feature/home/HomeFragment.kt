package com.hrmapp.mobile.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.loadSession()

        binding.btnOpenApproval.setOnClickListener {
            findNavController().navigate(R.id.approvalFragment)
        }

        binding.btnOpenDashboard.setOnClickListener {
            findNavController().navigate(R.id.dashboardFragment)
        }

        binding.btnOpenAttendance.setOnClickListener {
            findNavController().navigate(R.id.attendanceFragment)
        }

        binding.btnOpenLeave.setOnClickListener {
            findNavController().navigate(R.id.leaveFragment)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.tvWelcome.text = "Xin chào, ${state.fullName.ifBlank { "người dùng" }}"
            binding.tvRole.text = "Vai trò: ${state.roleCode.ifBlank { "Chưa xác định" }}"
            binding.tvEmployeeCode.text =
                if (state.employeeCode.isNotBlank()) "Mã nhân viên: ${state.employeeCode}" else ""
            binding.tvHomeMessage.text = state.message

            binding.cardManagerActions.visibility =
                if (state.isManager) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
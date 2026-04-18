package com.hrmapp.mobile.feature.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.hrmapp.mobile.R
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentManagerDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerDashboardFragment : Fragment(R.layout.fragment_manager_dashboard) {

    private var _binding: FragmentManagerDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManagerDashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentManagerDashboardBinding.bind(view)

        binding.btnBack.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnRefreshDashboard.setSafeClickListener {
            viewModel.load()
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressDashboard.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            binding.tvDashboardMessage.text = state.message

            if (state.data != null) {
                binding.tvTeamSize.text = state.data.teamSize.toString()
                binding.tvPendingApprovals.text = state.data.pendingApprovalCount.toString()
                binding.tvTodayCheckins.text = state.data.todayCheckinCount.toString()
                binding.tvUnreadAlerts.text = state.data.unreadNotificationCount.toString()

                binding.tvRealtimeHighlight.text = state.quickHighlight
                binding.tvAttendanceRate.text = state.attendanceRateText
                binding.tvAttendanceRateNote.text = state.attendanceRateNote
                binding.tvTeamHealth.text = state.teamHealth
            } else {
                binding.tvTeamSize.text = "-"
                binding.tvPendingApprovals.text = "-"
                binding.tvTodayCheckins.text = "-"
                binding.tvUnreadAlerts.text = "-"
                binding.tvRealtimeHighlight.text = state.message
                binding.tvAttendanceRate.text = "-"
                binding.tvAttendanceRateNote.text = ""
                binding.tvTeamHealth.text = ""
            }

            if (!state.isLoading && state.message.contains("không có quyền", ignoreCase = true)) {
                Snackbar.make(binding.root, state.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
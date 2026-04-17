package com.hrmapp.mobile.feature.dashboard

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.R
import com.hrmapp.mobile.databinding.FragmentManagerDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerDashboardFragment : Fragment(R.layout.fragment_manager_dashboard) {

    private var _binding: FragmentManagerDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManagerDashboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentManagerDashboardBinding.bind(view)

        view.findViewById<Button>(R.id.btnBack).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                binding.tvPendingApprovals.text = state.data.pendingApprovalCount.toString()
                binding.tvTeamSize.text = state.data.teamSize.toString()
                binding.tvUnreadAlerts.text = state.data.unreadNotificationCount.toString()
                binding.tvRealtimeHighlight.text =
                    "Hôm nay có ${state.data.pendingApprovalCount} yêu cầu chờ duyệt."
            } else if (state.message.isNotBlank()) {
                binding.tvRealtimeHighlight.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
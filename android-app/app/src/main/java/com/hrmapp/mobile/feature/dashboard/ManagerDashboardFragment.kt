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

        viewModel.load(2L)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val data = state.data ?: return@observe
            binding.tvPendingApprovals.text = data.pendingApprovalCount.toString()
            binding.tvTeamSize.text = data.teamSize.toString()
            binding.tvUnreadAlerts.text = data.unreadNotificationCount.toString()
            binding.tvRealtimeHighlight.text =
                "Hôm nay có ${data.pendingApprovalCount} yêu cầu chờ duyệt."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
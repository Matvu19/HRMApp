package com.hrmapp.mobile.feature.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.R
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentManagerDashboardBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerDashboardFragment : Fragment(R.layout.fragment_manager_dashboard) {

    private var _binding: FragmentManagerDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ManagerDashboardViewModel by viewModels()
    private lateinit var adapter: DashboardApprovalPreviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentManagerDashboardBinding.bind(view)

        binding.btnBack.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        adapter = DashboardApprovalPreviewAdapter()
        binding.rvDashboardApprovals.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDashboardApprovals.adapter = adapter

        binding.btnRefreshDashboard.setSafeClickListener {
            viewModel.load()
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressDashboard.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE

            if (state.data != null) {
                binding.tvPendingApprovals.text = state.data.pendingApprovalCount.toString()
                binding.tvTeamSize.text = state.data.teamSize.toString()
                binding.tvUnreadAlerts.text = state.data.unreadNotificationCount.toString()

                binding.tvRealtimeHighlight.text =
                    if (state.pendingApprovals.isNotEmpty()) {
                        "Hiện có ${state.pendingApprovals.size} yêu cầu đang chờ xử lý."
                    } else {
                        "Hiện chưa có yêu cầu chờ duyệt."
                    }
            } else {
                binding.tvPendingApprovals.text = "-"
                binding.tvTeamSize.text = "-"
                binding.tvUnreadAlerts.text = "-"
                binding.tvRealtimeHighlight.text = state.message
            }

            binding.tvDashboardMessage.text = state.message
            adapter.submitList(state.pendingApprovals)

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
package com.hrmapp.mobile.feature.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = NotificationAdapter { item ->
            viewModel.markRead(item.notificationEventId)
        }

        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = adapter

        binding.btnRefreshNotifications.setOnClickListener {
            viewModel.load()
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.items)
            binding.tvNotificationMessage.text = state.message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
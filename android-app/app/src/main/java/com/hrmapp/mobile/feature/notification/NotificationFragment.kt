package com.hrmapp.mobile.feature.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.load(2L)

        binding.btnMarkRead.setOnClickListener {
            val item = viewModel.uiState.value?.item ?: return@setOnClickListener
            viewModel.markRead(item.notificationEventId)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val item = state.item
            if (item != null) {
                binding.tvNotificationTitle.text = item.title
                binding.tvNotificationBody.text = item.messageText
                binding.tvNotificationDeeplink.text = item.deeplinkUrl
                binding.tvNotificationStatus.text =
                    if (item.readAt == null) "Chưa đọc" else "Đã đọc"
            }

            if (state.message.isNotBlank()) {
                binding.tvNotificationStatus.text = state.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
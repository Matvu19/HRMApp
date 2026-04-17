package com.hrmapp.mobile.feature.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.databinding.FragmentAttendanceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCheckIn.setOnClickListener {
            viewModel.checkIn()
        }

        binding.btnCheckOut.setOnClickListener {
            viewModel.checkOut()
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.message.isNotBlank()) {
                binding.tvLastAction.text = "Lần thao tác gần nhất: ${state.message}"
            }
            binding.tvQueueCount.text = "Số thao tác đang chờ: ${state.queueCount}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
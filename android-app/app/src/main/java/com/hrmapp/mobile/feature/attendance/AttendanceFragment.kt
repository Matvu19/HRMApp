package com.hrmapp.mobile.feature.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.databinding.FragmentAttendanceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendanceFragment : Fragment() {

    private var _binding: FragmentAttendanceBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AttendanceViewModel by viewModels()
    private lateinit var adapter: AttendanceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AttendanceAdapter()
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAttendance.adapter = adapter

        binding.btnCheckIn.setOnClickListener {
            viewModel.checkIn()
        }

        binding.btnCheckOut.setOnClickListener {
            viewModel.checkOut()
        }

        binding.btnRefreshAttendance.setOnClickListener {
            viewModel.loadHistory()
        }

        viewModel.loadHistory()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            adapter.submitList(state.items)
            binding.tvAttendanceMessage.text = state.message
            binding.tvLastAction.text =
                if (state.message.isNotBlank()) "Lần thao tác gần nhất: ${state.message}"
                else "Lần thao tác gần nhất: chưa có"
            binding.tvQueueCount.text = "Số thao tác đang chờ: ${state.queueCount}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.hrmapp.mobile.feature.leave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hrmapp.mobile.databinding.FragmentLeaveBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaveFragment : Fragment() {

    private var _binding: FragmentLeaveBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LeaveViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLeaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSubmitLeave.setOnClickListener {
            val dateFrom = binding.etDateFrom.text.toString().ifBlank { "2026-04-20" }
            val dateTo = binding.etDateTo.text.toString().ifBlank { "2026-04-21" }
            val reason = binding.etReason.text.toString().ifBlank { "Việc gia đình" }

            viewModel.submit(
                dateFrom = dateFrom,
                dateTo = dateTo,
                reason = reason
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.tvLeaveResult.text = state.message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
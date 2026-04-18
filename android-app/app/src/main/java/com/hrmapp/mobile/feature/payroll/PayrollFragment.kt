package com.hrmapp.mobile.feature.payroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrmapp.mobile.core.ui.setSafeClickListener
import com.hrmapp.mobile.databinding.FragmentPayrollBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PayrollFragment : Fragment() {

    private var _binding: FragmentPayrollBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PayrollViewModel by viewModels()
    private lateinit var adapter: PayrollAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPayrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PayrollAdapter()
        binding.rvPayroll.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPayroll.adapter = adapter

        binding.btnBackPayroll.setSafeClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnRefreshPayroll.setSafeClickListener {
            viewModel.load()
        }

        viewModel.load()

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.progressPayroll.visibility =
                if (state.isLoading) View.VISIBLE else View.GONE
            binding.tvPayrollMessage.text = state.message
            adapter.submitList(state.items)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
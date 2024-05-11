package com.cs4080.kotlinteam.expensetracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs4080.kotlinteam.expensetracker.databinding.FragmentWelcomeScreenBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WelcomeFragment: Fragment() {

    private var _binding: FragmentWelcomeScreenBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var budget = ""
        val today = Calendar.getInstance().time
        binding.apply {
            budgetAmount.setText(sharedPreferences.getString("Budget", "0.00").toString())
            budget = sharedPreferences.getString("Budget", "0.00").toString()
            saveBudgetButton.setOnClickListener {
                budget = String.format("%.2f", budgetAmount.text.toString().toFloat())
                editor.putString("Budget", budget)
                editor.apply()
            }

            expenseListButton.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.showExpenseList())
            }

            spendingAnalysisButton.setOnClickListener {
                findNavController().navigate(
                    WelcomeFragmentDirections.showSpendingAnalysis(
                        String.format(
                            "%.2f",
                            budget.toFloat()
                        ).toFloat()
                    )
                )
            }
            chatButton.setOnClickListener {
                findNavController().navigate(WelcomeFragmentDirections.showChat())
            }
            welcomeDateDisplay.text = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
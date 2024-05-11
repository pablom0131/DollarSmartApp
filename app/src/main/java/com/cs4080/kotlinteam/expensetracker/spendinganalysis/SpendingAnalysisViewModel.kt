package com.cs4080.kotlinteam.expensetracker.spendinganalysis
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs4080.kotlinteam.expensetracker.expenselist.Expense
import com.cs4080.kotlinteam.expensetracker.expenselist.ExpenseRepository

class SpendingAnalysisViewModel : ViewModel() {
    val sharedData = MutableLiveData<String>()
    private val expenseRepository = ExpenseRepository.get()
    private val _expenses: List<Expense> = expenseRepository.getSimpleExpenses()

    fun getExpenses(): List<Expense> {
        return _expenses
    }
}

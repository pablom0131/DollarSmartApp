package com.cs4080.kotlinteam.expensetracker.expenselist

import android.app.Application

class ExpenseTrackerApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ExpenseRepository.initialize(this)
    }
}
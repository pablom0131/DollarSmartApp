package com.cs4080.kotlinteam.expensetracker.expenselist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

// schema
@Entity
data class Expense (
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val amount: Double,
    val category: String,
    val photoFileName: String? = null
)
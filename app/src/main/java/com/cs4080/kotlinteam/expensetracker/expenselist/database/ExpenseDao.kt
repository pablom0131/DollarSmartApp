package com.cs4080.kotlinteam.expensetracker.expenselist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.cs4080.kotlinteam.expensetracker.expenselist.Expense
import java.util.UUID
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense")
    fun getExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expense")
    fun getSimpleExpenses(): List<Expense>

    @Query("SELECT * FROM expense WHERE id=(:id)")
    suspend fun getExpense(id: UUID): Expense

    @Update
    suspend fun updateExpense(expense: Expense)

    @Insert
    suspend fun addExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)
}
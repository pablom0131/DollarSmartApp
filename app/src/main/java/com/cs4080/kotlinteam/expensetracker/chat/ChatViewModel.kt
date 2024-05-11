package com.cs4080.kotlinteam.expensetracker.chat

import androidx.lifecycle.ViewModel

class ChatViewModel : ViewModel() {
    //private val expenseRepository = ExpenseRepository.get()

    val messages = mutableListOf<ChatMessage>()

    //private var _messages: List<ChatMessage> = emptyList()
//    val messages: List<ChatMessage>
//        get() = _messages

//    init {
//        for (i in 0 until 10) {
//            val message = ChatMessage("hi","hi")
//            _messages += message
//        }
//    }

    fun addMessage(message: ChatMessage){
        messages += message
    }
}
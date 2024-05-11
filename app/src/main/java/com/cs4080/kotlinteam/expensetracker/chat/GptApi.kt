package com.cs4080.kotlinteam.expensetracker.chat

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GptApi {
    @POST("v1/chat/completions")
    suspend fun getChatResponse(
        @Header("Authorization") apiKey: String,
        @Body request: ChatRequest
    ): ChatCompletionResponse
}

data class GptRequest(
    val prompt: String,
    val max_tokens: Int = 150,
    val temperature: Float = 0.7f,
    val top_p: Float = 1.0f,
    val frequency_penalty: Float = 0.0f,
    val presence_penalty: Float = 0.0f,
    val stop: List<String>? = null
)

data class ChatCompletionResponse(
    val choices: List<ChatChoice>,
    val created: Long,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

data class ChatChoice(
    val finish_reason: String,
    val index: Int,
    val message: ChatMessageResponse,
    val logprobs: Any? // Since logprobs can be null
)

data class ChatMessageResponse(
    val content: String,
    val role: String
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)

data class ChatRequest(
    val model: String,
    val messages: List<ChatMessage>
)
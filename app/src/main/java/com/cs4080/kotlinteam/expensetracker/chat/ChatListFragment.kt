package com.cs4080.kotlinteam.expensetracker.chat

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs4080.kotlinteam.expensetracker.databinding.FragmentChatBinding
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ChatFragment: Fragment() {

    private class AuthInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            return chain.proceed(request)
        }
    }
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(""))
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
    private val gptApi: GptApi = retrofit.create(GptApi::class.java)

    private var _binding: FragmentChatBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        binding.recyclerGchat.layoutManager = LinearLayoutManager(context)
        binding.recyclerGchat.apply {
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
                reverseLayout = false
            }
        }

        val messages = chatViewModel.messages
        val adapter = ChatListAdapter(messages)
        binding.recyclerGchat.adapter = adapter


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonGchatSend.setOnClickListener {
                hideKeyboard()
                showNewMessage()
            }
        }

        val newMessage = ChatMessage(
            user = "System",
            msgContent = "Welcome to the DollarSmart Chat Room. Ask him some financial questions, and he might help. Maybe."
        )

        chatViewModel.addMessage(newMessage)
        binding.recyclerGchat.adapter?.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.fragment_expense_list, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.new_expense -> {
//                showNewExpense()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private fun showNewMessage() {
        viewLifecycleOwner.lifecycleScope.launch {
            binding.editGchatMessage.setBackgroundColor(0)
            var textBoxContents = binding.editGchatMessage.text.toString()
            val newMessage = ChatMessage(
                user = "You",
                msgContent = textBoxContents
                )
            binding.editGchatMessage.text.clear()
            chatViewModel.addMessage(newMessage)
            getResponse(textBoxContents)
        }
    }

    private suspend fun getResponse(msg : String) {
        /*
        viewLifecycleOwner.lifecycleScope.launch {
            val response = gptApi.getChatResponse(msg)
            print(response)
            val cutResponse = response.drop(14)
            val cutCutResponse = cutResponse.dropLast(2)
            val newMessage = ChatMessage(
                user = "PennyWise",
                msgContent = cutCutResponse
            )

            chatViewModel.addMessage(newMessage)
            binding.recyclerGchat.adapter?.notifyDataSetChanged()
            binding.recyclerGchat.adapter?.let { binding.recyclerGchat.scrollToPosition(it.itemCount-1) }
        }
        */
        try {
            val messages = listOf(
                ChatMessage(user = "system", msgContent = "You are a helpful assistant."),
                ChatMessage(user = "user", msgContent = "Who won the world series in 2020?"),
                ChatMessage(user = "assistant", msgContent = "The Los Angeles Dodgers won the World Series in 2020."),
                ChatMessage(user = "user", msgContent = "Where was it played?")
            )

            val request = ChatRequest(model = "gpt-3.5-turbo", messages = messages)
            Log.d("ChatFragment", "Request: $request")
            val response = gptApi.getChatResponse("", request)
            val messageContent = response.choices.firstOrNull()?.message?.content ?: "No Response"

            val newMessage = ChatMessage(
                user = "PennyWise",
                msgContent = messageContent
            )

            chatViewModel.addMessage(newMessage)
            binding.recyclerGchat.adapter?.notifyDataSetChanged()
            binding.recyclerGchat.adapter?.let { binding.recyclerGchat.scrollToPosition(it.itemCount - 1) }
        } catch (e: Exception) {
            // Handle exception
            Log.e("ChatFragment", "Error: ${e.message}", e)
            e.printStackTrace()
        }
    }
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
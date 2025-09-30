package com.example.geminichat.ui.theme.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geminichat.BuildConfig
import com.example.geminichat.ui.theme.models.MessageModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _messageList = MutableStateFlow<List<MessageModel>>(emptyList())
    val messageList: StateFlow<List<MessageModel>> = _messageList.asStateFlow()

    val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    fun sendMessage(question: String){
        viewModelScope.launch {
            try {
                // Add user message immediately
                val updatedList = _messageList.value.toMutableList()
                updatedList.add(MessageModel(question, "user"))
                updatedList.add(MessageModel("Typing...", "model"))
                _messageList.value = updatedList

                val chat = generativeModel.startChat(
                    history = _messageList.value.filter { it.role != "model" || it.message != "Typing..." }.map {
                        content(it.role) { text(it.message) }
                    }
                )

                val response = chat.sendMessage(question)

                // Remove "Typing..." and add actual response
                val finalList = _messageList.value.toMutableList()
                finalList.removeAt(finalList.lastIndex)
                finalList.add(MessageModel(response.text.toString(), "model"))
                _messageList.value = finalList

            } catch (e: Exception){
                // Remove "Typing..." and add error message
                val errorList = _messageList.value.toMutableList()
                errorList.removeAt(errorList.lastIndex)
                errorList.add(MessageModel("Error: ${e.message}", "model"))
                _messageList.value = errorList
            }
        }
    }
}
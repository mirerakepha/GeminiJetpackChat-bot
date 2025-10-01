package com.example.geminichat.ui.theme.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.geminichat.ui.theme.BotColor
import com.example.geminichat.ui.theme.UserColor
import com.example.geminichat.ui.theme.data.ChatViewModel
import com.example.geminichat.ui.theme.models.MessageModel
import com.example.geminichat.R


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel
) {
    val messages by viewModel.messageList.collectAsState()

    Scaffold(
        // no topBar anymore
        bottomBar = {
            MessageInput(onSendMessage = { viewModel.sendMessage(it) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // optional centered title instead of TopAppBar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "DuckyZ",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.SansSerif
                )
            }

            MessageList(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                messageList = messages
            )
        }
    }
}


@Composable
fun MessageInput(onSendMessage: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = { message = it },
            placeholder = { Text("Type a message...", color = Color.Gray) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )
        IconButton(
            onClick = {
                if (message.isNotEmpty()) {
                    onSendMessage(message)
                    message = ""
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send message",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {
    if (messageList.isEmpty()) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.bott),
                contentDescription = "Icon",
                tint = BotColor,
                modifier = Modifier.size(60.dp)
            )
            Text(text = "DeepMind Intelligence")
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(8.dp),
            reverseLayout = true
        ) {
            items(messageList.reversed()) {
                MessageRow(messageModel = it)
            }
        }
    }
}


@Composable
fun MessageRow(messageModel: MessageModel) {
    val isModel = messageModel.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(
                        start = if (isModel) 8.dp else 70.dp,
                        end = if (isModel) 70.dp else 8.dp,
                        top = 8.dp, bottom = 8.dp
                    )
                    .clip(shape = RoundedCornerShape(48f))
                    .background(if (isModel) UserColor else BotColor)
                    .padding(16.dp)
            ) {
                SelectionContainer {
                    Text(
                        text = messageModel.message,
                        fontWeight = FontWeight.ExtraLight,
                        color = Color.White
                    )
                }
            }
        }
    }
}

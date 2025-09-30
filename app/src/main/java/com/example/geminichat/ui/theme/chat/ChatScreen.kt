package com.example.geminichat.ui.theme.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.geminichat.ui.theme.BotColor
import com.example.geminichat.ui.theme.UserColor
import com.example.geminichat.ui.theme.data.ChatViewModel
import com.example.geminichat.ui.theme.models.MessageModel
import com.example.geminichat.R


@Composable
fun ChatScreen(modifier: Modifier = Modifier, viewModel: ChatViewModel) {
    val messages by viewModel.messageList.collectAsState()

    Column(modifier = Modifier.fillMaxSize()
    ) {
        AppHeader()
        MessageList(
            modifier = Modifier.weight(1f),
            messageList = messages
        )
        MessageInput(
            onSendMessage = {
                viewModel.sendMessage(it)
            }
        )
    }
}

//Header
@Composable
fun AppHeader(){
    Box(modifier = Modifier.fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(text = "DuckyZ",
            modifier = Modifier.padding(16.dp),
            color = Color.White,
            fontSize = 22.sp
        )

    }
}

//Message input
@Composable
fun MessageInput( onSendMessage : (String) -> Unit ){
    var message by remember {mutableStateOf("")}

    Row(modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = {message = it }
        )
        IconButton(
            onClick = {
                if (message.isNotEmpty()){
                    onSendMessage(message)
                    message = ""
                }

            }
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "send message")
        }
    }

}

//MessageList
@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>){
    if (messageList.isEmpty()){
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Icon(painter = painterResource(id = R.drawable.baseline_question_answer_24),
                contentDescription = "Icon",
                tint = BotColor,
                modifier = Modifier.size(60.dp)
            )
            Text(text = "Ask me anything")
        }
    }
    else{
        LazyColumn(
            modifier = modifier,
            reverseLayout = true //messages start from down upwards
        ) {
            items(messageList.reversed()/*first message at the top*/) {
                MessageRow(messageModel = it)
            }
        }
    }


}


@Composable
fun MessageRow(messageModel: MessageModel){
//implementing the logic of different positions for chat bubble
    val isModel = messageModel.role == "model"
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box (
                modifier = Modifier
                    .align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                    .padding(start = if (isModel) 8.dp else 70.dp,
                             end = if (isModel) 70.dp else 8.dp,
                            top = 8.dp, bottom = 8.dp
                    )
                    .clip(shape = RoundedCornerShape(48f))
                    .background(if (isModel) UserColor else BotColor)
                    .padding(16.dp)
            ){
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





























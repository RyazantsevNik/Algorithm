package com.example.algorithms.screens.chat

import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.algorithms.R
import com.example.algorithms.di.ai_chat.Message
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.DarkBlue
import com.example.algorithms.viewmodels.chat.ChatViewModel
import io.noties.markwon.Markwon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel



@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = getViewModel()
) {
    var messageText by remember { mutableStateOf("") }
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()


    val dotCount = remember { mutableStateOf(1) }
    LaunchedEffect(viewModel.loadingState.value) {
        while (viewModel.loadingState.value) {
            delay(500)
            dotCount.value = (dotCount.value % 3) + 1
        }
    }


    LaunchedEffect(Unit) {
        viewModel.loadChatHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 2.dp)
    ) {
        if (viewModel.isAuthenticated.value) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier.weight(1f),
                reverseLayout = false
            ) {
                items(viewModel.chatMessages) { message ->
                    MessageBubble(message = message)
                }

                if (viewModel.loadingState.value) {
                    item {
                        MessageBubble(message = Message("assistant", "Думает${".".repeat(dotCount.value)}"))
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    BasicTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        decorationBox = { innerTextField ->
                            if (messageText.isEmpty()) {
                                Text(
                                    text = "Введите сообщение...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            innerTextField()
                        },
                        maxLines = 3
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (messageText.isNotBlank())
                                Color.Blue
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        ),
                    enabled = !viewModel.loadingState.value
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = if (messageText.isNotBlank())
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_robot_logo),
                    contentDescription = "Логотип приложения",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 24.dp),
                    contentScale = ContentScale.Fit
                )


                Text(
                    text = "Для использования чата требуется авторизация",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )


                Button(
                    onClick = { navController.navigate(AppRoutes.AUTH_SCREEN) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Войти в аккаунт",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        viewModel.errorState.value?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }

    LaunchedEffect(viewModel.chatMessages.size) {
        if (viewModel.chatMessages.isNotEmpty()) {
            coroutineScope.launch {
                scrollState.animateScrollToItem(viewModel.chatMessages.lastIndex)
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isUser = message.role == "user"

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isUser) Color(0xFFE0E0E0) else Color.White,
            tonalElevation = 2.dp,
            border = if (!isUser) BorderStroke(1.dp, Color.Gray) else null
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                if (!isUser) {
                    AndroidView(
                        factory = { context ->
                            TextView(context).apply {
                                setTextColor(android.graphics.Color.BLACK)
                                textSize = 16f
                                val markwon = Markwon.create(context)
                                markwon.setMarkdown(this, message.content)
                            }
                        }
                    )
                } else {
                    Text(
                        text = message.content,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

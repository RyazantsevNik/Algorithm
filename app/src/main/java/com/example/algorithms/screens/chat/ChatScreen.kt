package com.example.algorithms.screens.chat

import android.widget.TextView
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

    LaunchedEffect(Unit) {
        viewModel.loadChatHistory()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FAFC),
                        Color(0xFFF1F5F9)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (viewModel.isAuthenticated.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 1.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    tonalElevation = 2.dp,
                    border = BorderStroke(width = 1.dp, color = Color.LightGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_robot_logo),
                            contentDescription = "Логотип chat",
                            modifier = Modifier
                                .size(24.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = "Чат с ИИ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color(0xFF334155)
                            )
                        )
                        IconButton(
                            onClick = { viewModel.clearChatHistory() },
                            enabled = !viewModel.loadingState.value,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Очистить историю",
                                tint = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Список сообщений
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .weight(1f),
                    reverseLayout = false,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (viewModel.chatMessages.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(vertical = 32.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text = "Добрый день, ${viewModel.userName.value}",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = Color(0xFF334155),  // Темно-синий
                                            fontWeight = FontWeight.Medium
                                        ),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    Text(
                                        text = "Чем я могу помочь?",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = Color(0xFF64748B)  // Серый
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                    items(viewModel.chatMessages) { message ->
                        MessageBubble(message = message)
                    }
                    if (viewModel.loadingState.value) {
                        item {
                            MessageBubble(
                                message = Message(
                                    "assistant",
                                    ""
                                ),
                                customContent = {
                                    AnimatedDotsText()
                                }
                            )
                        }
                    }
                }
                // Отображение ошибок
                viewModel.errorState.value?.let { error ->
                    Surface(
                        modifier = Modifier
                            .padding(16.dp),
                        color = Color(0xFFFEE2E2),  // Светло-красный
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = error,
                            color = Color(0xFFDC2626),  // Красный
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Поле ввода сообщения
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color(0xFFF1F5F9),  // Светло-серый
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            BasicTextField(
                                value = messageText,
                                onValueChange = { messageText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF334155)  // Темно-синий
                                ),
                                decorationBox = { innerTextField ->
                                    if (messageText.isEmpty()) {
                                        Text(
                                            text = "Введите сообщение...",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = Color(0xFF94A3B8)  // Серый
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
                                        Color(0xFF3B82F6)  // Синий
                                    else
                                        Color(0xFFE2E8F0),  // Светло-серый
                                    shape = CircleShape
                                ),
                            enabled = !viewModel.loadingState.value
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Отправить",
                                tint = if (messageText.isNotBlank())
                                    Color.White
                                else
                                    Color(0xFF94A3B8),  // Серый
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            } else {
                // Экран для неавторизованных пользователей
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
                            .size(120.dp)
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
                            lineHeight = 28.sp,
                            fontWeight = FontWeight.Medium
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
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Войти в аккаунт",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
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
fun MessageBubble(
    message: Message,
    customContent: (@Composable () -> Unit)? = null
) {
    val isUser = message.role == "user"

    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            color = if (isUser) Color(0xFF3B82F6) else Color.White,
            tonalElevation = 2.dp,
            border = if (!isUser) BorderStroke(1.dp, Color(0xFFE2E8F0)) else null
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 280.dp)
            ) {
                if (customContent != null) {
                    customContent()
                } else if (!isUser) {
                    AndroidView(factory = { context ->
                        TextView(context).apply {
                            setTextColor(android.graphics.Color.parseColor("#334155"))
                            textSize = 16f
                            val markwon = Markwon.create(context)
                            markwon.setMarkdown(this, message.content)
                        }
                    })
                } else {
                    Text(
                        text = message.content,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@Composable
fun AnimatedDotsText(
    baseText: String = "Думает",
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF334155),
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    val dotCount by rememberInfiniteTransition(label = "dotsTransition").animateValue(
        initialValue = 0,
        targetValue = 2,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500),
            repeatMode = RepeatMode.Restart
        ),
        label = "dotCountAnim"
    )

    Text(
        text = baseText + ".".repeat(dotCount),
        style = style,
        color = color,
        modifier = modifier
    )
}
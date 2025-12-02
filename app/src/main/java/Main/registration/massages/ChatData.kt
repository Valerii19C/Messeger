package Main.registration.massages

import com.google.gson.annotations.SerializedName

/**
 * Модель для одного сообщения в чате.
 */
data class ChatMessage(
    @SerializedName("text") val text: String,
    @SerializedName("is_sent_by_user") val isSentByUser: Boolean // true, если от пользователя; false - от бота
)

/**
 * Модель для одной сессии чата с конкретным ботом.
 */
data class ChatSession(
    @SerializedName("bot_name") val botName: String,
    @SerializedName("messages") val messages: MutableList<ChatMessage> = mutableListOf()
)

/**
 * Корневая модель, которая хранит все чаты одного пользователя.
 */
data class UserChatData(
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("chats") val chats: MutableList<ChatSession> = mutableListOf()
)

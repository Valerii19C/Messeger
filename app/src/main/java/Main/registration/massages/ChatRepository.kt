package Main.registration.massages

import Main.registration.User.SessionManager
import android.content.Context
import com.google.gson.Gson
import java.io.File

object ChatRepository {

    private val gson = Gson()
    private var currentChatData: UserChatData? = null

    private fun getFile(context: Context): File? {
        val userEmail = SessionManager.currentUser?.email ?: return null
        val fileName = "chats_${userEmail}.json"
        return File(context.filesDir, fileName)
    }

    private fun save(context: Context) {
        currentChatData ?: return
        val file = getFile(context) ?: return
        val json = gson.toJson(currentChatData)
        file.writeText(json)
    }

    fun load(context: Context) {
        val file = getFile(context)
        if (file?.exists() == true) {
            val json = file.readText()
            currentChatData = gson.fromJson(json, UserChatData::class.java)
        } else {
            val userEmail = SessionManager.currentUser?.email
            if (userEmail != null) {
                currentChatData = UserChatData(userEmail = userEmail)
            } else {
                // Обработка случая, когда пользователь не вошел в систему
                currentChatData = null
            }
        }
    }

    fun unload() {
        currentChatData = null
    }

    fun getChatSessions(): List<ChatSession> {
        return currentChatData?.chats ?: emptyList()
    }

    fun getOrCreateChatSession(context: Context, botName: String): ChatSession {
        var session = currentChatData?.chats?.find { it.botName == botName }
        if (session == null) {
            session = ChatSession(botName = botName)
            currentChatData?.chats?.add(session)
            save(context)
        }
        return session
    }

    fun addMessage(context: Context, botName: String, message: ChatMessage) {
        val session = getOrCreateChatSession(context, botName)
        session.messages.add(message)
        save(context)
    }
}

package Main.registration.Chats

import Main.registration.bots.Bot
import Main.registration.bots.ChatBotSystem
import Main.registration.massages.ChatMessage
import Main.registration.massages.ChatRepository
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.messenger.R

class InChats : AppCompatActivity() {
    private lateinit var messagesContainer: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var botNameTextView: TextView
    private lateinit var messagesScrollView: ScrollView
    private var selectedBot: Bot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_chats_layout)
        hideSystemUI()

        messagesContainer = findViewById(R.id.messages_container)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_button)
        backButton = findViewById(R.id.back_button)
        botNameTextView = findViewById(R.id.bot_name_text_view)
        messagesScrollView = findViewById(R.id.messages_scroll_view)

        val botName = intent.getStringExtra("chat_name")
        if (botName != null) {
            selectedBot = ChatBotSystem.allBots.find { it.name == botName }
        }

        if (selectedBot == null) {
            botNameTextView.text = "Бот не найден"
            Toast.makeText(this, "Ошибка: чат с ботом не может быть открыт.", Toast.LENGTH_LONG).show()
            sendButton.isEnabled = false
            messageEditText.isEnabled = false
        } else {
            botNameTextView.text = selectedBot?.name
            loadChatHistory()
        }

        backButton.setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                selectedBot?.let { bot ->
                    val userMessage = ChatMessage(messageText, true)
                    addMessageToUi(userMessage)
                    ChatRepository.addMessage(this, bot.name, userMessage)
                    messageEditText.text.clear()

                    val botResponseText = ChatBotSystem.getResponse(bot, messageText)
                    val botMessage = ChatMessage(botResponseText, false)
                    addMessageToUi(botMessage)
                    ChatRepository.addMessage(this, bot.name, botMessage)

                    messagesScrollView.post { messagesScrollView.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun addMessageToUi(message: ChatMessage) {
        val textView = TextView(this).apply {
            text = message.text
            textSize = 16f
            setPadding(16, 8, 16, 8)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = if (message.isSentByUser) Gravity.END else Gravity.START
                topMargin = 8
            }

            this.layoutParams = layoutParams
            background = if (message.isSentByUser) {
                ContextCompat.getDrawable(this@InChats, R.drawable.user_message_background)
            } else {
                ContextCompat.getDrawable(this@InChats, R.drawable.bot_message_background)
            }
        }
        messagesContainer.addView(textView)
    }

    private fun loadChatHistory() {
        selectedBot?.let { bot ->
            val chatSession = ChatRepository.getOrCreateChatSession(this, bot.name)
            for (message in chatSession.messages) {
                addMessageToUi(message)
            }
        }
    }
}
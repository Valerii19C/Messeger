package Main.registration.Chats

import Main.registration.bots.Bot
import Main.registration.bots.ChatBotSystem
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
    private var selectedBot: Bot? = null // Make it nullable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_chats_layout)
        hideSystemUI()
        // 1. Находим все View по ID
        messagesContainer = findViewById(R.id.messages_container)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_button)
        backButton = findViewById(R.id.back_button)
        botNameTextView = findViewById(R.id.bot_name_text_view)
        messagesScrollView = findViewById(R.id.messages_scroll_view)

        // 2. Получаем имя бота и находим объект Bot
        val botName = intent.getStringExtra("selected_bot_name")
        if (botName != null) {
            selectedBot = ChatBotSystem.allBots.find { it.name == botName }
        }

        // 3. Проверяем, найден ли бот
        if (selectedBot == null) {
            // Если бот не найден или имя не передано, показываем ошибку и блокируем чат
            botNameTextView.text = "Бот не найден"
            Toast.makeText(this, "Ошибка: чат с ботом не может быть открыт.", Toast.LENGTH_LONG).show()
            sendButton.isEnabled = false
            messageEditText.isEnabled = false
        } else {
            botNameTextView.text = selectedBot?.name
        }


        // 4. Настраиваем кнопку "назад"
        backButton.setOnClickListener {
            finish()
        }

        // 5. Настраиваем кнопку отправки сообщения
        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                selectedBot?.let { bot ->
                    // Добавляем сообщение пользователя
                    addMessage(messageText, true)
                    messageEditText.text.clear()

                    // Получаем и добавляем ответ бота
                    val botResponse = ChatBotSystem.getResponse(bot, messageText)
                    addMessage(botResponse, false)

                    // Прокручиваем ScrollView вниз
                    messagesScrollView.post { messagesScrollView.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }
        }
    }
    private fun hideSystemUI(){
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
    private fun addMessage(message: String, isUserMessage: Boolean) {
        val textView = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(16, 8, 16, 8)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = if (isUserMessage) Gravity.END else Gravity.START
                topMargin = 8
            }

            this.layoutParams = layoutParams

            // Устанавливаем фон для сообщения
            background = if (isUserMessage) {
                ContextCompat.getDrawable(this@InChats, R.drawable.user_message_background)
            } else {
                ContextCompat.getDrawable(this@InChats, R.drawable.bot_message_background)
            }
        }

        messagesContainer.addView(textView)
    }
}

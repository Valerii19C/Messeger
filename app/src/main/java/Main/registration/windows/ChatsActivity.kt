package Main.registration.windows

import Main.registration.Chats.InChats
import Main.registration.User.Profile
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Находим контейнер
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)

        // Загружаем макет чатов и получаем доступ к его view
        val chatsView = layoutInflater.inflate(R.layout.chats_makets, contentContainer, true)

        // Настройка навигации в меню
        val userIcon = findViewById<ImageView>(R.id.user)
        val newsIcon = findViewById<ImageView>(R.id.news)

        // Находим элементы внутри загруженного макета chatsView
        val chatItem = chatsView.findViewById<LinearLayout>(R.id.chat_item_root)
        val addChatButton = chatsView.findViewById<FloatingActionButton>(R.id.add_chat_button)
        val chatUsername = chatsView.findViewById<TextView>(R.id.chat_username)

        // Получаем имя бота из Intent
        val selectedBotName = intent.getStringExtra("selected_bot_name")
        if (selectedBotName != null) {
            chatUsername.text = selectedBotName
        }

        newsIcon.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        userIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }

        chatItem?.setOnClickListener {
            val intent = Intent(this, InChats::class.java)
            startActivity(intent)
        }

        // Обработчик для кнопки "Добавить чат"
        addChatButton?.setOnClickListener {
            val intent = Intent(this, BotListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        setIntent(intent) // Обновляем intent
        val selectedBotName = getIntent().getStringExtra("selected_bot_name")
        if (selectedBotName != null) {
            val chatUsername = findViewById<TextView>(R.id.chat_username)
            chatUsername.text = selectedBotName
        }
    }
}

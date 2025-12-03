package Main.registration.windows

import Main.registration.Chats.InChats
import Main.registration.User.Profile
import Main.registration.bots.Bot
import Main.registration.bots.BotListAdapter
import Main.registration.bots.ChatBotSystem
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChatsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BotListAdapter
    private val chatList = mutableListOf<Bot>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Загружаем макет чатов, который содержит RecyclerView
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)
        val chatsViews = layoutInflater.inflate(R.layout.chats_makets, contentContainer, true)

        // Настройка RecyclerView
        recyclerView = chatsViews.findViewById(R.id.bot_list_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Загружаем ботов из ChatBotSystem
        chatList.addAll(ChatBotSystem.allBots)

        // Создаем и устанавливаем адаптер
        adapter = BotListAdapter(chatList) { bot ->
            val intent = Intent(this, InChats::class.java)
            intent.putExtra("chat_name", bot.name)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Настройка навигации
        setupNavigation()
    }

    private fun setupNavigation() {
        val userIcon = findViewById<ImageView>(R.id.user)
        val newsIcon = findViewById<ImageView>(R.id.news)

        userIcon.setOnClickListener {
            startActivity(Intent(this, Profile::class.java))
        }

        newsIcon.setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // Обновляем intent для активности
        intent?.getStringExtra("selected_bot_name")?.let { newBotName ->
        }
    }
}

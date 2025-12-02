package Main.registration.windows

import Main.registration.bots.BotListAdapter
import Main.registration.bots.ChatBotSystem
import Main.registration.massages.ChatRepository
import android.os.Bundle
import Main.registration.bots.Bot
import android.content.Intent
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R

class BotListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.bot_list_layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Находим все View по ID
        val backButton = findViewById<ImageButton>(R.id.back_button)
        val recyclerView = findViewById<RecyclerView>(R.id.bots_recycler_view)

        // 2. Настраиваем кнопку "назад"
        backButton.setOnClickListener {
            finish()
        }

        // 3. Создаем и настраиваем адаптер
        val adapter = BotListAdapter(ChatBotSystem.allBots) { bot ->
            // При нажатии на бота - создаем чат и переходим на экран чатов
            ChatRepository.getOrCreateChatSession(this, bot.name)
            val intent = Intent(this, ChatsActivity::class.java)
            intent.putExtra("selected_bot_name", bot.name)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }

        // 4. Устанавливаем LayoutManager и адаптер для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}

package Main.registration.windows

import Main.registration.User.Profile
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Находим контейнер
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)

        // Загружаем макет новостей
        layoutInflater.inflate(R.layout.news_makets, contentContainer, true)

        // Настройка навигации
        val userIcon = findViewById<ImageView>(R.id.user)
        val chatsIcon = findViewById<ImageView>(R.id.chats)

        chatsIcon.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
            finish() // Закрываем текущую активити
        }
        userIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
            finish()
        }
    }
}

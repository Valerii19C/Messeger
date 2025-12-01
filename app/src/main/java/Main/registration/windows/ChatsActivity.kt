package Main.registration.windows

import Main.registration.User.Profile
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R

class ChatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        // Находим контейнер
        val contentContainer = findViewById<LinearLayout>(R.id.content_container)

        // Загружаем макет чатов
        layoutInflater.inflate(R.layout.chats_makets, contentContainer, true)
        // Настройка навигации
        val userIcon = findViewById<ImageView>(R.id.user)
        val newsIcon = findViewById<ImageView>(R.id.news)

        newsIcon.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
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

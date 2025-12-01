package Main.registration.windows

import Main.registration.Chats.InChats
import Main.registration.User.Profile
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
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
        
        // Настройка навигации в меню
        val userIcon = findViewById<ImageView>(R.id.user)
        val newsIcon = findViewById<ImageView>(R.id.news)
        
        // Находим кликабельный элемент чата в списке, но ищем его внутри контейнера!
        val chatItem = contentContainer.findViewById<LinearLayout>(R.id.chat_item_root)

        newsIcon.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        userIcon.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent)
        }
        
        // Добавляем обработчик нажатия для элемента чата
        // Проверяем, что chatItem не null, чтобы избежать крэша
        chatItem?.setOnClickListener {
            val intent = Intent(this, InChats::class.java)
            startActivity(intent)
        }
    }
}

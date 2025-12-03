package Main.registration.regis

import Main.registration.Data.Users
import Main.registration.User.SessionManager
import Main.registration.massages.ChatRepository
import Main.registration.windows.ChatsActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.google.gson.Gson
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val registrationButton = findViewById<Button>(R.id.registration)
        val entranceButton = findViewById<Button>(R.id.entrance)
        emailEditText = findViewById(R.id.Email)
        passwordEditText = findViewById(R.id.Password)

        registrationButton.setOnClickListener {
            val intent = Intent(this, EntranceActivity::class.java)
            startActivity(intent)
        }

        entranceButton.setOnClickListener {
            handleEntranceButtonClick()
        }
    }

    private fun handleEntranceButtonClick() {
        val file = File(filesDir, "user.json")
        if (file.exists()) {
            val userJson = file.readText()
            val gson = Gson()
            val savedUser = gson.fromJson(userJson, Users::class.java)

            val enteredEmail = emailEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            if (savedUser.email == enteredEmail && savedUser.password == enteredPassword) {
                SessionManager.currentUser = savedUser // Assuming Users object has email property
                ChatRepository.load(this)
                Toast.makeText(this, "Добро пожаловать", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChatsActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Вы не зарегистрированы", Toast.LENGTH_SHORT).show()
        }
    }
}

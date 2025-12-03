package Main.registration.regis

import Main.registration.Data.Users
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class EntranceActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.entrance_layout)
        val registration = findViewById<Button>(R.id.reg)
        val email = findViewById<EditText>(R.id.editTextEmail)
        val username = findViewById<EditText>(R.id.user2)
        val password = findViewById<EditText>(R.id.password2)

        registration.setOnClickListener {
            val emailText = email.text.toString()
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (emailText.isNotEmpty() && usernameText.isNotEmpty() && passwordText.isNotEmpty()) {
                val user = Users(
                    email = emailText,
                    username = usernameText,
                    password = passwordText
                )
                saveUser(user)
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveUser(user: Users) {
        val gson = Gson()
        val file = File(filesDir, "users.json")

        val users: MutableList<Users> = if (file.exists()) {
            val json = file.readText()
            gson.fromJson(json, object : TypeToken<MutableList<Users>>() {}.type)
        } else {
            mutableListOf()
        }

        if (users.any { it.email == user.email }) {
            Toast.makeText(this, "Пользователь с такой почтой уже существует", Toast.LENGTH_SHORT).show()
        } else {
            users.add(user)
            val updatedJson = gson.toJson(users)
            file.writeText(updatedJson)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
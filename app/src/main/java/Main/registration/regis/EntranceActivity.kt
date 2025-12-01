package Main.registration.regis

import Main.registration.Data.Users
import Main.registration.regis.MainActivity
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
        val userJson = gson.toJson(user)

        val file = File(filesDir, "user.json")
        file.writeText(userJson)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
package Main.registration.User

import Main.registration.Data.Users
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.google.gson.Gson
import java.io.File

class Profile: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.users)

        val back = findViewById<ImageButton>(R.id.back)
        val aboutButton = findViewById<Button>(R.id.about_button)
        val usernameTextView = findViewById<TextView>(R.id.username)
        val emailTextView = findViewById<TextView>(R.id.email)

        loadUserData(usernameTextView, emailTextView)

        back.setOnClickListener {
            finish()
        }

        aboutButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("О приложении")
            builder.setMessage("Разработчик: Синева Валерия")
            builder.setPositiveButton("Назад") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun loadUserData(usernameTextView: TextView, emailTextView: TextView) {
        val file = File(filesDir, "user.json")
        if (file.exists()) {
            val userJson = file.readText()
            val gson = Gson()
            val savedUser = gson.fromJson(userJson, Users::class.java)

            usernameTextView.text = savedUser.username
            emailTextView.text = savedUser.email
        } else {
            Toast.makeText(this, "Не удалось загрузить данные пользователя", Toast.LENGTH_SHORT).show()
        }
    }
}

package Main.registration.Chats

import Main.registration.bots.Bot
import Main.registration.bots.ChatBotSystem
import Main.registration.massages.ChatMessage
import Main.registration.massages.ChatRepository
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.messenger.R
import java.io.File

class InChats : AppCompatActivity() {
    private lateinit var messagesContainer: LinearLayout
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var botNameTextView: TextView
    private lateinit var messagesScrollView: ScrollView
    private lateinit var attachFileButton: ImageButton
    private lateinit var cameraButton: ImageButton
    private var selectedBot: Bot? = null
    private var tempImageUri: Uri? = null

    private val requestFilePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openFileManager()
            } else {
                Toast.makeText(this, "Permission denied to access files", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val fileUri: Uri? = data?.data
                fileUri?.let { uri ->
                    val newUri = PhotoFile.saveFileToInternalStorage(this, uri)
                    val message = ChatMessage(fileUri = newUri.toString(), isSentByUser = true)
                    addMessageToUi(message)
                    selectedBot?.let {
                        ChatRepository.addMessage(this, it.name, message)
                    }
                }
            }
        }

    private val requestImagePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                Toast.makeText(this, "Permission denied to access gallery", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickGalleryImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageUri: Uri? = data?.data
                imageUri?.let { uri ->
                    val newUri = PhotoFile.saveFileToInternalStorage(this, uri)
                    val message = ChatMessage(imageUri = newUri.toString(), isSentByUser = true)
                    addMessageToUi(message)
                    selectedBot?.let {
                        ChatRepository.addMessage(this, it.name, message)
                    }
                }
            }
        }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Permission denied to use camera", Toast.LENGTH_SHORT).show()
            }
        }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempImageUri?.let { uri ->
                    val newUri = PhotoFile.saveFileToInternalStorage(this, uri)
                    val message = ChatMessage(imageUri = newUri.toString(), isSentByUser = true)
                    addMessageToUi(message)
                    selectedBot?.let {
                        ChatRepository.addMessage(this, it.name, message)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.in_chats_layout)
        hideSystemUI()

        messagesContainer = findViewById(R.id.messages_container)
        messageEditText = findViewById(R.id.message_edit_text)
        sendButton = findViewById(R.id.send_button)
        backButton = findViewById(R.id.back_button)
        botNameTextView = findViewById(R.id.bot_name_text_view)
        messagesScrollView = findViewById(R.id.messages_scroll_view)
        attachFileButton = findViewById(R.id.attach_file_button)
        cameraButton = findViewById(R.id.camera_button)

        val botName = intent.getStringExtra("chat_name")
        if (botName != null) {
            selectedBot = ChatBotSystem.allBots.find { it.name == botName }
        }

        if (selectedBot == null) {
            botNameTextView.text = "Бот не найден"
            Toast.makeText(this, "Ошибка: чат с ботом не может быть открыт.", Toast.LENGTH_LONG).show()
            sendButton.isEnabled = false
            messageEditText.isEnabled = false
        } else {
            botNameTextView.text = selectedBot?.name
            loadChatHistory()
        }

        backButton.setOnClickListener {
            finish()
        }

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotBlank()) {
                selectedBot?.let { bot ->
                    val userMessage = ChatMessage(text = messageText, isSentByUser = true)
                    addMessageToUi(userMessage)
                    ChatRepository.addMessage(this, bot.name, userMessage)
                    messageEditText.text.clear()

                    val botResponseText = ChatBotSystem.getResponse(bot, messageText)
                    val botMessage = ChatMessage(text = botResponseText, isSentByUser = false)
                    addMessageToUi(botMessage)
                    ChatRepository.addMessage(this, bot.name, botMessage)

                    messagesScrollView.post { messagesScrollView.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }
        }

        attachFileButton.setOnClickListener {
            showAttachFileDialog()
        }

        cameraButton.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
    private fun openCamera() {
        Kamera.getTmpFileUri(this).let { uri ->
            tempImageUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    private fun showAttachFileDialog() {
        val options = arrayOf("Прикрепить файл", "Отправить изображение")
        AlertDialog.Builder(this)
            .setTitle("Выберите действие")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkFilePermissionAndOpenFilePicker()
                    1 -> checkPermissionAndOpenGallery()
                }
            }
            .show()
    }

    private fun checkFilePermissionAndOpenFilePicker() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES // Or other specific media types
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                openFileManager()
            }
            else -> {
                requestFilePermissionLauncher.launch(permission)
            }
        }
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        pickFileLauncher.launch(intent)
    }

    private fun checkPermissionAndOpenGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            else -> {
                requestImagePermissionLauncher.launch(permission)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickGalleryImageLauncher.launch(intent)
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    private fun addMessageToUi(message: ChatMessage) {
        if (message.imageUri != null) {
            val imageView = ImageView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    500, // width
                    500 // height
                ).apply {
                    gravity = if (message.isSentByUser) Gravity.END else Gravity.START
                    topMargin = 8
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                val imageUri = Uri.parse(message.imageUri)
                try {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    setImageBitmap(bitmap)
                } catch (e: Exception) {
                    setImageResource(R.drawable.ic_launcher_background) // Example placeholder
                }

            }
            messagesContainer.addView(imageView)
        } else if (message.fileUri != null) {
            val textView = TextView(this).apply {
                text = "File: ${ChatFileUtils.getFileName(this@InChats, Uri.parse(message.fileUri))}"
                textSize = 16f
                setPadding(16, 8, 16, 8)
                // Add icon and click listener to open the file
                setOnClickListener {
                    try {
                        val file = File(Uri.parse(message.fileUri).path!!)
                        val fileUri = FileProvider.getUriForFile(this@InChats, "${applicationContext.packageName}.provider", file)
                        val openIntent = Intent(Intent.ACTION_VIEW)
                        openIntent.data = fileUri
                        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        val mimeType = contentResolver.getType(fileUri)
                        openIntent.setDataAndType(fileUri, mimeType)

                        startActivity(openIntent)
                    } catch (e: Exception) {
                        Toast.makeText(this@InChats, "Could not open file: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            messagesContainer.addView(textView)

        } else {
            val textView = TextView(this).apply {
                text = message.text
                textSize = 16f
                setPadding(16, 8, 16, 8)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = if (message.isSentByUser) Gravity.END else Gravity.START
                    topMargin = 8
                }

                this.layoutParams = layoutParams
                background = if (message.isSentByUser) {
                    ContextCompat.getDrawable(this@InChats, R.drawable.user_message_background)
                } else {
                    ContextCompat.getDrawable(this@InChats, R.drawable.bot_message_background)
                }
            }
            messagesContainer.addView(textView)
        }
    }

    private fun loadChatHistory() {
        selectedBot?.let { bot ->
            val chatSession = ChatRepository.getOrCreateChatSession(this, bot.name)
            for (message in chatSession.messages) {
                addMessageToUi(message)
            }
        }
    }
}

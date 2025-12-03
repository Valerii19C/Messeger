package Main.registration.User

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.messenger.R

class Photo(private val activity: AppCompatActivity, private val profileImageView: ImageView) {

    private val requestPermissionLauncher: ActivityResultLauncher<String>
    private val pickImageLauncher: ActivityResultLauncher<Intent>

    init {
        requestPermissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchGallery()
            } else {
                Toast.makeText(activity, "Для выбора фото требуется разрешение на доступ к галерее", Toast.LENGTH_SHORT).show()
            }
        }

        pickImageLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                imageUri?.let {
                    profileImageView.setImageURI(it)
                }
            }
        }
    }

    fun changeProfilePhoto() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                launchGallery()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    fun viewProfilePhoto() {
        val builder = AlertDialog.Builder(activity)
        val inflater = LayoutInflater.from(activity)
        val dialogView = inflater.inflate(R.layout.dialog_view_photo, null)
        val fullScreenImage = dialogView.findViewById<ImageView>(R.id.full_screen_image)

        fullScreenImage.setImageDrawable(profileImageView.drawable)

        builder.setView(dialogView)
        builder.setPositiveButton("Назад") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }
}

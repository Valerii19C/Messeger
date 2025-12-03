package Main.registration.Chats

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object Kamera {

    fun getTmpFileUri(context: Context): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", context.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.provider", tmpFile)
    }
}

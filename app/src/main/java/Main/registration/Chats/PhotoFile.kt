package Main.registration.Chats

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object PhotoFile {

    fun saveFileToInternalStorage(context: Context, uri: Uri): Uri {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = ChatFileUtils.getFileName(context, uri)
        val file = File(context.filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return Uri.fromFile(file)
    }
}

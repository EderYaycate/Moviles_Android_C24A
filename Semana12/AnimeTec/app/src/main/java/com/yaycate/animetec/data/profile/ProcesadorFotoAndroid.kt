package com.yaycate.animetec.data.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ProcesadorFotoAndroid(
    private val context: Context
) : ProcesadorFoto {

    override suspend fun aBase64(uri: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val parsedUri = Uri.parse(uri)
            
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            var inSampleSize = 1
            val reqWidth = 512
            val reqHeight = 512
            if (options.outHeight > reqHeight || options.outWidth > reqWidth) {
                val halfHeight = options.outHeight / 2
                val halfWidth = options.outWidth / 2
                while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
            }
            
            val bitmap = context.contentResolver.openInputStream(parsedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, decodeOptions)
            } ?: return@withContext Result.failure(Exception("No se pudo leer la imagen"))

            val width = bitmap.width
            val height = bitmap.height
            val dimension = minOf(width, height)
            val x = (width - dimension) / 2
            val y = (height - dimension) / 2
            val squareBitmap = Bitmap.createBitmap(bitmap, x, y, dimension, dimension)
            if (squareBitmap != bitmap) {
                bitmap.recycle()
            }

            val scaledBitmap = Bitmap.createScaledBitmap(squareBitmap, 256, 256, true)
            if (scaledBitmap != squareBitmap) {
                squareBitmap.recycle()
            }

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            scaledBitmap.recycle()

            val bytes = outputStream.toByteArray()
            val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
            Result.success(base64String)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

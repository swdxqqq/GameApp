package com.example.gameapp.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.storage.storage
import com.example.gameapp.data.remote.SupabaseClient
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SupabaseStorageRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val storage = SupabaseClient.client.storage
    private val bucketName = "game-images"

    suspend fun uploadImage(uri: Uri): Result<String> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("Failed to open image"))

            val bytes = inputStream.readBytes()
            inputStream.close()

            val fileName = "game_${UUID.randomUUID()}.jpg"
            val path = fileName

            storage.from(bucketName).upload(path, bytes)

            val publicUrl = storage.from(bucketName).publicUrl(path)

            Result.success(publicUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

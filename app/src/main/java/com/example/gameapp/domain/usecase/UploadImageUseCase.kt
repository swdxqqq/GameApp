package com.example.gameapp.domain.usecase

import android.net.Uri
import com.example.gameapp.data.repository.SupabaseStorageRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val storageRepository: SupabaseStorageRepository
) {
    suspend operator fun invoke(uri: Uri): Result<String> {
        return storageRepository.uploadImage(uri)
    }
}

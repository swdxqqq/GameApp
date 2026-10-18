package com.example.gameapp.presentation.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.gameapp.domain.model.Game
import com.example.gameapp.presentation.viewmodel.GamesViewModel
import kotlinx.coroutines.launch

@Composable
fun AddGameDialog(
    onDismiss: () -> Unit,
    onAddGame: (Game) -> Unit,
    viewModel: GamesViewModel
) {
    var name by remember { mutableStateOf("") }
    var released by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var dateError by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf<String?>(null) }
    
    val coroutineScope = rememberCoroutineScope()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    fun isValidDate(date: String): Boolean {
        if (date.isBlank()) return true // optional field
        val regex = Regex("""^\d{4}-\d{2}-\d{2}$""")
        return regex.matches(date)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add your game") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = rating,
                    onValueChange = { rating = it },
                    label = { Text("Rating *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    placeholder = { Text("0.0 - 5.0") }
                )

                OutlinedTextField(
                    value = released,
                    onValueChange = {
                        released = it
                        dateError = !isValidDate(it)
                    },
                    label = { Text("Released date") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("YYYY-MM-DD") },
                    isError = dateError,
                    supportingText = if (dateError) {
                        { Text("Format: YYYY-MM-DD", color = MaterialTheme.colorScheme.error) }
                    } else null
                )

                // Image picker button
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (imageUri == null) "Choose image *" else "Image chosen")
                }

                // Upload status
                if (isUploading) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Uploading to Supabase...")
                    }
                }

                uploadError?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Image preview
                imageUri?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()
                        && rating.isNotBlank()
                        && imageUri != null
                        && isValidDate(released)
                    ) {
                        isUploading = true
                        uploadError = null
                        
                        coroutineScope.launch {
                            viewModel.uploadImage(imageUri!!).onSuccess { publicUrl ->
                                val game = Game(
                                    id = System.currentTimeMillis().toInt(),
                                    name = name,
                                    released = released.ifBlank { null },
                                    backgroundImage = publicUrl,
                                    rating = rating.toDoubleOrNull() ?: 0.0,
                                    metacritic = null,
                                    playtime = 0,
                                    genres = emptyList()
                                )
                                onAddGame(game)
                                isUploading = false
                                onDismiss()
                            }.onFailure { e ->
                                uploadError = "Upload failed: ${e.message}"
                                isUploading = false
                            }
                        }
                    }
                },
                enabled = name.isNotBlank()
                        && rating.isNotBlank()
                        && imageUri != null
                        && !dateError
                        && !isUploading
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
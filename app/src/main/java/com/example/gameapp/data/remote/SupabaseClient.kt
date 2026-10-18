package com.example.gameapp.data.remote

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://icupsjoffocjfkhjlisx.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImljdXBzam9mZm9jamZraGpsaXN4Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTIzNDY4MjksImV4cCI6MjEwNzkyMjgyOX0.Eiq0FPArURJmdIzxtAsGo4ZdwZzvvhB0qQvunkh26Zw"
        ) {
            install(Storage)
        }
    }
}

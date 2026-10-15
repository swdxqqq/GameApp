package com.example.gameapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gameapp.data.local.dao.CustomGameDao
import com.example.gameapp.data.local.dao.FavoriteGameDao
import com.example.gameapp.data.local.entity.CustomGameEntity
import com.example.gameapp.data.local.entity.FavoriteGameEntity

@Database(
    entities = [FavoriteGameEntity::class, CustomGameEntity::class],
    version = 2,
    exportSchema = false
)
abstract class GameAppDatabase : RoomDatabase() {
    abstract fun favoriteGameDao(): FavoriteGameDao
    abstract fun customGameDao(): CustomGameDao

}
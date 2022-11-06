package com.example.game

import android.app.Application
import com.example.game.data.GameRoomDatabase


class GameApplication : Application() {
    val database: GameRoomDatabase by lazy { GameRoomDatabase.getDatabase(this) }
}

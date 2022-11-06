package com.example.game.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val gameName: String,
    @ColumnInfo(name = "price")
    val gamePrice: Double,
    @ColumnInfo(name = "quantity")
    val quantityInStock: Int,
)

fun Game.getFormattedPrice(): String =
    NumberFormat.getCurrencyInstance().format(gamePrice)
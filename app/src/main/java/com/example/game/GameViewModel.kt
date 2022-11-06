package com.example.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.game.data.Game
import com.example.game.data.GameDao
import kotlinx.coroutines.launch

class GameViewModel(private val gameDao: GameDao) : ViewModel() {

    val allGames: LiveData<List<Game>> = gameDao.getGames().asLiveData()

    fun isStockAvailable(game: Game): Boolean {
        return (game.quantityInStock > 0)
    }

    fun updateGame(
        gameId: Int,
        gameName: String,
        gamePrice: String,
        gameCount: String
    ) {
        val updatedGame = getUpdatedGameEntry(gameId, gameName, gamePrice, gameCount)
        updateGame(updatedGame)
    }

    private fun updateGame(game: Game) {
        viewModelScope.launch {
            gameDao.update(game)
        }
    }

    fun sellGame(game: Game) {
        if (game.quantityInStock > 0) {
            // Decrease the quantity by 1
            val newGame = game.copy(quantityInStock = game.quantityInStock - 1)
            updateGame(newGame)
        }
    }

    fun addNewGame(gameName: String, gamePrice: String, gameCount: String) {
        val newGame = getNewGameEntry(gameName, gamePrice, gameCount)
        insertGame(newGame)
    }

    private fun insertGame(game: Game) {
        viewModelScope.launch {
            gameDao.insert(game)
        }
    }

    fun deleteGame(game: Game) {
        viewModelScope.launch {
            gameDao.delete(game)
        }
    }

    fun retrieveGame(id: Int): LiveData<Game> {
        return gameDao.getDame(id).asLiveData()
    }

    fun isEntryValid(gameName: String, gamePrice: String, gameCount: String): Boolean {
        if (gameName.isBlank() || gamePrice.isBlank() || gameCount.isBlank()) {
            return false
        }
        return true
    }

    private fun getNewGameEntry(gameName: String, gamePrice: String, gameCount: String): Game {
        return Game(
            gameName = gameName,
            gamePrice = gamePrice.toDouble(),
            quantityInStock = gameCount.toInt()
        )
    }

    private fun getUpdatedGameEntry(
        gameId: Int,
        gameName: String,
        gamePrice: String,
        gameCount: String
    ): Game {
        return Game(
            id = gameId,
            gameName = gameName,
            gamePrice = gamePrice.toDouble(),
            quantityInStock = gameCount.toInt()
        )
    }
}

class InventoryViewModelFactory(private val gameDao: GameDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(gameDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


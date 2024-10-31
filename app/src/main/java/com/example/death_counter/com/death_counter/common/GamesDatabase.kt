package com.example.death_counter.com.death_counter.common

import com.example.death_counter.com.death_counter.Game
import com.example.death_counter.com.death_counter.GameInputDTO

object GamesDatabase {
    private val Games = mutableListOf(
        Game(1, "Super Mario Odyssey", 30, false),
        Game(2, "The Legend of Zelda: Breath of the Wild", 50, true),
        Game(3, "Animal Crossing: New Horizons", 13, false),
        Game(4, "Splatoon 2", 240, false),
        Game(5, "Mario Kart 8 Deluxe", 40, false),
        Game(6, "Super Smash Bros. Ultimate", 12, false),
        Game(7, "Metroid Dread", 54, false),
        Game(8, "Fire Emblem: Three Houses", 1, false),
        Game(9, "Luigi's Mansion 3", 65, false),
        Game(10, "Pokemon Sword and Shield", 77, false)
    )
    fun getById(id:Int) = Games.find { it.id == id }

    fun getAll() = Games

    fun createGame(game: GameInputDTO) {
        val newId = if (Games.isNotEmpty()) Games.last().id + 1 else 1
        val toReturn = Game(newId, game.nome, game.deaths, game.favorite)
        Games.add(toReturn)
    }
    fun editGame(game: Game) {
        val index = Games.indexOfFirst { it.id == game.id }
        if (index != -1) {
            Games[index] = game
        }
    }

    fun changeDeathsOnState(id: Int, deaths: Int) {
        val index = Games.indexOfFirst { it.id == id }
        if (index != -1) {
            Games[index].deaths = deaths
        }
    }
    fun updateGameFavoriteStatus(id: Int, isFavorite: Boolean) {
        val game = Games.find { it.id == id }
        game?.favorite = isFavorite
    }


}
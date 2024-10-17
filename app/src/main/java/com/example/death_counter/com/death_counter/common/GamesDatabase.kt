package com.example.death_counter.com.death_counter.common

import com.example.death_counter.com.death_counter.Game

object GamesDatabase {
    private val Games = listOf(
        Game(1, "Super Mario Odyssey", 30),
        Game(2, "The Legend of Zelda: Breath of the Wild", 50),
        Game(3, "Animal Crossing: New Horizons", 13),
        Game(4, "Splatoon 2", 240),
        Game(5, "Mario Kart 8 Deluxe", 40),
        Game(6, "Super Smash Bros. Ultimate", 12),
        Game(7, "Metroid Dread", 54),
        Game(8, "Fire Emblem: Three Houses", 1),
        Game(9, "Luigi's Mansion 3", 65),
        Game(10, "Pokemon Sword and Shield", 77)
    )
    fun getById(id:Int) = Games.find { it.id == id }

    fun getAll() = Games
}
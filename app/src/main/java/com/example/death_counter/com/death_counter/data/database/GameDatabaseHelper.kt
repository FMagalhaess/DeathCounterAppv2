package com.example.death_counter.com.death_counter.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.death_counter.com.death_counter.Game
import com.example.death_counter.com.death_counter.GameAdapter
import com.example.death_counter.com.death_counter.GameInputDTO
import com.example.death_counter.com.death_counter.common.GamesDatabas

class GameDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION),
    List<Game> {

    companion object {
        private const val DATABASE_NAME = "game_database.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_GAMES = "games"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_DEATHS = "deaths"
        const val COLUMN_FAVORITE = "favorite"
    }
    private lateinit var adapter: GameAdapter;
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_GAMES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_DEATHS INTEGER,
                $COLUMN_FAVORITE INTEGER DEFAULT 0
            )
        """
        db?.execSQL(createTable)
        insertInitialGames(db!!)
    }
    private fun insertInitialGames(db: SQLiteDatabase) {
        val games = listOf(
            Game(1, "Super Mario Odyssey", 30, false),
            Game(2, "The Legend of Zelda: Breath of the Wild", 50, false),
            Game(3, "Animal Crossing: New Horizons", 13, false),
            Game(4, "Splatoon 2", 240, false),
            Game(5, "Mario Kart 8 Deluxe", 40, true),
            Game(6, "Super Smash Bros. Ultimate", 12, false),
            Game(7, "Metroid Dread", 54, false),
            Game(8, "Fire Emblem: Three Houses", 1, false),
            Game(9, "Luigi's Mansion 3", 65, false),
            Game(10, "Pokemon Sword and Shield", 77, false)
        )

        val insertQuery = "INSERT INTO $TABLE_GAMES ($COLUMN_ID, $COLUMN_NAME, $COLUMN_DEATHS, $COLUMN_FAVORITE) VALUES (?, ?, ?, ?)"
        for (game in games) {
            val statement = db.compileStatement(insertQuery)
            statement.bindLong(1, game.id.toLong())
            statement.bindString(2, game.nome)
            statement.bindLong(3, game.deaths.toLong())
            statement.bindLong(4, if (game.favorite) 1 else 0) // 1 para true, 0 para false
            statement.executeInsert()
        }
    }

    fun setAdapter(adapter: GameAdapter) {
        this.adapter = adapter
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_GAMES")
        onCreate(db)
    }
    fun createGame(game: GameInputDTO) : Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, game.nome)
            put(COLUMN_DEATHS, game.deaths)
            put(COLUMN_FAVORITE, if (game.favorite) 1 else 0)
        }
        val newRowId = db.insert(TABLE_GAMES, null, values)
        db.close()
        return newRowId
    }
    fun getAllGames(): List<Game> {
        val games = mutableListOf<Game>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_GAMES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val deaths = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEATHS))
                val favorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)) == 1

                val game = Game(id, name, deaths, favorite)
                games.add(game)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return games
    }
    fun updateGameFavoriteStatus(id: Int, isFavorite: Boolean) {
        val db = writableDatabase

        // Desmarca todos os outros jogos como não favoritos
        val resetFavorites = ContentValues().apply {
            put(GameDatabaseHelper.COLUMN_FAVORITE, 0) // 0 significa "não favorito"
        }
        db.update(
            GameDatabaseHelper.TABLE_GAMES,
            resetFavorites,
            null,
            null
        )

        val updateFavorite = ContentValues().apply {
            put(GameDatabaseHelper.COLUMN_FAVORITE, if (isFavorite) 1 else 0)
        }
        db.update(
            GameDatabaseHelper.TABLE_GAMES,
            updateFavorite,
            "${GameDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )

        adapter.notifyDataSetChanged()
        db.close()
    }

    fun updateDeaths(gameId: Int, newDeaths: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DEATHS, newDeaths)
        }

        val rowsAffected = db.update(
            TABLE_GAMES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(gameId.toString())
        )

        db.close()
        return rowsAffected
    }
    fun editGame(game: Game): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, game.nome)
            put(COLUMN_DEATHS, game.deaths)
            put(COLUMN_FAVORITE, if (game.favorite) 1 else 0)
        }
        val rowsUpdated = db.update(
            TABLE_GAMES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(game.id.toString())
        )
        db.close()
        return rowsUpdated
    }

    fun getById(id:Int): Game? {
        val db = this.writableDatabase
        val cursor = db.query(
            TABLE_GAMES,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_DEATHS, COLUMN_FAVORITE),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var game: Game? = null
        if (cursor.moveToFirst()) {
            val gameId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val gameName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            val gameDeaths = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DEATHS))
            val gameFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE)) == 1

            game = Game(gameId, gameName, gameDeaths, gameFavorite)
        }
        cursor.close()
        db.close()

        return game
    }
    fun deleteGameById(gameId: Int) {
        val db = writableDatabase
        db.delete("games", "id = ?", arrayOf(gameId.toString()))
        db.close()
    }


    override val size: Int
        get() = TODO("Not yet implemented")

    override fun get(index: Int): Game {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<Game> {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<Game> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<Game> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Game> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: Game): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: Game): Int {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<Game>): Boolean {
        TODO("Not yet implemented")
    }

    override fun contains(element: Game): Boolean {
        TODO("Not yet implemented")
    }
}

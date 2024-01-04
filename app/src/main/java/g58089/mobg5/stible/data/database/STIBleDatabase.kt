package g58089.mobg5.stible.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import g58089.mobg5.stible.data.dto.GameRecap
import g58089.mobg5.stible.data.dto.GuessResponse

/**
 * Room Database providing DAOs for the games history and the current game session.
 */
@Database(entities = [GameRecap::class, GuessResponse::class], version = 3)
abstract class STIBleDatabase : RoomDatabase() {
    abstract fun currentSessionDao(): CurrentSessionDao
    abstract fun gameHistoryDao(): GameHistoryDao

    companion object {
        private const val DATABASE_NAME = "stible_db"

        /* @Volatile ensures it's not cached and makes i/o on this field atomic.
        * Good to prevent a thread seeing Instance as null, creating it but at the same time
        * another thread sees it as null and creates it too.
        * tbh, this should never happen, but the google tutorial does it.
        */
        @Volatile
        private var Instance: STIBleDatabase? = null

        /**
         * Returns a unique instance to the [STIBleDatabase]
         */
        fun getDatabase(context: Context): STIBleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, STIBleDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration() // if schema changes, just wipe out everything lol
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
package com.reeloop.app.data
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reeloop.app.domain.model.Objeto
import com.reeloop.app.domain.repository.ObjetoDao

@Database(entities = [Objeto::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun objetoDao(): ObjetoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reloop_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
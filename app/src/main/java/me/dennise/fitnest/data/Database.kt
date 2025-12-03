package me.dennise.fitnest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.dennise.fitnest.data.entities.User
import me.dennise.fitnest.data.entities.UserDao
import me.dennise.fitnest.data.entities.Workout
import me.dennise.fitnest.data.entities.WorkoutDao

@Database(entities = [User::class, Workout::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .fallbackToDestructiveMigration() // For database migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
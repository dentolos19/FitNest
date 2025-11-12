package me.dennise.fitnest.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Workout::class], version = 2, exportSchema = false)
abstract class FitNestDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workoutDao(): WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: FitNestDatabase? = null

        fun getDatabase(context: Context): FitNestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitNestDatabase::class.java,
                    "fitnest"
                )
                    .fallbackToDestructiveMigration() // allows version upgrades
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
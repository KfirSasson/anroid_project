import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinfinal.Model.Training
import com.example.kotlinfinal.Model.User

// Annotate the class as a Room database
@Database(entities = [User::class, Training::class], version = 1, exportSchema = false)
abstract class AppLocalDb : RoomDatabase() {

    // Define DAOs
    abstract fun userDao(): UserDao
    abstract fun trainingDao(): TrainingDao

    // Singleton instance
    companion object {
        @Volatile
        private var INSTANCE: AppLocalDb? = null

        fun getDatabase(context: Context): AppLocalDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppLocalDb::class.java,
                    "app_local_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

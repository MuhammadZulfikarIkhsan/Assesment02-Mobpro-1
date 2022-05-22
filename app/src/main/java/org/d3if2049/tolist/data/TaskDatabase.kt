package org.d3if2049.tolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.d3if2049.tolist.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Mencuci baju", completed = true))
                dao.insert(Task("Membuat kue"))
                dao.insert(Task("Olahraga"))
                dao.insert(Task("Belanja bahan kue", important = true))
                dao.insert(Task("Memperbaiki motor"))
                dao.insert(Task("Pergi sekolah", important = true))
                dao.insert(Task("Telpon host webinar", important = true))
                dao.insert(Task("Webinar business"))
            }
        }
    }
}
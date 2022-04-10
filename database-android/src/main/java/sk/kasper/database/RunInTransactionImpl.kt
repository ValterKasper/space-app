package sk.kasper.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class RunInTransactionImpl @Inject constructor(private val database: SpaceRoomDatabase) : RunInTransaction {

    @Suppress("DEPRECATION")
    override suspend fun invoke(block: suspend () -> Unit) {
        // todo D: find and use not deprecated methods
        withContext(Dispatchers.IO) {
            database.beginTransaction()
            try {
                block()
                database.setTransactionSuccessful()
            } finally {
                database.endTransaction()
            }
        }

    }

}
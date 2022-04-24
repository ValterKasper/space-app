package sk.kasper.database

import androidx.room.withTransaction
import javax.inject.Inject

internal class RunInTransactionImpl @Inject constructor(private val database: SpaceRoomDatabase) : RunInTransaction {

    override suspend fun invoke(block: suspend () -> Unit) {
        database.withTransaction {
            block()
        }
    }

}
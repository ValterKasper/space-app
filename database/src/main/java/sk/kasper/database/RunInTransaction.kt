package sk.kasper.database

fun interface RunInTransaction {

    suspend operator fun invoke(block: suspend () -> Unit)

}
package sk.kasper.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.database.RunInTransaction
import sk.kasper.database.RunInTransactionImpl

@InstallIn(SingletonComponent::class)
@Module
internal class TransactionRunnerModule {

    @Provides
    internal fun providesRunInDatabase(impl: RunInTransactionImpl): RunInTransaction = impl

}
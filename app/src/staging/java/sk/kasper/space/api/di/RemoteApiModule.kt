package sk.kasper.space.api.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.remote.RemoteApi

@InstallIn(SingletonComponent::class)
@Module
interface RemoteApiModule {

    @Binds
    fun bindRemoteApi(fakeRemoteApi: FakeRemoteApi): RemoteApi

}
package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.space.notification.showLaunchNotificationJob.ShowLaunchNotificationJob
import sk.kasper.space.sync.SyncJobService


@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    internal abstract fun bindSyncJobService(): SyncJobService

    @ContributesAndroidInjector
    internal abstract fun bindShowLaunchNotificationJob(): ShowLaunchNotificationJob

}
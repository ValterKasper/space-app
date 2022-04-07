package sk.kasper.ui_common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import sk.kasper.base.SettingsManager
import sk.kasper.base.init.AppInitializer
import sk.kasper.base.notification.NotificationsHelper
import sk.kasper.ui_common.analytics.AnalyticsInitializer
import sk.kasper.ui_common.notification.NotificationsHelperImpl
import sk.kasper.ui_common.settings.SettingsManagerImpl

@InstallIn(SingletonComponent::class)
@Module
internal class UiCommonModule {

    @Provides
    fun bindsSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl

    @Provides
    fun providesNotificationsHelper(impl: NotificationsHelperImpl): NotificationsHelper = impl

    @Provides
    @IntoSet
    fun providesAnalyticsInitializer(initializer: AnalyticsInitializer): AppInitializer = initializer

}
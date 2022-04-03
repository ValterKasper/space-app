package sk.kasper.ui_common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.kasper.base.SettingsManager
import sk.kasper.ui_common.settings.SettingsManagerImpl
import sk.kasper.ui_common.tag.MapToDomainTag
import sk.kasper.ui_common.tag.MapToUiTag
import sk.kasper.ui_common.tag.TagMapperImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal class UiCommonModule {

    @Provides
    internal fun bindsSettingsManager(impl: SettingsManagerImpl): SettingsManager = impl

    @Provides
    @Singleton
    internal fun providesMapToUiTag(mapper: TagMapperImpl): MapToUiTag = mapper

    @Provides
    @Singleton
    internal fun providesMapToDomainTag(mapper: TagMapperImpl): MapToDomainTag = mapper

}
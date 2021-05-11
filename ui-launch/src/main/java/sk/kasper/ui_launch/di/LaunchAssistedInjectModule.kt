package sk.kasper.ui_launch.di

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module


@AssistedModule
@Module(includes = [AssistedInject_LaunchAssistedInjectModule::class])
abstract class LaunchAssistedInjectModule
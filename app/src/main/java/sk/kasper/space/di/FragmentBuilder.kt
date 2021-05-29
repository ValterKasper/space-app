package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.ui_common.about.LibrariesFragment
import sk.kasper.ui_launch.LaunchFragment
import sk.kasper.ui_playground.ComposePlaygroundFragment
import sk.kasper.ui_timeline.TimelineFragment


@Module
abstract class FragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun bindTimelineFragment(): TimelineFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun bindLaunchFragment(): LaunchFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun bindLibrariesFragment(): LibrariesFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun bindComposePlaygroundFragment(): ComposePlaygroundFragment


}
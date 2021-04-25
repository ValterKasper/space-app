package sk.kasper.space.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.kasper.space.about.LibrariesFragment
import sk.kasper.space.launchdetail.LaunchFragment
import sk.kasper.space.launchdetail.gallery.PhotoPagerFragment
import sk.kasper.space.playground.PlaygroundFragment
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
    internal abstract fun bindPlaygroundFragment(): PlaygroundFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun bindPhotoPagerFragment(): PhotoPagerFragment

}
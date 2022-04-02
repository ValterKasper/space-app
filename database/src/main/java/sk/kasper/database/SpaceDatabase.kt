package sk.kasper.database

import sk.kasper.database.dao.*

interface SpaceDatabase {
    fun launchDao(): LaunchDao
    fun launchSiteDao(): LaunchSiteDao
    fun rocketDao(): RocketDao
    fun tagDao(): TagDao
    fun photoDao(): PhotoDao
}
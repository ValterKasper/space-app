package sk.kasper.database

interface SpaceDatabase {
    fun carDao(): sk.kasper.database.dao.CarDao
}
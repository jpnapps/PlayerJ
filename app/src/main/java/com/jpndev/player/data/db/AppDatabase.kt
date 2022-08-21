package com.jpndev.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jpndev.player.data.model.*


@Database(entities = [QA::class,Article::class,PItem::class, PJUrl::class,MUpdateData::class],
version = 4,
exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

abstract fun articleDao(): ArticleDAO
    abstract fun appDao(): DAO

    abstract fun updateDAO(): UpdateDAO
}
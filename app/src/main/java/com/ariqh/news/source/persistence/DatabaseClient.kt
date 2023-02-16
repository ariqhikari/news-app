package com.ariqh.news.source.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ariqh.news.source.news.ArticleModel
import com.ariqh.news.source.news.NewsDao
import com.ariqh.news.util.SourceConverter

@Database(
    entities = [ArticleModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(SourceConverter::class)
abstract class DatabaseClient: RoomDatabase() {
    abstract val newsDao: NewsDao
}
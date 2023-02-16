package com.ariqh.news

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ariqh.news.source.network.networkModule
import com.ariqh.news.source.news.repositoryModule
import com.ariqh.news.source.persistence.databaseModule
import com.ariqh.news.ui.bookmark.bookmarkModule
import com.ariqh.news.ui.bookmark.bookmarkViewModel
import com.ariqh.news.ui.detail.detailModule
import com.ariqh.news.ui.detail.detailViewModel
import com.ariqh.news.ui.home.homeModule
import com.ariqh.news.ui.home.homeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.e("run base application")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger()
            androidContext(this@NewsApp)
            modules(
                networkModule,
                repositoryModule,
                homeViewModel,
                homeModule,
                bookmarkViewModel,
                bookmarkModule,
                databaseModule,
                detailViewModel,
                detailModule
            )
        }
    }
}
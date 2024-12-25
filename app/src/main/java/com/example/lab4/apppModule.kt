package com.example.lab4

//import androidx.room.Room
//import org.koin.androidx.viewmodel.dsl.viewModel
//import org.koin.dsl.module
//
//val appModule = module {
//    single { Room.databaseBuilder(get(), AppDatabase::class.java, "app_db").build() }
//    single { get<AppDatabase>().placeDao() }
//    single { get<AppDatabase>().noteDao() }
//    viewModel { AppViewModel(get()) }
//}
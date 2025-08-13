package com.amir.bagshop.di

import android.content.Context
import androidx.room.Room
import com.amir.bagshop.model.db.AppDatabase
import com.amir.bagshop.model.net.createApiService
import com.amir.bagshop.model.repository.cart.AddToCartRepository
import com.amir.bagshop.model.repository.cart.AddToCartRepositoryIMP
import com.amir.bagshop.model.repository.comments.CommentRepository
import com.amir.bagshop.model.repository.comments.CommentRepositoryIMP
import com.amir.bagshop.model.repository.product.ProductRepository
import com.amir.bagshop.model.repository.product.ProductRepositoryIMP
import com.amir.bagshop.model.repository.user.UserRepository
import com.amir.bagshop.model.repository.user.UserRepositoryIMP
import com.amir.bagshop.ui.features.Main.MainViewmodel
import com.amir.bagshop.ui.features.Sign_In.SignInViewModel
import com.amir.bagshop.ui.features.cart.CartScreenViewModel
import com.amir.bagshop.ui.features.category.CategoryViewModel
import com.amir.bagshop.ui.features.product.ProductViewModel
import com.amir.bagshop.ui.features.profile.ProfileViewModel
import com.amir.bagshop.ui.features.sign_Up.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val myModules = module {


    //SharedPreferences
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }

    //ApiService
    single { createApiService() }

    //DataBase
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build()
    }

    // Repositories
    single<ProductRepository> { ProductRepositoryIMP(get(), get<AppDatabase>().productDao()) }
    single<UserRepository> { UserRepositoryIMP(get(), get()) }
    single<CommentRepository> { CommentRepositoryIMP(get()) }
    single<AddToCartRepository> { AddToCartRepositoryIMP(get(),get()) }


    // ViewModels
    viewModel{ CartScreenViewModel (  get(),get() ) }
    viewModel { ProfileViewModel( get() ) }
    viewModel { ProductViewModel(get(), get() , get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isNetConnected: Boolean) -> MainViewmodel(get(), get(),isNetConnected) }
    viewModel { CategoryViewModel(get()) }
}
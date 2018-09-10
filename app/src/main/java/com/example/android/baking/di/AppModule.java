package com.example.android.baking.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.example.android.baking.ApiService;
import com.example.android.baking.AppExecutors;
import com.example.android.baking.AppRepository;
import com.example.android.baking.ExoPlayerVideoHandler;
import com.example.android.baking.database.AppDatabase;
import com.example.android.baking.database.RecipeDao;
import com.example.android.baking.ui.main.MainViewModelFactory;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    @Singleton
    @Provides
    AppRepository provideAppRepository(ApiService apiService, RecipeDao recipeDao, AppExecutors appExecutors) {
        return new AppRepository(apiService, recipeDao, appExecutors);
    }

    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, AppDatabase.DATABASE_NAME).build();
    }

    @Singleton
    @Provides
    RecipeDao provideRecipeDao(AppDatabase appDatabase) {
        return appDatabase.recipeDao();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    MainViewModelFactory provideMainViewModelFactory(AppRepository repository) {
        return new MainViewModelFactory(repository);
    }

    @Singleton
    @Provides
    ExoPlayerVideoHandler provideExoPlayerVideoHandler(){
        return new ExoPlayerVideoHandler();
    }
}

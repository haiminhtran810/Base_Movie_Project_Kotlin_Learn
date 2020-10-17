package learn.htm.projectlearn.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.room.Room
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import learn.htm.projectlearn.BuildConfig
import learn.htm.projectlearn.data.Constants
import learn.htm.projectlearn.data.local.dao.MovieDao
import learn.htm.projectlearn.data.local.dao.db.AppDatabase
import learn.htm.projectlearn.data.remote.api.CONNECTION_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.MovieAPI
import learn.htm.projectlearn.data.remote.api.READ_API_TIME_OUT
import learn.htm.projectlearn.data.remote.api.WRITE_API_TIME_OUT
import learn.htm.projectlearn.data.remote.factory.RxErrorHandlingFactory
import learn.htm.projectlearn.data.remote.interceptor.HeaderInterceptor
import learn.htm.projectlearn.data.remote.moshi.MoshiArrayListJsonAdapter
import learn.htm.projectlearn.data.remote.repository.MovieRepository
import learn.htm.projectlearn.data.remote.repository.movie.MovieRepositoryImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideLogInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        paramsInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(paramsInterceptor)
            .readTimeout(READ_API_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECTION_API_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_API_TIME_OUT, TimeUnit.SECONDS).build()

    @Singleton
    @Provides
    fun provideHeaderInterceptor(): Interceptor =
        HeaderInterceptor()

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(MoshiArrayListJsonAdapter.FACTORY)
            .build()

    @Singleton
    @Provides
    fun provideDefaultRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxErrorHandlingFactory())
            .build()

    @Singleton
    @Provides
    fun provideServerAPIService(retrofit: Retrofit): MovieAPI =
        retrofit.create(MovieAPI::class.java)

    @Singleton
    @Provides
    fun createSharedPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun createAppDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun createMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()

    @Singleton
    @Provides
    fun provideUserRepository(
        apiService: MovieAPI,
        movieDao: MovieDao
    ): MovieRepository = MovieRepositoryImpl(apiService, movieDao)

    @Singleton
    @Provides
    fun provideResources(context: Context): Resources = context.resources

}


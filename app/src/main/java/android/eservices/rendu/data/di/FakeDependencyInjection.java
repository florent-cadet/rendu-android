package android.eservices.rendu.data.di;

import android.content.Context;
import android.eservices.rendu.data.api.MovieDisplayService;
import android.eservices.rendu.data.db.MovieDatabase;
import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayDataRepository;
import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayRepository;
import android.eservices.rendu.data.repository.moviedisplay.local.MovieDisplayLocalDataSource;
import android.eservices.rendu.data.repository.moviedisplay.mapper.MovieDetailsResponseToMovieEntityMapper;
import android.eservices.rendu.data.repository.moviedisplay.remote.MovieDisplayRemoteDataSource;
import android.eservices.rendu.presentation.viewmodel.ViewModelFactory;

import androidx.room.Room;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FakeDependencyInjection {

    private static MovieDisplayService movieDisplayService;
    private static Retrofit retrofit;
    private static Gson gson;
    private static MovieDisplayRepository movieDisplayRepository;
    private static Context applicationContext;
    private static ViewModelFactory viewModelFactory;
    private static MovieDatabase movieDatabase;

    public static ViewModelFactory getViewModelFactory() {
        if (viewModelFactory == null) {
            viewModelFactory = new ViewModelFactory(getMovieDisplayRepository());
        }
        return viewModelFactory;
    }


    public static MovieDisplayRepository getMovieDisplayRepository() {
        if (movieDisplayRepository == null) {
            movieDisplayRepository = new MovieDisplayDataRepository(new MovieDisplayRemoteDataSource(getMovieDisplayService()), new MovieDisplayLocalDataSource(getMovieDatabase()), new MovieDetailsResponseToMovieEntityMapper());
        }
        return movieDisplayRepository;
    }

    public static MovieDisplayService getMovieDisplayService() {
        if (movieDisplayService == null) {
            movieDisplayService = getRetrofit().create(MovieDisplayService.class);
        }
        return movieDisplayService;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addNetworkInterceptor(new StethoInterceptor())
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(getGson()))
                    .build();
        }
        return retrofit;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static void setContext(Context context) {
        applicationContext = context;
    }

    public static MovieDatabase getMovieDatabase() {
        if (movieDatabase == null) {
            movieDatabase = Room.databaseBuilder(applicationContext,
                    MovieDatabase.class, "movie_database").build();
        }
        return movieDatabase;
    }
}

package android.eservices.rendu.data.repository.moviedisplay.local;

import android.eservices.rendu.data.db.MovieDatabase;
import android.eservices.rendu.data.entity.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MovieDisplayLocalDataSource {

    private MovieDatabase movieDatabase;

    public MovieDisplayLocalDataSource(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    public Flowable<List<MovieEntity>> loadWatched() {
        return movieDatabase.movieDao().loadWatched();
    }

    public Completable addMovieToWatched(MovieEntity movieEntity) {
        return movieDatabase.movieDao().addMovieToWatched(movieEntity);
    }

    public Completable deleteMovieFromWatched(int id) {
        return movieDatabase.movieDao().deleteMovieFromWatched(id);
    }

    public Single<List<Integer>> getWatchedIdList() {
        return movieDatabase.movieDao().getWatchedIdList();
    }
}

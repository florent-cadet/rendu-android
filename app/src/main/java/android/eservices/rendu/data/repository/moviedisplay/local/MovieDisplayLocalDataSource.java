package android.eservices.rendu.data.repository.moviedisplay.local;

import android.eservices.rendu.data.db.MovieDatabase;
import android.eservices.rendu.data.entity.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class MovieDisplayLocalDataSource {

    private MovieDatabase movieDatabase;

    public MovieDisplayLocalDataSource(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    /**
     * Get the movies watched from the dao
     * @return the movies watched from the dao
     */
    public Flowable<List<MovieEntity>> loadWatched() {
        return movieDatabase.movieDao().loadWatched();
    }

    /**
     * Call the dao method to add a new movie
     * @param movieEntity the movie to add
     * @return the Completable from the dao
     */
    public Completable addMovieToWatched(MovieEntity movieEntity) {
        return movieDatabase.movieDao().addMovieToWatched(movieEntity);
    }

    /**
     * Call the dao method to delete a movie
     * @param id the id of the movie to delete
     * @return the Completable from the dao
     */
    public Completable deleteMovieFromWatched(int id) {
        return movieDatabase.movieDao().deleteMovieFromWatched(id);
    }

    /**
     * Get the movies ids list from the dao
     * @return the movies ids list
     */
    public Single<List<Integer>> getWatchedIdList() {
        return movieDatabase.movieDao().getWatchedIdList();
    }

    /**
     * Get a movie from the dao
     * @param id the id of the movie
     * @return a Single with a movie entity from the database if it exists or an empty movie entity
     */
    public Single<MovieEntity> getById(int id) {
        return movieDatabase.movieDao().getById(id).defaultIfEmpty(new MovieEntity()).toSingle();
    }
}

package android.eservices.rendu.data.db;

import android.eservices.rendu.data.entity.MovieEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MovieDao {

    /**
     * Select all the movies watched from database
     * @return all the movies watched
     */
    @Query("SELECT * from movieentity")
    Flowable<List<MovieEntity>> loadWatched();

    /**
     * Insert a new movie in the database
     * @param movieEntity the movie to insert
     * @return a Completable to notify the change
     */
    @Insert
    public Completable addMovieToWatched(MovieEntity movieEntity);

    /**
     * Delete a movie from the database
     * @param id the id of the movie to delete
     * @return a Completable to notify the change
     */
    @Query("DELETE FROM movieentity WHERE id = :id")
    public Completable deleteMovieFromWatched(int id);

    /**
     * Get all the ids of the movies in the database
     * @return The list of the movies ids in the database
     */
    @Query("SELECT id from movieentity")
    Single<List<Integer>> getWatchedIdList();


    /**
     * Get a specific movie from the databse
     * @param id the id of the movie
     * @return a Maybe of the movie entity
     */
    @Query("SELECT * FROM movieentity WHERE id = :id")
    public Maybe<MovieEntity> getById(int id);
}

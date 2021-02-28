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

    @Query("SELECT * from movieentity")
    Flowable<List<MovieEntity>> loadWatched();

    @Insert
    public Completable addMovieToWatched(MovieEntity movieEntity);

    @Query("DELETE FROM movieentity WHERE id = :id")
    public Completable deleteMovieFromWatched(int id);

    @Query("SELECT id from movieentity")
    Single<List<Integer>> getWatchedIdList();


    @Query("SELECT * FROM movieentity WHERE id = :id")
    public Maybe<MovieEntity> getById(int id);
}

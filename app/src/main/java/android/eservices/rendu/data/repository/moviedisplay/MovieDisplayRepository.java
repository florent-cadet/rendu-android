package android.eservices.rendu.data.repository.moviedisplay;

import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;
import android.eservices.rendu.data.entity.MovieEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface MovieDisplayRepository {

    Single<MovieSearchResponse> getPopularMovies();

    Single<MovieSearchResponse> getMovieSearchResponse(String query);

    Completable addMovieToWatched(int movieId);

    Completable removeMovieFromWatched(int movieId);

    Flowable<List<MovieEntity>> getWatchedMovies();

    Single<MovieDetailsResponse> getMovieById(int movieId);
}

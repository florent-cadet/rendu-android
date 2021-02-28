package android.eservices.rendu.data.repository.moviedisplay;

import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;
import android.eservices.rendu.data.entity.MovieEntity;
import android.eservices.rendu.data.repository.moviedisplay.local.MovieDisplayLocalDataSource;
import android.eservices.rendu.data.repository.moviedisplay.mapper.MovieDetailsResponseToMovieEntityMapper;
import android.eservices.rendu.data.repository.moviedisplay.remote.MovieDisplayRemoteDataSource;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class MovieDisplayDataRepository implements MovieDisplayRepository {

    private MovieDisplayRemoteDataSource movieDisplayRemoteDataSource;
    private MovieDisplayLocalDataSource movieDisplayLocalDataSource;
    private MovieDetailsResponseToMovieEntityMapper movieDetailsResponseToMovieEntityMapper;

    public MovieDisplayDataRepository(MovieDisplayRemoteDataSource movieDisplayRemoteDataSource, MovieDisplayLocalDataSource movieDisplayLocalDataSource, MovieDetailsResponseToMovieEntityMapper movieDetailsResponseToMovieEntityMapper) {
        this.movieDisplayRemoteDataSource = movieDisplayRemoteDataSource;
        this.movieDisplayLocalDataSource = movieDisplayLocalDataSource;
        this.movieDetailsResponseToMovieEntityMapper = movieDetailsResponseToMovieEntityMapper;
    }


    @Override
    public Single<MovieSearchResponse> getPopularMovies() {
        return movieDisplayRemoteDataSource.getPopularMoviesResponse()
                .zipWith(movieDisplayLocalDataSource.getWatchedIdList(), new BiFunction<MovieSearchResponse, List<Integer>, MovieSearchResponse>() {
            @Override
            public MovieSearchResponse apply(MovieSearchResponse movieSearchResponse, List<Integer> idList) throws Exception {
                for (Movie movie : movieSearchResponse.getResults()) {
                    if (idList.contains(movie.getId())) {
                        //String non null - la date n'est pas a affichée
                        movie.setSeenDate("watched");
                    }
                }
                return movieSearchResponse;
            }
        });
    }

    @Override
    public Single<MovieSearchResponse> getMovieSearchResponse(String query) {
        return movieDisplayRemoteDataSource.getMovieSearchResponse(query).zipWith(movieDisplayLocalDataSource.getWatchedIdList(), new BiFunction<MovieSearchResponse, List<Integer>, MovieSearchResponse>() {
            @Override
            public MovieSearchResponse apply(MovieSearchResponse movieSearchResponse, List<Integer> idList) throws Exception {
                for (Movie movie : movieSearchResponse.getResults()) {
                    if (idList.contains(movie.getId())) {
                        //String non null - la date n'est pas a affichée
                        movie.setSeenDate("watched");
                    }
                }
                return movieSearchResponse;
            }
        });
    }

    @Override
    public Completable addMovieToWatched(int movieId) {
        return movieDisplayRemoteDataSource.getMovieDetails(movieId).map(new Function<MovieDetailsResponse, MovieEntity>() {
            @Override
            public MovieEntity apply(MovieDetailsResponse movie) throws Exception {
                return movieDetailsResponseToMovieEntityMapper.map(movie);
            }
        }).flatMapCompletable(new Function<MovieEntity, CompletableSource>() {
            @Override
            public CompletableSource apply(MovieEntity movieEntity) throws Exception {
                return movieDisplayLocalDataSource.addMovieToWatched(movieEntity);
            }
        });
    }

    @Override
    public Completable removeMovieFromWatched(int movieId) {
        return movieDisplayLocalDataSource.deleteMovieFromWatched(movieId);
    }

    @Override
    public Flowable<List<MovieEntity>> getWatchedMovies() {
        return movieDisplayLocalDataSource.loadWatched();
    }


}

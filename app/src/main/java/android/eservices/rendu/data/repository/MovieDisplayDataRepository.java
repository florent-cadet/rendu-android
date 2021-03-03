package android.eservices.rendu.data.repository;

import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;
import android.eservices.rendu.data.entity.MovieEntity;
import android.eservices.rendu.data.repository.local.MovieDisplayLocalDataSource;
import android.eservices.rendu.data.repository.mapper.MovieDetailsResponseToMovieEntityMapper;
import android.eservices.rendu.data.repository.remote.MovieDisplayRemoteDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Single;
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

    /**
     * Get the popular movies and set the seenDate of the movies to non empty string if the id of the movie is in the database
     * @return the popular movies
     */
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

    /**
     * Get the searched movie and set the seenDate of the movies to non empty string if the id of the movie is in the database
     * @param query the search
     * @return the searched movies
     */
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

    /**
     * Get the details of the movie, map the response to a movie entity and save it in the databse
     * @param movieId the id of the movie
     * @return a Completable
     */
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

    /**
     * Remove a movie from the databse
     * @param movieId the id of the movie to remove
     * @return a Completable
     */
    @Override
    public Completable removeMovieFromWatched(int movieId) {
        return movieDisplayLocalDataSource.deleteMovieFromWatched(movieId);
    }

    /**
     * Get the movies watched from the databse
     * @return the movies watched
     */
    @Override
    public Flowable<List<MovieEntity>> getWatchedMovies() {
        return movieDisplayLocalDataSource.loadWatched();
    }

    /**
     * Get the details of a movie from the api and set it's seen date to the value in the database if the movie has been watched
     * @param movieId the id of the movie
     * @return the details of the movie
     */
    @Override
    public Single<MovieDetailsResponse> getMovieById(int movieId) {
        return movieDisplayRemoteDataSource.getMovieDetails(movieId).zipWith(movieDisplayLocalDataSource.getById(movieId), new BiFunction<MovieDetailsResponse, MovieEntity, MovieDetailsResponse>() {

            @Override
            public MovieDetailsResponse apply(MovieDetailsResponse movieDetailsResponse, MovieEntity movieEntity) throws Exception {
                movieDetailsResponse.setSeenDate(movieEntity.getSeenDate());
                return movieDetailsResponse;
            }
        });
    }


}

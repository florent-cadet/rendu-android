package android.eservices.rendu.data.repository.remote;

import android.eservices.rendu.MovieApplication;
import android.eservices.rendu.data.api.MovieDisplayService;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;

import io.reactivex.Single;

public class MovieDisplayRemoteDataSource {

    private MovieDisplayService movieDisplayService;

    public MovieDisplayRemoteDataSource(MovieDisplayService movieDisplayService) {
        this.movieDisplayService = movieDisplayService;
    }

    /**
     * Get the popular movies from the api
     * @return the popular movies
     */
    public Single<MovieSearchResponse> getPopularMoviesResponse() {
        return movieDisplayService.getPopularMovies(MovieApplication.API_KEY, MovieApplication.LANGUAGE);
    }

    /**
     * Get the movies corresponding to the search from the api
     * @param query the search
     * @return the movies corresponding to the search
     */
    public Single<MovieSearchResponse> getMovieSearchResponse(String query) {
        return movieDisplayService.searchMovies(MovieApplication.API_KEY, query, MovieApplication.LANGUAGE);
    }

    /**
     * Get the details of a movie from the api
     * @param movieId the id of the movie
     * @return the details of the movie
     */
    public Single<MovieDetailsResponse> getMovieDetails(int movieId) {
        return movieDisplayService.getMovieDetails(movieId, MovieApplication.API_KEY, MovieApplication.LANGUAGE);
    }
}

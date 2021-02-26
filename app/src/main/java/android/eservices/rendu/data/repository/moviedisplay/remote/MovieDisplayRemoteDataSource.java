package android.eservices.rendu.data.repository.moviedisplay.remote;

import android.eservices.rendu.MovieApplication;
import android.eservices.rendu.data.api.MovieDisplayService;
import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;

import io.reactivex.Single;

public class MovieDisplayRemoteDataSource {

    private MovieDisplayService movieDisplayService;

    public MovieDisplayRemoteDataSource(MovieDisplayService movieDisplayService) {
        this.movieDisplayService = movieDisplayService;
    }

    public Single<MovieSearchResponse> getPopularMoviesResponse() {
        return movieDisplayService.getPopularMovies(MovieApplication.API_KEY, MovieApplication.LANGUAGE);
    }

    public Single<MovieSearchResponse> getMovieSearchResponse(String query) {
        return movieDisplayService.searchMovies(MovieApplication.API_KEY, query, MovieApplication.LANGUAGE);
    }

    public Single<MovieDetailsResponse> getMovieDetails(int movieId) {
        return movieDisplayService.getMovieDetails(movieId, MovieApplication.API_KEY, MovieApplication.LANGUAGE);
    }
}

package android.eservices.rendu.data.api;

import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDisplayService {
    /**
     * Get all the popular movies of the moment from the api
     * @param apiKey the api key
     * @param language the language
     * @return An entity with the list of movies, the number of pages and the total elements
     */
    @GET("movie/popular")
    Single<MovieSearchResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);

    /**
     * Search a movie from the api
     * @param apiKey the api key
     * @param query the search phrase
     * @param language the language
     * @return An entity with the list of movies, the number of pages and the total elements corresponding to the search
     */
    @GET("search/movie")
    Single<MovieSearchResponse> searchMovies(@Query("api_key") String apiKey, @Query("query") String query, @Query("language") String language);

    /**
     * Get the details of a movie from the api
     * @param movieId the id of the movie
     * @param apiKey the api key
     * @param language the language
     * @return An entity with the details of the movie
     */
    @GET("movie/{movieId}")
    Single<MovieDetailsResponse> getMovieDetails(@Path("movieId") int movieId, @Query("api_key") String apiKey, @Query("language") String language);

}

package android.eservices.rendu.data.api;

import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.api.model.MovieSearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDisplayService {
    @GET("movie/popular")
    Single<MovieSearchResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("language") String language);


    @GET("search/movie")
    Single<MovieSearchResponse> searchMovies(@Query("api_key") String apiKey, @Query("query") String query, @Query("language") String language);

    @GET("movie/{movieId}")
    Single<MovieDetailsResponse> getMovieDetails(@Path("movieId") int movieId, @Query("api_key") String apiKey, @Query("language") String language);

}

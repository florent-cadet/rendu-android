package android.eservices.rendu.data.repository.moviedisplay;

import android.eservices.rendu.data.api.model.MovieSearchResponse;
import android.eservices.rendu.data.repository.moviedisplay.remote.MovieDisplayRemoteDataSource;

import io.reactivex.Single;

public class MovieDisplayDataRepository implements MovieDisplayRepository {

    private MovieDisplayRemoteDataSource movieDisplayRemoteDataSource;

    public MovieDisplayDataRepository(MovieDisplayRemoteDataSource movieDisplayRemoteDataSource) {
        this.movieDisplayRemoteDataSource = movieDisplayRemoteDataSource;
    }


    @Override
    public Single<MovieSearchResponse> getPopularMovies() {
        return movieDisplayRemoteDataSource.getPopularMoviesResponse();
    }

    @Override
    public Single<MovieSearchResponse> getMovieSearchResponse(String query) {
        return movieDisplayRemoteDataSource.getMovieSearchResponse(query);
    }


}

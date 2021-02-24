package android.eservices.rendu.data.repository.moviedisplay;

import android.eservices.rendu.data.api.model.MovieSearchResponse;
import io.reactivex.Single;

public interface MovieDisplayRepository {

    Single<MovieSearchResponse> getPopularMovies();

    Single<MovieSearchResponse> getMovieSearchResponse(String query);
}

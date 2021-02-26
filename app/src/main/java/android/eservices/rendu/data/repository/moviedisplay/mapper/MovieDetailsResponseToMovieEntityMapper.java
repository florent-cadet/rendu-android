package android.eservices.rendu.data.repository.moviedisplay.mapper;

import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.entity.MovieEntity;

public class MovieDetailsResponseToMovieEntityMapper {

    public MovieEntity map(MovieDetailsResponse movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(movie.getId());
        movieEntity.setOverview(movie.getOverview());
        movieEntity.setTitle(movie.getTitle());
        return movieEntity;
    }
}

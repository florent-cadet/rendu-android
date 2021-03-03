package android.eservices.rendu.data.repository.mapper;

import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.entity.MovieEntity;
import android.text.format.DateFormat;

import java.util.Date;

public class MovieDetailsResponseToMovieEntityMapper {

    /**
     * Map a MovieDetailsResponse to a MovieEntity
     * @param movie the MovieDetailsResponse to map
     * @return the mapped MovieDetailsResponse
     */
    public MovieEntity map(MovieDetailsResponse movie) {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(movie.getId());
        movieEntity.setOverview(movie.getOverview());
        movieEntity.setTitle(movie.getTitle());
        movieEntity.setPosterPath(movie.getPosterPath());
        movieEntity.setSeenDate(DateFormat.format("dd-MM-yyyy", new Date()).toString());
        return movieEntity;
    }
}

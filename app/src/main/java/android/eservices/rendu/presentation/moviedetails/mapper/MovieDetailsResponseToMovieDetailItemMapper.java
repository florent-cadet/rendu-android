package android.eservices.rendu.presentation.moviedetails.mapper;

import android.eservices.rendu.data.api.model.Genre;
import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.presentation.moviedetails.model.MovieDetailItemViewModel;

public class MovieDetailsResponseToMovieDetailItemMapper {

    public MovieDetailItemViewModel map(MovieDetailsResponse movieDetailsResponse) {
        MovieDetailItemViewModel movieDetailItemViewModel = new MovieDetailItemViewModel();
        movieDetailItemViewModel.setTitle(movieDetailsResponse.getTitle());
        movieDetailItemViewModel.setOverview(movieDetailsResponse.getOverview());
        if (movieDetailsResponse.getPosterPath() != null) {
            movieDetailItemViewModel.setPosterPath("https://image.tmdb.org/t/p/w500/" + movieDetailsResponse.getPosterPath());
        }
        if(movieDetailsResponse.getGenres().size() > 0) {
            movieDetailItemViewModel.setGenres(movieDetailsResponse.getGenres().get(0).getName());
            for (int i = 1; i < movieDetailsResponse.getGenres().size(); ++i) {
                movieDetailItemViewModel.setGenres(movieDetailItemViewModel.getGenres() + ", " + movieDetailsResponse.getGenres().get(i).getName());
            }
        }
        movieDetailItemViewModel.setReleaseDate(movieDetailsResponse.getReleaseDate());
        movieDetailItemViewModel.setSeenDate(movieDetailsResponse.getSeenDate());
        return movieDetailItemViewModel;
    }

}

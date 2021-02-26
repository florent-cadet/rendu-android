package android.eservices.rendu.presentation.moviedisplay.search.mapper;

import android.eservices.rendu.data.api.model.Movie;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MovieToViewModelMapper {

    public List<MovieItemViewModel> map(List<Movie> movieList) {
        List<MovieItemViewModel> movieItemViewModelList = new ArrayList<>();
        for (Movie movie: movieList) {
            MovieItemViewModel movieItemViewModel = new MovieItemViewModel();
            movieItemViewModel.setId(movie.getId());
            movieItemViewModel.setTitle(movie.getTitle());
            movieItemViewModel.setOverview(movie.getOverview());
            movieItemViewModel.setWatched(movie.isWatched());
            if (movie.getPoster_path() != null) {
                movieItemViewModel.setPosterUrl("https://image.tmdb.org/t/p/w500/" + movie.getPoster_path());
            }
            movieItemViewModelList.add(movieItemViewModel);
        }
        return movieItemViewModelList;
    }
}

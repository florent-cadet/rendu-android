package android.eservices.rendu.presentation.moviedisplay.seen.mapper;

import android.eservices.rendu.data.entity.MovieEntity;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class MovieEntityToWatchedViewModelMapper {

    public List<MovieItemViewModel> map(List<MovieEntity> movieList) {
        List<MovieItemViewModel> movieItemViewModelList = new ArrayList<>();
        for (MovieEntity movie: movieList) {
            MovieItemViewModel movieItemViewModel = new MovieItemViewModel();
            movieItemViewModel.setId(movie.getId());
            movieItemViewModel.setTitle(movie.getTitle());
            movieItemViewModel.setOverview(movie.getOverview());
            movieItemViewModel.setSeenDate(movie.getSeenDate());
            if (movie.getPosterPath() != null) {
                movieItemViewModel.setPosterUrl("https://image.tmdb.org/t/p/w500/" + movie.getPosterPath());
            }
            movieItemViewModelList.add(movieItemViewModel);
        }
        return movieItemViewModelList;
    }
}

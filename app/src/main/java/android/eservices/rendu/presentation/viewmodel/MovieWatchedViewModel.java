package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayRepository;
import android.eservices.rendu.presentation.moviedisplay.search.mapper.MovieToViewModelMapper;

import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;

public class MovieWatchedViewModel extends ViewModel {

    private MovieDisplayRepository movieDisplayRepository;
    private CompositeDisposable compositeDisposable;
    private MovieToViewModelMapper movieToViewModelMapper;

    public MovieWatchedViewModel(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.movieToViewModelMapper = new MovieToViewModelMapper();
    }

}

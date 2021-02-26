package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieDisplayRepository movieDisplayRepository;

    public ViewModelFactory(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieSearchViewModel.class)) {
            return (T) new MovieSearchViewModel(movieDisplayRepository);
        }
        if(modelClass.isAssignableFrom(MovieWatchedViewModel.class)) {
            return (T) new MovieWatchedViewModel(movieDisplayRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
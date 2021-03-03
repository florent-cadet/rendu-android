package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.repository.MovieDisplayRepository;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MovieDisplayRepository movieDisplayRepository;

    public ViewModelFactory(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
    }

    /**
     * Override the create method of the ViewModelFactory
     * @param modelClass the class of the model
     * @param <T> the type of the class
     * @return a the ViewModelCorresponding ti the class
     */
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieSearchViewModel.class)) {
            return (T) new MovieSearchViewModel(movieDisplayRepository);
        }
        if(modelClass.isAssignableFrom(MovieWatchedViewModel.class)) {
            return (T) new MovieWatchedViewModel(movieDisplayRepository);
        }
        if(modelClass.isAssignableFrom(MovieDetailsViewModel.class)) {
            return (T) new MovieDetailsViewModel(movieDisplayRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

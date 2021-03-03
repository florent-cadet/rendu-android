package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.entity.MovieEntity;
import android.eservices.rendu.data.repository.MovieDisplayRepository;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.eservices.rendu.presentation.moviedisplay.seen.mapper.MovieEntityToWatchedViewModelMapper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class MovieWatchedViewModel extends ViewModel {

    private MovieDisplayRepository movieDisplayRepository;
    private CompositeDisposable compositeDisposable;
    private MovieEntityToWatchedViewModelMapper movieEntityToWatchedViewModelMapper;

    public MovieWatchedViewModel(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.movieEntityToWatchedViewModelMapper = new MovieEntityToWatchedViewModelMapper();
    }


    private MutableLiveData<List<MovieItemViewModel>> movies;
    private MutableLiveData<Boolean> isDataLoading = new MutableLiveData<Boolean>();

    public MutableLiveData<List<MovieItemViewModel>> getMovies() { return movies; }
    public MutableLiveData<Boolean> getIsDataLoading() { return isDataLoading; }

    private MutableLiveData<Event<Integer>> movieDeletedEvent = new MutableLiveData<Event<Integer>>();

    /**
     * Get the watched movie from the database
     * @return the watched movies
     */
    public MutableLiveData<List<MovieItemViewModel>> getWatchedMovies() {
        isDataLoading.setValue(true);
        if (movies == null) {
            movies = new MutableLiveData<List<MovieItemViewModel>>();
            compositeDisposable.add(movieDisplayRepository.getWatchedMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSubscriber<List<MovieEntity>>() {

                        @Override
                        public void onNext(List<MovieEntity> movieEntityList) {
                            isDataLoading.setValue(false);
                            movies.setValue(movieEntityToWatchedViewModelMapper.map(movieEntityList));
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            isDataLoading.setValue(false);
                        }

                        @Override
                        public void onComplete() {
                            isDataLoading.setValue(false);
                        }
                    }));

        }
        return movies;
    }

    /**
     * Remove a movie from the database
     * @param movieId the id of the movie to remove
     */
    public void removeMovieFromWatched(final int movieId) {
        compositeDisposable.add(movieDisplayRepository.removeMovieFromWatched(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        movieDeletedEvent.setValue(new Event<Integer>(movieId));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

}

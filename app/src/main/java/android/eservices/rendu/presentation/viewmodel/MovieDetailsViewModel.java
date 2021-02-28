package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.api.model.MovieDetailsResponse;
import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayRepository;
import android.eservices.rendu.presentation.moviedetails.model.MovieDetailItemViewModel;
import android.eservices.rendu.presentation.moviedetails.mapper.MovieDetailsResponseToMovieDetailItemMapper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsViewModel extends ViewModel {

    private MovieDisplayRepository movieDisplayRepository;
    private CompositeDisposable compositeDisposable;
    private MovieDetailsResponseToMovieDetailItemMapper movieDetailsResponseToMovieDetailItemMapper;

    public MovieDetailsViewModel(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.movieDetailsResponseToMovieDetailItemMapper = new MovieDetailsResponseToMovieDetailItemMapper();
    }

    private MutableLiveData<MovieDetailItemViewModel> movie = new MutableLiveData<MovieDetailItemViewModel>();

    private MutableLiveData<Event<Boolean>> movieAddedEvent = new MutableLiveData<Event<Boolean>>();
    private MutableLiveData<Event<Boolean>> movieDeletedEvent = new MutableLiveData<Event<Boolean>>();

    public MutableLiveData<Event<Boolean>> getMovieAddedEvent() {
        return movieAddedEvent;
    }

    public MutableLiveData<Event<Boolean>> getMovieDeletedEvent() {
        return movieDeletedEvent;
    }

    public MutableLiveData<MovieDetailItemViewModel> getMovieDetails(int movieId) {
        compositeDisposable.clear();
        compositeDisposable.add(movieDisplayRepository.getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieDetailsResponse>() {

                    @Override
                    public void onSuccess(@NonNull MovieDetailsResponse movieDetailsResponse) {
                        movie.setValue(movieDetailsResponseToMovieDetailItemMapper.map(movieDetailsResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                }));
        return movie;
    }

    public void addMovieToWatched(final int movieId) {
        movieAddedEvent.setValue(new Event<Boolean>(false));
        compositeDisposable.add(movieDisplayRepository.addMovieToWatched(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        movieAddedEvent.setValue(new Event<Boolean>(true));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    public void removeMovieFromWatched(final int movieId) {
        movieAddedEvent.setValue(new Event<Boolean>(false));
        compositeDisposable.add(movieDisplayRepository.removeMovieFromWatched(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        movieDeletedEvent.setValue(new Event<Boolean>(true));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

}

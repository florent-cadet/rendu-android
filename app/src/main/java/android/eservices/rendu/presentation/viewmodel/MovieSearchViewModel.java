package android.eservices.rendu.presentation.viewmodel;

import android.eservices.rendu.data.api.model.MovieSearchResponse;
import android.eservices.rendu.data.repository.moviedisplay.MovieDisplayRepository;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.eservices.rendu.presentation.moviedisplay.search.mapper.MovieToViewModelMapper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MovieSearchViewModel extends ViewModel {
    private MovieDisplayRepository movieDisplayRepository;
    private CompositeDisposable compositeDisposable;
    private MovieToViewModelMapper movieToViewModelMapper;

    public MovieSearchViewModel(MovieDisplayRepository movieDisplayRepository) {
        this.movieDisplayRepository = movieDisplayRepository;
        this.compositeDisposable = new CompositeDisposable();
        this.movieToViewModelMapper = new MovieToViewModelMapper();
    }

    private MutableLiveData<List<MovieItemViewModel>> movies = new MutableLiveData<List<MovieItemViewModel>>();
    private MutableLiveData<Boolean> isDataLoading = new MutableLiveData<Boolean>();

    public MutableLiveData<List<MovieItemViewModel>> getMovies() {
        return movies;
    }

    public MutableLiveData<Boolean> getIsDataLoading() {
        return isDataLoading;
    }
    final MutableLiveData<Event<Integer>> movieAddedEvent = new MutableLiveData<Event<Integer>>();
    final MutableLiveData<Event<Integer>> movieDeletedEvent = new MutableLiveData<Event<Integer>>();

    public void searchMovies(String query) {
        isDataLoading.postValue(true);
        compositeDisposable.clear();
        compositeDisposable.add(movieDisplayRepository.getMovieSearchResponse(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieSearchResponse>() {

                    @Override
                    public void onSuccess(MovieSearchResponse movieSearchResponse) {
                        movies.setValue(movieToViewModelMapper.map(movieSearchResponse.getResults()));
                        isDataLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        isDataLoading.setValue(false);
                    }
                }));
    }

    public void getPopularMovies() {
        isDataLoading.postValue(true);
        compositeDisposable.clear();
        compositeDisposable.add(movieDisplayRepository.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieSearchResponse>() {

                    @Override
                    public void onSuccess(@NonNull MovieSearchResponse movieSearchResponse) {
                        movies.setValue(movieToViewModelMapper.map(movieSearchResponse.getResults()));
                        isDataLoading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        isDataLoading.setValue(false);
                    }
                }));
    }

    public void addMovieToWatched(final int movieId) {
        compositeDisposable.add(movieDisplayRepository.addMovieToWatched(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        movieAddedEvent.setValue(new Event<Integer>(movieId));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

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
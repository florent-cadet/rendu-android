package android.eservices.rendu.presentation.moviedisplay.search.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.eservices.rendu.R;
import android.eservices.rendu.data.di.FakeDependencyInjection;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieActionInterface;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieAdapter;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.eservices.rendu.presentation.viewmodel.MovieSearchViewModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.eservices.rendu.MovieApplication.displayChangeAction;


public class SearchFragment extends Fragment implements MovieActionInterface {

    public static final String TAB_NAME = "Rechercher";
    private View rootView;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private MovieSearchViewModel movieSearchViewModel;

    BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupLayoutOnRecyclerView((int) intent.getExtras().get(displayChangeAction));
        }
    };

    private SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        IntentFilter filter = new IntentFilter(displayChangeAction);
        getActivity().registerReceiver(receiverUpdateDownload, filter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchView();
        setupAdapterOnRecyclerView();
        setupLayoutOnRecyclerView(1);
        progressBar = rootView.findViewById(R.id.progress_bar);

        registerViewModels();
    }

    private void registerViewModels() {
        movieSearchViewModel = new ViewModelProvider(requireActivity(), FakeDependencyInjection.getViewModelFactory()).get(MovieSearchViewModel.class);

        movieSearchViewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieItemViewModel>>() {
            @Override
            public void onChanged(List<MovieItemViewModel> movieItemViewModelList) {
                movieAdapter.bindViewModels(movieItemViewModelList);
            }
        });

        movieSearchViewModel.getIsDataLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDataLoading) {
                progressBar.setVisibility(isDataLoading ? View.VISIBLE : View.GONE);
            }
        });

        movieSearchViewModel.getPopularMovies();
    }

    private void setupSearchView() {
        searchView = rootView.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private Timer timer = new Timer();

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                if (s.length() == 0) {
                    movieSearchViewModel.getPopularMovies();
                } else {
                    movieSearchViewModel.searchMovies(s);
                }
                return true;
            }
        });
    }

    private void setupAdapterOnRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
    }

    private void setupLayoutOnRecyclerView(int layout) {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if(layout == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (receiverUpdateDownload != null) {
            try {
                getActivity().unregisterReceiver(receiverUpdateDownload);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onWatchedButtonClicked(int movieId, boolean watched) {
        if(watched) {
            movieSearchViewModel.addMovieToWatched(movieId);
        } else {
            movieSearchViewModel.removeMovieFromWatched(movieId);
        }
    }
}

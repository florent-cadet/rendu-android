package android.eservices.rendu.presentation.moviedisplay.search.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.eservices.rendu.R;
import android.eservices.rendu.data.di.FakeDependencyInjection;
import android.eservices.rendu.presentation.moviedetails.MovieDetailsActivity;
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

    /**
     * Override the onCreateView to prepare the fragment
     * @param inflater the layoutInflater
     * @param container the container
     * @param savedInstanceState the savedInstanceState
     * @return the view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        return rootView;
    }

    /**
     * Override the onActivityCreated to prepare the fragment
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSearchView();
        setupAdapterOnRecyclerView();
        setupLayoutOnRecyclerView(1);
        progressBar = rootView.findViewById(R.id.progress_bar);
        registerViewModel();
    }

    /**
     * Override the onStart to prepare a broadcastReceiver if the fragment is resumed
     */
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(displayChangeAction);
        getActivity().registerReceiver(receiverUpdateDownload, filter);
    }

    /**
     * Register the view model and prepare the onChanged
     */
    private void registerViewModel() {
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

    /**
     * Set up the search view to handle the search
     */
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

    /**
     * Set up the adapter on the recyclerview
     */
    private void setupAdapterOnRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        movieAdapter = new MovieAdapter(this);
        recyclerView.setAdapter(movieAdapter);
    }

    /**
     * Set up the layout of the recyclerview
     * @param layout 1 to display a list, 2 to display a grid
     */
    private void setupLayoutOnRecyclerView(int layout) {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        if(layout == 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }
    }

    /**
     * Override the onStop to unregister the broadcast receiver if the fragment is stopped
     */
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

    /**
     * Handle the click on the watched button
     * @param movieId the id of the movie clicked
     * @param watched if the movie was watched or not before the click
     */
    @Override
    public void onWatchedButtonClicked(int movieId, boolean watched) {
        if(watched) {
            movieSearchViewModel.addMovieToWatched(movieId);
        } else {
            movieSearchViewModel.removeMovieFromWatched(movieId);
        }
    }

    /**
     * Start new activity to get the details of the movie clicked
     * @param movieId the id of the movie clicked
     */
    @Override
    public void onItemClicked(int movieId) {
        Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }
}

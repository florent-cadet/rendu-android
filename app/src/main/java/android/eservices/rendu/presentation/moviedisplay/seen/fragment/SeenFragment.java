package android.eservices.rendu.presentation.moviedisplay.seen.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.eservices.rendu.R;
import android.eservices.rendu.data.di.FakeDependencyInjection;
import android.eservices.rendu.presentation.moviedetails.MovieDetailsActivity;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.eservices.rendu.presentation.moviedisplay.seen.adapter.MovieWatchedActionInterface;
import android.eservices.rendu.presentation.moviedisplay.seen.adapter.MovieWatchedAdapter;
import android.eservices.rendu.presentation.viewmodel.MovieWatchedViewModel;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.eservices.rendu.MovieApplication.displayChangeAction;

public class SeenFragment extends Fragment implements MovieWatchedActionInterface {

    public static final String TAB_NAME = "Films vus";
    private View rootView;
    private RecyclerView recyclerView;
    private MovieWatchedAdapter movieWatchedAdapter;
    private ProgressBar progressBar;
    private MovieWatchedViewModel movieWatchedViewModel;

    BroadcastReceiver receiverUpdateDownload = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupLayoutOnRecyclerView((int) intent.getExtras().get(displayChangeAction));
        }
    };

    private SeenFragment() {
    }

    public static SeenFragment newInstance() {
        return new SeenFragment();
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
        rootView = inflater.inflate(R.layout.fragment_seen, container, false);
        return rootView;
    }

    /**
     * Override the onActivityCreated to prepare the fragment
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        movieWatchedViewModel = new ViewModelProvider(requireActivity(), FakeDependencyInjection.getViewModelFactory()).get(MovieWatchedViewModel.class);

        movieWatchedViewModel.getWatchedMovies().observe(getViewLifecycleOwner(), new Observer<List<MovieItemViewModel>>() {
            @Override
            public void onChanged(List<MovieItemViewModel> movieItemViewModelList) {
                movieWatchedAdapter.bindViewModels(movieItemViewModelList);
            }
        });

        movieWatchedViewModel.getIsDataLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isDataLoading) {
                progressBar.setVisibility(isDataLoading ? View.VISIBLE : View.GONE);
            }
        });

        movieWatchedViewModel.getWatchedMovies();
    }

    /**
     * Set up the adapter on the recyclerview
     */
    private void setupAdapterOnRecyclerView() {
        recyclerView = rootView.findViewById(R.id.recycler_view);
        movieWatchedAdapter = new MovieWatchedAdapter(this);
        recyclerView.setAdapter(movieWatchedAdapter);
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
     * Handle the click to remove a movie from the watched movies
     * @param movieId the id of the movie to remove
     */
    @Override
    public void onRemoveWatchedMovie(int movieId) {
        movieWatchedViewModel.removeMovieFromWatched(movieId);
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

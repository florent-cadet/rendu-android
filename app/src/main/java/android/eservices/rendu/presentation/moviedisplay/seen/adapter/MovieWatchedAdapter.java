package android.eservices.rendu.presentation.moviedisplay.seen.adapter;

import android.eservices.rendu.R;
import android.eservices.rendu.presentation.moviedisplay.search.adapter.MovieItemViewModel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;

public class MovieWatchedAdapter extends RecyclerView.Adapter<MovieWatchedAdapter.MovieViewHolder> {


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView overviewTextView;
        private ImageView posterImageView;
        private View v;
        private MovieItemViewModel movieItemViewModel;
        private ImageButton movieWatchedButton;
        private MovieWatchedActionInterface movieWatchedActionInterface;
        private TextView seenDateTextView;

        public MovieViewHolder(final View v, final MovieWatchedActionInterface movieWatchedActionInterface) {
            super(v);
            this.v = v;
            this.movieWatchedActionInterface = movieWatchedActionInterface;
            this.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieWatchedActionInterface.onItemClicked(movieItemViewModel.getId());
                }
            });
            titleTextView = v.findViewById(R.id.movie_title_textview);
            overviewTextView = v.findViewById(R.id.movie_overview_textview);
            posterImageView = v.findViewById(R.id.movie_icon_imageview);
            seenDateTextView = v.findViewById(R.id.movie_seen_date_textview);
            movieWatchedButton = v.findViewById(R.id.movie_seen_button);
            movieWatchedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieWatchedActionInterface.onRemoveWatchedMovie(movieItemViewModel.getId());
                }
            });
        }

        /**
         * Bind the content of the elements of the layout from the MovieItemViewModel
         * @param movieItemViewModel a MovieItemViewModel
         */
        void bind(MovieItemViewModel movieItemViewModel) {
            this.movieItemViewModel = movieItemViewModel;
            titleTextView.setText(movieItemViewModel.getTitle());
            overviewTextView.setText(movieItemViewModel.getOverview());
            seenDateTextView.setText("Vu le : \n" + movieItemViewModel.getSeenDate());
            movieWatchedButton.setSelected(true);
            Glide.with(v)
                    .load(movieItemViewModel.getPosterUrl())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(posterImageView);
        }

    }

    private List<MovieItemViewModel> movieItemViewModelList;
    private MovieWatchedActionInterface movieWatchedActionInterface;

    public MovieWatchedAdapter(MovieWatchedActionInterface movieWatchedActionInterface) {
        movieItemViewModelList = new ArrayList<>();
        this.movieWatchedActionInterface = movieWatchedActionInterface;
    }

    /**
     * Set the list of the MovieItemViewModel and notify in case of change
     * @param movieItemViewModelList the list to set
     */
    public void bindViewModels(List<MovieItemViewModel> movieItemViewModelList) {
        this.movieItemViewModelList.clear();
        this.movieItemViewModelList.addAll(movieItemViewModelList);
        notifyDataSetChanged();
    }

    /**
     * Override of the onCreateViewHolder to prepare the view
     * @param parent the parent
     * @param viewType the viewType
     * @return
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watched_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(v, movieWatchedActionInterface);
        return movieViewHolder;
    }

    /**
     * Bind the elements of the MovieItemViewModel list in the view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movieItemViewModelList.get(position));
    }

    /**
     * Get the number of items
     * @return the size of the MovieItemViewModel list
     */
    @Override
    public int getItemCount() {
        return movieItemViewModelList.size();
    }


}

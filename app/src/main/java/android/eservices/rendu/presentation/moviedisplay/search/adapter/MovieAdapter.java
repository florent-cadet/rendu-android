package android.eservices.rendu.presentation.moviedisplay.search.adapter;

import android.eservices.rendu.R;
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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView overviewTextView;
        private ImageView posterImageView;
        private View v;
        private MovieItemViewModel movieItemViewModel;
        private ImageButton movieWatchedButton;
        private MovieActionInterface movieActionInterface;

        public MovieViewHolder(final View v, final MovieActionInterface movieActionInterface) {
            super(v);
            this.v = v;
            this.movieActionInterface = movieActionInterface;
            this.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieActionInterface.onItemClicked(movieItemViewModel.getId());
                }
            });
            titleTextView = v.findViewById(R.id.movie_title_textview);
            overviewTextView = v.findViewById(R.id.movie_overview_textview);
            posterImageView = v.findViewById(R.id.movie_icon_imageview);
            movieWatchedButton = v.findViewById(R.id.movie_seen_button);
            movieWatchedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(movieItemViewModel.getSeenDate() == null) {
                        movieItemViewModel.setSeenDate("watched");
                    } else {
                        movieItemViewModel.setSeenDate(null);
                    }
                    movieActionInterface.onWatchedButtonClicked(movieItemViewModel.getId(), movieItemViewModel.getSeenDate() != null);
                    movieWatchedButton.setSelected(movieItemViewModel.getSeenDate() != null);
                }
            });
        }

        void bind(MovieItemViewModel movieItemViewModel) {
            this.movieItemViewModel = movieItemViewModel;
            titleTextView.setText(movieItemViewModel.getTitle());
            overviewTextView.setText(movieItemViewModel.getOverview());
            movieWatchedButton.setSelected(movieItemViewModel.getSeenDate() != null);
            Glide.with(v)
                    .load(movieItemViewModel.getPosterUrl())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(posterImageView);
        }

    }

    private List<MovieItemViewModel> movieItemViewModelList;
    private MovieActionInterface movieActionInterface;

    public MovieAdapter(MovieActionInterface movieActionInterface) {
        movieItemViewModelList = new ArrayList<>();
        this.movieActionInterface = movieActionInterface;
    }

    public void bindViewModels(List<MovieItemViewModel> movieItemViewModelList) {
        this.movieItemViewModelList.clear();
        this.movieItemViewModelList.addAll(movieItemViewModelList);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(v, movieActionInterface);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movieItemViewModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return movieItemViewModelList.size();
    }

}
package android.eservices.rendu.presentation.moviedisplay.search.adapter;

import android.eservices.rendu.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public MovieViewHolder(View v) {
            super(v);
            this.v = v;
            titleTextView = v.findViewById(R.id.movie_title_textview);
            overviewTextView = v.findViewById(R.id.movie_overview_textview);
            posterImageView = v.findViewById(R.id.movie_icon_imageview);
        }

        void bind(MovieItemViewModel movieItemViewModel) {
            this.movieItemViewModel = movieItemViewModel;
            titleTextView.setText(movieItemViewModel.getTitle());
            overviewTextView.setText(movieItemViewModel.getOverview());
            Glide.with(v)
                    .load(movieItemViewModel.getPosterUrl())
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(posterImageView);
        }

    }

    private List<MovieItemViewModel> movieItemViewModelList;

    public MovieAdapter() {
        movieItemViewModelList = new ArrayList<>();
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
        return new MovieViewHolder(v);
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
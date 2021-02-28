package android.eservices.rendu.presentation.moviedetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.eservices.rendu.R;
import android.eservices.rendu.data.di.FakeDependencyInjection;
import android.eservices.rendu.presentation.moviedetails.model.MovieDetailItemViewModel;
import android.eservices.rendu.presentation.viewmodel.Event;
import android.eservices.rendu.presentation.viewmodel.MovieDetailsViewModel;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.Date;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView titleTextView;
    private ImageView posterImageView;
    private TextView releaseDateTextView;
    private TextView genresTextView;
    private TextView overviewTextView;
    private ImageButton imageButton;
    private TextView seenDateTextView;
    private MovieDetailsViewModel movieDetailsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        setupView();
        registerViewModel();
    }

    public void setupView() {
        titleTextView = findViewById(R.id.title_movie_detail);
        posterImageView = findViewById(R.id.poster_movie_detail);
        releaseDateTextView = findViewById(R.id.release_date_movie_detail);
        genresTextView = findViewById(R.id.genres_movie_detail);
        overviewTextView = findViewById(R.id.overview_movie_detail);
        imageButton = findViewById(R.id.movie_detail_seen_button);
        seenDateTextView = findViewById(R.id.movie_watched_textview);
        Toolbar toolbar =  findViewById(R.id.toolbar_movie_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void registerViewModel() {
        movieDetailsViewModel = new ViewModelProvider(this, FakeDependencyInjection.getViewModelFactory()).get(MovieDetailsViewModel.class);
        final int movieId = getIntent().getIntExtra("movieId", 0);
        if(movieId == 0) {
            finish();
        }

        movieDetailsViewModel.getMovieAddedEvent().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if(booleanEvent.getContentIfHasntBeenHandled()) {
                    imageButton.setSelected(true);
                    seenDateTextView.setText("Vu le " + DateFormat.format("dd-MM-yyyy", new Date()).toString());
                }
            }
        });

        movieDetailsViewModel.getMovieDeletedEvent().observe(this, new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                if (booleanEvent.getContentIfHasntBeenHandled()) {
                    imageButton.setSelected(false);
                    seenDateTextView.setText("Non vu");
                }
            }
        });

        movieDetailsViewModel.getMovieDetails(movieId).observe(this, new Observer<MovieDetailItemViewModel>() {
            @Override
            public void onChanged(final MovieDetailItemViewModel movieDetail) {
                titleTextView.setText(movieDetail.getTitle());
                Glide.with(MovieDetailsActivity.this)
                        .load(movieDetail.getPosterPath())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(posterImageView);
                releaseDateTextView.setText("Sorti le " + movieDetail.getReleaseDate());
                genresTextView.setText("Genres : " + movieDetail.getGenres());
                overviewTextView.setText(movieDetail.getOverview());
                if(movieDetail.getSeenDate() == null) {
                    imageButton.setSelected(false);
                    seenDateTextView.setText("Non vu");
                } else {
                    imageButton.setSelected(true);
                    seenDateTextView.setText("Vu le " + movieDetail.getSeenDate());
                }
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(movieDetail.getSeenDate() != null) {
                            movieDetailsViewModel.removeMovieFromWatched(movieId);
                            movieDetail.setSeenDate(null);
                        } else {
                            movieDetailsViewModel.addMovieToWatched(movieId);
                            movieDetail.setSeenDate(DateFormat.format("dd-MM-yyyy", new Date()).toString());
                        }
                    }
                });
            }
        });
    }
}
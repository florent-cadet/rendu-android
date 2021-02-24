package android.eservices.rendu.presentation.moviedisplay;

import android.content.Intent;
import android.eservices.rendu.R;
import android.eservices.rendu.presentation.moviedisplay.seen.fragment.SeenFragment;
import android.eservices.rendu.presentation.moviedisplay.search.fragment.SearchFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static android.eservices.rendu.MovieApplication.displayChangeAction;

public class MovieDisplayActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private static int displayState = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewPagerAndTabs();
    }

    private void setupViewPagerAndTabs() {
        viewPager = findViewById(R.id.tab_viewpager);

        final SearchFragment searchFragment = SearchFragment.newInstance();
        final SeenFragment fragmentTwo = SeenFragment.newInstance();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return searchFragment;
                }
                return fragmentTwo;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                if (position == 0) {
                    return SearchFragment.TAB_NAME;
                }
                return SeenFragment.TAB_NAME;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.display_list) {
            displayState = 1;
        } else if(id == R.id.display_grid) {
            displayState = 2;
        }

        Intent intent = new Intent(displayChangeAction);
        intent.putExtra(displayChangeAction, displayState);
        sendBroadcast(intent);
        return super.onOptionsItemSelected(item);
    }

}

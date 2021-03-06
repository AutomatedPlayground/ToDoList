package pl.automatedplayground.todolist.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleNetworkCallback;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.fragments.lists.DoingListFragment;
import pl.automatedplayground.todolist.fragments.lists.DoneListFragment;
import pl.automatedplayground.todolist.fragments.lists.ToDoListFragment;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_FIRSTRUN = "KEY_FIRST"; // key for first app run
    private static final int NUM_PAGES = 3; // 3 types of cards
    @InjectView(R.id.pager)
    protected ViewPager mPager;
    @InjectView(R.id.mode_loading)
    View modeLoading;


    private PagerAdapter mPagerAdapter;
    private boolean loading = false;
    private boolean firstRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        modeLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // just to interfere with background clickable objects
            }
        });
        setLoading(true, true);
        // not so good but gives user a info about other pages
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        mPager.setPageMargin(-margin);
    }

    private void setLoading(boolean load, final boolean onDemand) {
        if (load && !loading) {
            loading = true;
            invalidateOptionsMenu();
            modeLoading.setVisibility(View.VISIBLE);

            // check if its first run
            if (checkFirstRun())
                NetworkCardProvider.getInstance().initialSyncFromServer(new SimpleNetworkCallback<Object>() {
                    @Override
                    public void onError() {
                        // first run require network response
                        Toast.makeText(MainActivity.this, R.string.error_first, Toast.LENGTH_LONG).show();
                        // TODO: switch to view
                    }

                    @Override
                    public void onCallback(Object obj) {
                        // update ready
                        setFirstRunCompleted();
                        showDataAfterLoading(onDemand);
                    }
                });
            else
                NetworkCardProvider.getInstance().synchronizeCards(new SimpleNetworkCallback<Object>() {
                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, R.string.error_offline, Toast.LENGTH_LONG).show();
                        showDataAfterLoading(onDemand);
                    }

                    @Override
                    public void onCallback(Object obj) {
                        showDataAfterLoading(onDemand);
                    }
                });

        } else
            modeLoading.setVisibility(View.GONE);
    }


    private void showDataAfterLoading(final boolean onDemand) {
        mPager.post(new Runnable() {
            @Override
            public void run() {
                loading = false;
                if (onDemand) {
                    mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                    mPager.setAdapter(mPagerAdapter);
                }
                invalidateOptionsMenu();
                setLoading(false, true);
            }
        });
    }

    /**
     * Check if its a first run
     *
     * @return
     */
    private boolean checkFirstRun() {
        SharedPreferences settings = getSharedPreferences("data", MODE_PRIVATE);
        return settings.getBoolean(KEY_FIRSTRUN, true);
    }


    /**
     * Set that first run was completed sucessfully
     */
    private void setFirstRunCompleted() {
        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean(KEY_FIRSTRUN, false).commit();
    }


    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!loading)
            getMenuInflater().inflate(R.menu.menu_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            setLoading(true, false);
        }
        return true;
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int POSITION_DOING = 1;
        private static final int POSITION_DONE = 2;
        private static final int POSITION_TODO = 0;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case POSITION_DOING:
                    return new DoingListFragment();
                case POSITION_DONE:
                    return new DoneListFragment();
                case POSITION_TODO:
                default:
                    return new ToDoListFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}

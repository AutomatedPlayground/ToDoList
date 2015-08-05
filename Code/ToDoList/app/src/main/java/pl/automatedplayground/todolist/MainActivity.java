package pl.automatedplayground.todolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.automatedplayground.todolist.fragments.DoingListFragment;
import pl.automatedplayground.todolist.fragments.DoneListFragment;
import pl.automatedplayground.todolist.fragments.ToDoListFragment;

public class MainActivity extends AppCompatActivity {
    /**
     * The number of pages to show
     */
    private static final int NUM_PAGES = 3; // 3 types of cards

    @InjectView(R.id.pager)
    protected ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        // nice looking but unstable
        //        int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,     getResources().getDisplayMetrics());
        //        mPager.setPageMargin(-margin);
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

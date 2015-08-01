package pl.automatedplayground.todolist.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.dataprovider.model.ICard;

/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public abstract class ACardListFragment<DATATYPE extends ICard<?>, DATAPROVIDER extends SimpleDataProvider<DATATYPE>> extends Fragment implements SimpleCallback<DATATYPE> {

    @InjectView(R.id.listview)
    RecyclerView mListView;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout mRefresh;
    @InjectView(R.id.addbutton)
    ImageView mAddButton;

    DATAPROVIDER mDataProvider;

    public ACardListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        mListView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llm);
        mDataProvider = createDataProvider();
        mListView.setAdapter(new CardRecyclerViewAdapter(mDataProvider, this));


        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataProvider.refreshData(new SimpleCallback<ArrayList<DATATYPE>>() {

                    @Override
                    public void onCallback(final ArrayList<DATATYPE> obj) {
                        // it need to be done on ui thread
                        mListView.post(new Runnable() {
                            @Override
                            public void run() {
                                ((CardRecyclerViewAdapter) mListView.getAdapter()).setData(obj);
                                mRefresh.setRefreshing(false);
                                mListView.getAdapter().notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });

        mAddButton.setVisibility(isAddAvaiable() ? View.VISIBLE : View.INVISIBLE);
        mAddButton.setOnClickListener(createOnAddClicked());

        return view;
    }

    /**
     * Get on add button click listener
     * @return
     */
    protected abstract View.OnClickListener createOnAddClicked();

    /**
     * Should user see button "add"?
     * @return
     */
    protected abstract boolean isAddAvaiable();

    protected abstract DATAPROVIDER createDataProvider();

}

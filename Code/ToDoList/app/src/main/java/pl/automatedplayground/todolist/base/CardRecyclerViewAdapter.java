package pl.automatedplayground.todolist.base;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.SimpleDataProvider;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;

public class CardRecyclerViewAdapter<DATATYPE extends ICard<?>, DATAPROVIDER extends SimpleDataProvider<DATATYPE>> extends RecyclerView.Adapter<CardViewHolder> {

    private final SimpleCallback<DATATYPE> mCallbackOnClicked;
    private DATAPROVIDER mProvider;
    private ArrayList<DATATYPE> mData;

    public CardRecyclerViewAdapter(DATAPROVIDER provider, SimpleCallback<DATATYPE> onItemClicked) {
        mProvider = provider;
        mData = mProvider.getDataForView();
        mCallbackOnClicked = onItemClicked;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.layout_row, parent, false);
        return new CardViewHolder(itemView, new SimpleCallback<Integer>() {
            @Override
            public void onCallback(Integer position) {
                if (mCallbackOnClicked != null)
                    mCallbackOnClicked.onCallback(mData.get(position));
            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        DATATYPE data = mData.get(position);
        holder.mContent.setText(data.getContent().toString());
        holder.mTitle.setText(data.getTitle());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public void setData(ArrayList<DATATYPE> obj) {
        mData = obj;
    }
}

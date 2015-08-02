package pl.automatedplayground.todolist.base;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;

public class CardViewHolder extends RecyclerView.ViewHolder {

    public int position;
    @InjectView(R.id.row_title)
    protected TextView mTitle;
    @InjectView(R.id.row_content)
    protected TextView mContent;
    @InjectView(R.id.row_date)
    protected TextView mDate;

    public CardViewHolder(View view, final SimpleCallback<Integer> onActionClicked) {
        super(view);
        ButterKnife.inject(this, view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionClicked != null)
                    onActionClicked.onCallback(position);
            }
        });
    }


}

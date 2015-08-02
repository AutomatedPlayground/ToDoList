package pl.automatedplayground.todolist.fragments;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardFactory;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;

public class ToDoListFragment extends ACardListFragment<ToDoCard, SimpleDataProvider<ToDoCard>> implements SimpleDataProvider<ToDoCard> {

    @Override
    protected View.OnClickListener createOnAddClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Creating new object...", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected boolean isAddAvaiable() {
        return true;
    }

    @Override
    protected SimpleDataProvider<ToDoCard> createDataProvider() {
        return this;
    }

    @Override
    public void onCallback(ToDoCard obj) {
        Toast.makeText(getActivity(), "Clicked on " + obj.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public ArrayList<ToDoCard> getDataForView() {
        ArrayList<ToDoCard> tmp = new ArrayList<>();
        return tmp;
    }

    @Override
    public void refreshData(final SimpleCallback<ArrayList<ToDoCard>> simpleCallback) {
//        NetworkCardProvider.getInstance().putCard(ToDoCard.createMockCard(0), new Callback<TrelloCard>() {
//            @Override
//            public void success(TrelloCard trelloCards, Response response) {

        CardFactory.getInstance().getAllCardsForTODOList(simpleCallback);
    }


}

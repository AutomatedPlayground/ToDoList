package pl.automatedplayground.todolist.fragments;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import pl.automatedplayground.todolist.CardDetails;
import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.dataprovider.model.api.SimpleNetworkCallback;

public class ToDoListFragment extends ACardListFragment<ToDoCard, SimpleDataProvider<ToDoCard>> implements SimpleDataProvider<ToDoCard> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.color_todo);
    }

    @Override
    protected int getTextForHeader() {
        return R.string.header_todo;
    }

    @Override
    protected View.OnClickListener createOnAddClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardDetails.class);
                intent.putExtra(CardDetails.INTENT_CARDTYPE, CardType.TODO.toInt());
                startActivity(intent);
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
        Intent intent = new Intent(getActivity(), CardDetails.class);
        intent.putExtra(CardDetails.INTENT_CARD, obj);
        startActivity(intent);
    }

    @Override
    public ArrayList<ToDoCard> getDataForView() {
        ArrayList<ToDoCard> tmp = new ArrayList<>();
        return tmp;
    }

    @Override
    public void refreshData(final SimpleCallback<ArrayList<ToDoCard>> simpleCallback) {

        NetworkCardProvider.getInstance().synchronizeCards(new SimpleNetworkCallback<String>() {
            @Override
            public void onError() {
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                // load all local
                CardManager.getInstance().getAllCardsForTODOList(simpleCallback);
            }

            @Override
            public void onCallback(String obj) {
                // load local/updated from server
                CardManager.getInstance().getAllCardsForTODOList(simpleCallback);
            }
        });
    }

}

package pl.automatedplayground.todolist.fragments;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import pl.automatedplayground.todolist.CardDetails;
import pl.automatedplayground.todolist.R;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.configuration.ConfigFile;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.dataprovider.model.api.SimpleNetworkCallback;

public class DoneListFragment extends ACardListFragment<DoneCard, SimpleDataProvider<DoneCard>> implements SimpleDataProvider<DoneCard> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.color_done);
    }

    @Override
    protected int getTextForHeader() {
        return R.string.header_done;
    }
    @Override
    protected View.OnClickListener createOnAddClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CardDetails.class);
                intent.putExtra(CardDetails.INTENT_CARDTYPE, CardType.DONE.toInt());
                startActivity(intent);
            }
        };
    }

    @Override
    protected boolean isAddAvaiable() {
        return ConfigFile.SHOW_ADDBUTTON_ON_ALLVIEWS;
    }

    @Override
    protected SimpleDataProvider<DoneCard> createDataProvider() {
        return this;
    }

    @Override
    public void onCallback(DoneCard obj) {
        Intent intent = new Intent(getActivity(), CardDetails.class);
        intent.putExtra(CardDetails.INTENT_CARD, obj);
        startActivity(intent);
    }

    @Override
    public ArrayList<DoneCard> getDataForView() {
        ArrayList<DoneCard> tmp = new ArrayList<>();
        return tmp;
    }

    @Override
    public void refreshData(final SimpleCallback<ArrayList<DoneCard>> simpleCallback) {

        NetworkCardProvider.getInstance().synchronizeCards(new SimpleNetworkCallback<Object>() {
            @Override
            public void onError() {
                Toast.makeText(getActivity(), R.string.error_network, Toast.LENGTH_SHORT).show();
                // load all local
                CardManager.getInstance().getAllCardsForDoneList(simpleCallback);
            }

            @Override
            public void onCallback(Object obj) {
                // load local/updated from server
                CardManager.getInstance().getAllCardsForDoneList(simpleCallback);
            }
        });
    }
}

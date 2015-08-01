package pl.automatedplayground.todolist.fragments;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.automatedplayground.todolist.base.ACardListFragment;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.dataprovider.model.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
//      for (int i=0;i<50;i++)
//         tmp.add(ToDoCard.createMockData(i));
        return tmp;
    }

    @Override
    public void refreshData(final SimpleCallback<ArrayList<ToDoCard>> simpleCallback) {
//      Timer timer = new Timer();
//      timer.schedule(new TimerTask() {
//         @Override
//         public void run() {
//            ArrayList<ToDoCard> tmp = new ArrayList<>();
////            for (int i=0;i<50;i++)
////               tmp.add(ToDoCard.createMockData(i));
//            simpleCallback.onCallback(tmp);
//         }
//      },5000);
        NetworkCardProvider.getInstance().getLists(new Callback<List<TrelloList>>() {
            @Override
            public void success(List<TrelloList> trelloList, Response response) {
                ArrayList<ToDoCard> tmp = new ArrayList<ToDoCard>();
                for (int i=0;i<trelloList.size();i++)
                    tmp.add(ToDoCard.createListCard(trelloList.get(i)));
                simpleCallback.onCallback(tmp);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


}

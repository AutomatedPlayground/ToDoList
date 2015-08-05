package pl.automatedplayground.todolist;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 05.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;
import android.net.wifi.WifiManager;
import android.test.ApplicationTestCase;

import pl.automatedplayground.todolist.base.ToDoListApplication;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CardNetworkTest extends ApplicationTestCase<ToDoListApplication> {
    public CardNetworkTest() {
        super(ToDoListApplication.class);
    }

    // disclaimer: as for this moment this test dont work - it dont call network request on my device
    public void testAddEditAndRemove(){
        ((NetworkCardProvider) NetworkCardProvider.getInstance()).initWithContext(getApplication());
        final DoneCard mCard = DoneCard.createTestCard();
        ((NetworkCardProvider) NetworkCardProvider.getInstance()).putCard(mCard, new Callback<TrelloCard>() {
            @Override
            public void success(TrelloCard trelloCard, Response response) {
                assertEquals(trelloCard.getName(), mCard.getTitle());
                // now edit card
                mCard.setData(mCard.getLocalID(), "New title", "New content", trelloCard.getId());
                ((NetworkCardProvider) NetworkCardProvider.getInstance()).updateCard(mCard, new Callback<TrelloCard>() {
                    @Override
                    public void success(TrelloCard trelloCard, Response response) {
                        assertEquals(trelloCard.getName(),mCard.getTitle());
                        assertEquals(trelloCard.getDescription(),mCard.getContent());
                        // now remove card
                        ((NetworkCardProvider) NetworkCardProvider.getInstance()).removeCard(trelloCard, new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                assertTrue(true);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                assertFalse(true);
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        assertFalse(true);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                // error, cant continue
                assertFalse(true);
            }
        });
    }
}

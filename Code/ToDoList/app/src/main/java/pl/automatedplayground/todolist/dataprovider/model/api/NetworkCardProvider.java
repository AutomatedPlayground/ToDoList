package pl.automatedplayground.todolist.dataprovider.model.api;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import pl.automatedplayground.todolist.base.interfaces.NetworkCardProviderInterface;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.configuration.ConfigFile;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoingCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import pl.automatedplayground.todolist.dataprovider.model.realmmodel.RealmCard;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;
import retrofit.client.Response;

public class NetworkCardProvider implements ErrorHandler, RequestInterceptor, NetworkCardProviderInterface {
    private static final String API_URL = "https://api.trello.com";
    private static NetworkCardProvider ourInstance = new NetworkCardProvider();
    private ApiInterface apiInterface;
    private Context mContext;
    private boolean atLeastOneInsertedOrModified; // for internal usage only

    private NetworkCardProvider() {
    }

    public static NetworkCardProviderInterface getInstance() {
        return ourInstance;
    }

    public NetworkCardProvider initWithContext(Context ctx) {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("RETROFIT")).setErrorHandler(this).setRequestInterceptor(this)
                .setEndpoint(API_URL).build();
        apiInterface = restAdapter.create(ApiInterface.class);
        mContext = ctx;
        return this;
    }

    public String getBoard() {
        return ConfigFile.API_BOARD;
    }

    public String getKey() {
        return ConfigFile.API_KEY;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        Log.i("RETROFIT", "ERRO:" + cause.toString());
        return new Throwable(cause);
    }

    private void getLists(Callback<List<TrelloList>> trelloListCallback) {
        apiInterface.getBoardLists(getBoard(), getKey(), trelloListCallback);
    }

    public void getToDoCards(Callback<List<TrelloCard>> outputCallback) {
        apiInterface.getCardsForList(ConfigFile.API_LIST_TODO, ConfigFile.API_KEY, outputCallback);
    }

    public void getDoingCards(Callback<List<TrelloCard>> outputCallback) {
        apiInterface.getCardsForList(ConfigFile.API_LIST_DOING, ConfigFile.API_KEY, outputCallback);
    }

    public void getDoneCards(Callback<List<TrelloCard>> outputCallback) {
        apiInterface.getCardsForList(ConfigFile.API_LIST_DONE, ConfigFile.API_KEY, outputCallback);
    }

    public void putCard(ICard<String> mCard, Callback<TrelloCard> outputCallback) {
        if (mCard instanceof DoingCard)
            putCard((DoingCard) mCard, outputCallback);
        else if (mCard instanceof DoneCard)
            putCard((DoneCard) mCard, outputCallback);
        else if (mCard instanceof ToDoCard) // this at end, cause Doing and Done also are of ToDo type
            putCard((ToDoCard) mCard, outputCallback);
    }

    public void putCard(ToDoCard mCard, Callback<TrelloCard> outputCallback) {
        apiInterface.putCardIntoList(ConfigFile.API_KEY, ConfigFile.API_LIST_TODO, mCard.getTitle(), mCard.getContent(), ConfigFile.API_TOKEN, outputCallback);
    }

    public void putCard(DoingCard mCard, Callback<TrelloCard> outputCallback) {
        apiInterface.putCardIntoList(ConfigFile.API_KEY, ConfigFile.API_LIST_DOING, mCard.getTitle(), mCard.getContent(), ConfigFile.API_TOKEN, outputCallback);
    }

    public void putCard(DoneCard mCard, Callback<TrelloCard> outputCallback) {
        apiInterface.putCardIntoList(ConfigFile.API_KEY, ConfigFile.API_LIST_DONE, mCard.getTitle(), mCard.getContent(), ConfigFile.API_TOKEN, outputCallback);
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("content-type", "application/json");
    }

    public void updateCard(ICard<String> card, Callback<TrelloCard> callback) {
        String list = "";
        switch (card.getType()) {
            case DOING:
                list = ConfigFile.API_LIST_DOING;
                break;
            case DONE:
                list = ConfigFile.API_LIST_DONE;
                break;
            case TODO:
            default:
                list = ConfigFile.API_LIST_TODO;
                break;
        }
        apiInterface.updateCard(ConfigFile.API_KEY, card.getID(), card.getTitle(), card.getContent(), list, ConfigFile.API_TOKEN, callback);
    }

    public void removeCard(TrelloCard card, Callback<String> callback) {
        apiInterface.removeCard(card.getId(), ConfigFile.API_KEY, ConfigFile.API_TOKEN, callback);
    }


    public void moveCardToOtherList(String id, CardType whereToPut, Callback<String> callback) {
        String list = "";
        switch (whereToPut) {
            case DOING:
                list = ConfigFile.API_LIST_DOING;
                break;
            case DONE:
                list = ConfigFile.API_LIST_DONE;
                break;
            case TODO:
            default:
                list = ConfigFile.API_LIST_TODO;
                break;
        }
        apiInterface.moveCard(ConfigFile.API_KEY, id, list, ConfigFile.API_TOKEN, callback);
    }


    @Override
    public void synchronizeCards(final SimpleNetworkCallback<Object> callback) {
//        callback.onCallback(null);
        atLeastOneInsertedOrModified = false;
        // first load to do cards
        syncTodo(new SimpleCallback<List<TrelloCard>>() {
            @Override
            public void onCallback(List<TrelloCard> obj) {
                syncDoing(obj, new SimpleCallback<List<TrelloCard>>() {
                    @Override
                    public void onCallback(List<TrelloCard> obj) {
                        syncDone(obj, new SimpleCallback<List<TrelloCard>>() {
                            @Override
                            public void onCallback(final List<TrelloCard> leftCards) {
                                // left obj are items that should exist in todo or be deleted
//                                CardManager.getInstance().getAllCardsForTODOList(new SimpleCallback<ArrayList<ToDoCard>>() {
//                                    @Override
//                                    public void onCallback(ArrayList<ToDoCard> obj) {
//                                        if (leftCards != null)
//                                            for (int i = 0; i < leftCards.size(); i++) {
//                                                boolean exists = false;
//                                                if (obj != null)
//                                                    for (int j = 0; j < obj.size(); j++)
//                                                        if (obj.get(j).getID().equalsIgnoreCase(leftCards.get(i).getId())) {
//                                                            exists = true;
//                                                            moveCardToOtherList(leftCards.get(i).getId(), CardType.TODO, null);
//                                                            break;
//                                                        }
//                                                if (!exists) {
//                                                    // to be removed
//                                                    removeCard(leftCards.get(i), null);
//                                                }
//                                            }
                                        // final callback ;)
                                        callback.onCallback(null);
//                                    }
//                                });
                            }
                        }, callback);
                    }
                }, callback);
            }
        }, callback);

    }

    private void syncTodo(final SimpleCallback<List<TrelloCard>> afterFinish, final SimpleNetworkCallback<?> errorCallback) {
        getToDoCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(final List<TrelloCard> trelloCards, Response response) {
                // load local cards
                CardManager.getInstance().getAllCardsForTODOList(new SimpleCallback<ArrayList<ToDoCard>>() {
                    @Override
                    public void onCallback(ArrayList<ToDoCard> obj) {
                        for (int i = 0; i < obj.size(); i++) {
                            if (obj.get(i).getID().length() > 3) {
                                for (int j = 0; j < trelloCards.size(); j++) {
                                    if (trelloCards.get(j).getId().equalsIgnoreCase(obj.get(i).getID())) {
                                        if (obj.get(i).wasModified()) {
                                            // update
                                            updateCard(obj.get(i), null);
                                            atLeastOneInsertedOrModified = true;
                                        }
                                        trelloCards.remove(j);
                                        j--;
                                        break;
                                    }
                                }
                            } else {
                                // insert object to net db, without controlling the callback
                                putCard(obj.get(i), createUpdateCallback(obj.get(i)));
                                atLeastOneInsertedOrModified = true;
                            }
                        }
                        for (int i = 0; i < trelloCards.size(); i++) {
                            removeCard(trelloCards.get(i), new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
                            trelloCards.remove(i);
                            i--;
                        }
                        // now trellocards contains only cards that need to be removed from here
                        afterFinish.onCallback(trelloCards);

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                errorCallback.onError();
            }
        });
    }

    private Callback<TrelloCard> createUpdateCallback(final ICard<String> toDoCard) {
        return new Callback<TrelloCard>() {
            @Override
            public void success(TrelloCard trelloCard, Response response) {
                CardManager.getInstance().updateWebID(toDoCard.getLocalID(), trelloCard.getId());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        };
    }

    private void syncDoing(final List<TrelloCard> leftN, final SimpleCallback<List<TrelloCard>> afterFinish, final SimpleNetworkCallback<?> errorCallback) {
        getDoingCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(final List<TrelloCard> trelloCards, Response response) {
                // load local cards
                CardManager.getInstance().getAllCardsForDoingList(new SimpleCallback<ArrayList<DoingCard>>() {
                    @Override
                    public void onCallback(ArrayList<DoingCard> obj) {
                        for (int i = 0; i < obj.size(); i++) {
                            if (obj.get(i).getID().length() > 3) {
                                for (int j = 0; j < trelloCards.size(); j++) {
                                    if (trelloCards.get(j).getId().equalsIgnoreCase(obj.get(i).getID())) {
                                        if (obj.get(i).wasModified()) {
                                            // update
                                            updateCard(obj.get(i), null);
                                            atLeastOneInsertedOrModified = true;
                                        }
                                        trelloCards.remove(j);
                                        j--;
                                    }
                                }
                            } else {
                                // insert object to net db, without controlling the callback
                                putCard(obj.get(i), createUpdateCallback(obj.get(i)));
                                atLeastOneInsertedOrModified = true;
                            }
                        }
                        for (int i = 0; i < trelloCards.size(); i++) {
                            removeCard(trelloCards.get(i), new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
                            trelloCards.remove(i);
                            i--;
                        }

                        afterFinish.onCallback(trelloCards);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                errorCallback.onError();
            }
        });
    }


    private void syncDone(final List<TrelloCard> leftN, final SimpleCallback<List<TrelloCard>> afterFinish, final SimpleNetworkCallback<?> errorCallback) {
        getDoneCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(final List<TrelloCard> trelloCards, Response response) {
                // load local cards
                CardManager.getInstance().getAllCardsForDoneList(new SimpleCallback<ArrayList<DoneCard>>() {
                    @Override
                    public void onCallback(ArrayList<DoneCard> obj) {

                        for (int i = 0; i < obj.size(); i++) {
                            if (obj.get(i).getID().length() > 3) {
                                for (int j = 0; j < trelloCards.size(); j++) {
                                    if (trelloCards.get(j).getId().equalsIgnoreCase(obj.get(i).getID())) {
                                        if (obj.get(i).wasModified()) {
                                            // update
                                            updateCard(obj.get(i), null);
                                            atLeastOneInsertedOrModified = true;
                                        }
                                        trelloCards.remove(j);
                                        j--;
                                    }
                                }
                            } else {
                                // insert object to net db, without controlling the callback
                                putCard(obj.get(i), createUpdateCallback(obj.get(i)));
                                atLeastOneInsertedOrModified = true;
                            }
                        }
                        // now trellocards contains only cards that need to be removed from here
                        for (int i = 0; i < trelloCards.size(); i++) {
                            removeCard(trelloCards.get(i), new Callback<String>() {
                                @Override
                                public void success(String s, Response response) {

                                }

                                @Override
                                public void failure(RetrofitError error) {

                                }
                            });
                            trelloCards.remove(i);
                            i--;
                        }

                        afterFinish.onCallback(null);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                errorCallback.onError();
            }
        });
    }

    @Override
    public void initialSyncFromServer(final SimpleNetworkCallback<Object> callback) {
        // clear local database
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        realm.clear(RealmCard.class);
        realm.commitTransaction();
        // connect with network and download all current cards
        // cascades, not the best one, but fastest to be developed when there is no time
        // to create good inside-sync-and-outside-async downloader with requests
        getToDoCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(List<TrelloCard> trelloCards, Response response) {
                if (trelloCards != null)
                    for (int i = 0; i < trelloCards.size(); i++) {
                        CardManager.getInstance().addNewExistingNetCard(trelloCards.get(i), CardType.TODO);
                    }
                // now same with doing
                getDoingCards(new Callback<List<TrelloCard>>() {
                    @Override
                    public void success(List<TrelloCard> trelloCards, Response response) {
                        if (trelloCards != null)
                            for (int i = 0; i < trelloCards.size(); i++) {
                                CardManager.getInstance().addNewExistingNetCard(trelloCards.get(i), CardType.DOING);
                            }
                        getDoneCards(new Callback<List<TrelloCard>>() {
                            @Override
                            public void success(List<TrelloCard> trelloCards, Response response) {
                                if (trelloCards != null)
                                    for (int i = 0; i < trelloCards.size(); i++) {
                                        CardManager.getInstance().addNewExistingNetCard(trelloCards.get(i), CardType.DONE);
                                    }
                                callback.onCallback(null);
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                callback.onError();
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        callback.onError();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                callback.onError();
            }
        });
    }
}

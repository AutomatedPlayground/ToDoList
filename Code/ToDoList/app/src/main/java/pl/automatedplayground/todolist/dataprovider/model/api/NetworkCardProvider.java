package pl.automatedplayground.todolist.dataprovider.model.api;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;
import android.util.Log;

import java.util.List;

import pl.automatedplayground.todolist.configuration.ConfigFile;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoingCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.android.AndroidLog;

public class NetworkCardProvider implements ErrorHandler, RequestInterceptor {
    private static final String API_URL = "https://api.trello.com";
    private static NetworkCardProvider ourInstance = new NetworkCardProvider();
    private ApiInterface apiInterface;

    public static NetworkCardProvider getInstance() {
        return ourInstance;
    }

    private NetworkCardProvider() {
    }

    public NetworkCardProvider initWithContext(Context ctx) {
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("RETROFIT")).setErrorHandler(this).setRequestInterceptor(this)
                .setEndpoint(API_URL).build();
        apiInterface = restAdapter.create(ApiInterface.class);
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
}

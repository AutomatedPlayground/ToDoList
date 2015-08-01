package pl.automatedplayground.todolist.dataprovider.model.api;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;
import android.util.Log;

import java.util.List;

import pl.automatedplayground.todolist.configuration.ConfigFile;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import retrofit.Callback;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;

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
        RestAdapter restAdapter = new RestAdapter.Builder().setRequestInterceptor(this).setLog(new RestAdapter.Log() {
            @Override
            public void log(String message) {
                Log.i("RETROFIT",message);
            }
        }).setErrorHandler(this)
                .setEndpoint(API_URL).build();
        apiInterface = restAdapter.create(ApiInterface.class);
        return this;
    }

    public String getBoard(){
        return ConfigFile.API_BOARD;
    }

    public String getKey(){
        return ConfigFile.API_KEY;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {
        return new Throwable(cause);
    }

    @Override
    public void intercept(RequestFacade request) {

//        request.addQueryParam("key",getKey());
//        request.addQueryParam("board",getBoard());
    }

    public void getLists(Callback<List<TrelloList>> trelloListCallback) {
        apiInterface.getBoardLists(getBoard(),getKey(),trelloListCallback);
    }
}

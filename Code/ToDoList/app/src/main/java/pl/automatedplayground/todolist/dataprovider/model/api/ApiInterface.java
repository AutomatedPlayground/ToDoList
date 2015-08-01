package pl.automatedplayground.todolist.dataprovider.model.api;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import java.util.List;

import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {
    @GET("/1/boards/{board1}?key={key1}")
    public void getBoard(@Path("board1")String board,@Path("key1")String key,Callback<String> response);

    @GET("/1/boards/{board}/lists")
    public void getBoardLists(@Path("board")String board,@Query("key")String key,Callback<List<TrelloList>> response);
}
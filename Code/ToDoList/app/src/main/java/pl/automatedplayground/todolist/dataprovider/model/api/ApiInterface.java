package pl.automatedplayground.todolist.dataprovider.model.api;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import java.util.List;

import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;
import retrofit.Callback;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiInterface {
    @GET("/1/boards/{board1}?key={key1}")
    public void getBoard(@Path("board1") String board, @Path("key1") String key, Callback<String> response);

    @GET("/1/boards/{board}/lists")
    public void getBoardLists(@Path("board") String board, @Query("key") String key, Callback<List<TrelloList>> response);

    @GET("/1/lists/{list}/cards")
    public void getCardsForList(@Path("list") String list, @Query("key") String key, Callback<List<TrelloCard>> response);

    @POST("/1/cards")
    public void putCardIntoList(@Query("key") String key, @Query("idList") String list, @Query("name") String name, @Query("desc") String description, @Query("token") String token, Callback<TrelloCard> response);

    @POST("/1/cards/{id}")
    public void updateCard(@Query("key") String key, @Path("id") String id, @Query("name") String name, @Query("desc") String description, @Query("idList") String currentList, @Query("token") String token, Callback<TrelloCard> response);

    @POST("/1/cards/{id}")
    public void moveCard(@Query("key") String key,@Path("id")String id, @Query("idList") String list,@Query("token") String token, Callback<String> response);

    @DELETE("/1/cards/{id}")
    public void removeCard(@Path("id") String id, @Query("key") String key, @Query("token") String token, Callback<String> response);


}

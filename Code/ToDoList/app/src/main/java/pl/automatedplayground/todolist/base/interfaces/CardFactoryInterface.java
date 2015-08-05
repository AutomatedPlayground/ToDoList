package pl.automatedplayground.todolist.base.interfaces;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import org.joda.time.DateTime;

import java.util.ArrayList;

import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoingCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import retrofit.Callback;

/**
 * Public visible functions
 */
public interface CardFactoryInterface {
    ICard<String> getCardByLocalID(int cardID);

    /**
     * Get all cards for 1st list
     *
     * @param output
     * @return
     */
    void getAllCardsForTODOList(SimpleCallback<ArrayList<ToDoCard>> output);

    /**
     * Get all cards for second list
     *
     * @return
     */
    void getAllCardsForDoingList(SimpleCallback<ArrayList<DoingCard>> output);

    /**
     * Get all cards for third list
     *
     * @return
     */
    void getAllCardsForDoneList(SimpleCallback<ArrayList<DoneCard>> output);

    /**
     * Create new card in DB using source and return updated object
     *
     * @return
     */
    void createNewCard(String title, String content, CardType listToAdd, String id,SimpleCallback<ICard<String>> onReturn);

    /**
     * Move card to other list - or remove if null as param
     *
     * @param source
     * @param newRequestedType
     * @return
     */
    void changeCardType(ICard<String> source, CardType newRequestedType, SimpleCallback<ICard<String>> onReturn);

    /**
     * Update card info - content and name
     *
     * @param source
     * @return
     */
    void changeCardData(ICard<String> source, SimpleCallback<ICard<String>> onReturn);

    /**
     * Remove card
     *
     * @param source
     * @return
     */
    void removeCard(ICard<String> source, SimpleCallback<Boolean> onReturn);

    void addNewExistingNetCard(TrelloCard trelloCard, CardType mode);

    void updateWebID(int localID, String id);
}

package pl.automatedplayground.todolist.dataprovider.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Public visible functions
 */
public interface CardFactoryInterface{
   /**
    * Get all cards for 1st list
    * @return
    */
   ArrayList<ICard<String>> getAllCardsForTODOList();

   /**
    * Get all cards for second list
    * @return
    */
   ArrayList<ICard<String>> getAllCardsForDoingList();

   /**
    * Get all cards for third list
    * @return
    */
   ArrayList<ICard<String>> getAllCardsForDoneList();

   /**
    * Update card in database
    * @param source
    * @return false if object dont exist in db
    */
   boolean updateCardInDb(ICard<String> source);

   /**
    * Create new card in DB using source and return updated object
    * @return
    */
   ICard<String> createNewCardInDB(String title,String content,DateTime dateForCard,CardType listToAdd);

   /**
    * Move card to other list - or remove if null as param
    * @param source
    * @param newRequestedType
    * @return
    */
   ICard<String> changeCardType(ICard<String> source,CardType newRequestedType);
}

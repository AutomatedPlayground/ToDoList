package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;

public class DoingCard extends ToDoCard {

   DoingCard(){

   }

   @Override
   public CardType getType() {
      return CardType.DOING;
   }

   public static DoingCard createListCard(TrelloCard trelloList) {
      DoingCard tmp = new DoingCard();
      tmp.setData(trelloList.getName(), trelloList.getDescription(), trelloList.getId());
      return tmp;
   }
}

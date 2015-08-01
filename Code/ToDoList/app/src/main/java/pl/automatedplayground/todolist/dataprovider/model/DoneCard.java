package pl.automatedplayground.todolist.dataprovider.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public class DoneCard extends ToDoCard {
   @Override
   public CardType getType() {
      return CardType.DONE;
   }
}

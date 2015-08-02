package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;

public class DoneCard extends ToDoCard {

    DoneCard() {
        super();
    }

    @Override
    public CardType getType() {
        return CardType.DONE;
    }

    public static DoneCard createListCard(TrelloCard trelloList) {
        DoneCard tmp = new DoneCard();
        tmp.setData(trelloList.getName(), trelloList.getDescription(), trelloList.getId());
        return tmp;
    }
}

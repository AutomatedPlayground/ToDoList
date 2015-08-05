package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public class DoneCard extends ToDoCard {

    DoneCard() {
        super();
    }

//    public static DoneCard createListCard(TrelloCard trelloList) {
//        DoneCard tmp = new DoneCard();
//        tmp.setData(trelloList.getName(), trelloList.getDescription(), trelloList.getId());
//        return tmp;
//    }

    public static DoneCard createTestCard() {
        DoneCard card = new DoneCard();
        card.setData(-1, "test title", "test description", "");
        return card;
    }

    @Override
    public CardType getType() {
        return CardType.DONE;
    }
}

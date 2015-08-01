package pl.automatedplayground.todolist.dataprovider.model;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.automatedplayground.todolist.dataprovider.model.realmodel.RealmCard;

public class CardFactory implements CardFactoryInterface {

    private static CardFactory ourInstance = new CardFactory();
    private Context mContext;

    /**
     * We isolate avaiable functions using class interface
     * @return
     */
    public static CardFactoryInterface getInstance() {
        return ourInstance;
    }

    private CardFactory() {
    }

    /**
     * Hidden function
     * @param context
     */
    public void setContext(Context context){
        mContext = context;
    }

    /**
     * Create object using realm card
     * @param card
     * @return
     */
    ICard<String> getCardFromRealmObject(RealmCard card){
        ICard<String> output;
        switch(CardType.fromId(card.getType())){
            case DONE:
                output = new DoneCard();
                break;
            case DOING:
                output = new DoingCard();
                break;
            case TODO:
            default:
                output = new ToDoCard();
                break;
        }
        output.setData(card.getTitle(),card.getContent(),card.getDateTime(),card.getID());
        return output;
    }

    @Override
    public ArrayList<ICard<String>> getAllCardsForTODOList() {
        return null;
    }

    @Override
    public ArrayList<ICard<String>> getAllCardsForDoingList() {
        return null;
    }

    @Override
    public ArrayList<ICard<String>> getAllCardsForDoneList() {
        return null;
    }

    /**
     * Update card in database
     * @param source
     * @return
     */
    @Override
    public boolean updateCardInDb(ICard<String> source){

        return false;
    }

    @Override
    public ICard<String> createNewCardInDB(String title, String content, DateTime dateForCard, CardType listToAdd) {
        // Obtain a Realm instance
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        RealmCard card = realm.createObject(RealmCard.class);
        card.setTitle(title);
        card.setContent(content);
        card.setDateTime(DateTimeFormat.forPattern("hh:mm dd/MM/yyyy").print(dateForCard));
        card.setType(listToAdd.toInt());
        realm.commitTransaction();
        return getCardFromRealmObject(card);
    }

    @Override
    public ICard<String> changeCardType(ICard<String> source, CardType newRequestedType) {
        if (newRequestedType==null) {
            // remove card
            Realm realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            RealmResults<RealmCard> obj = realm.allObjects(RealmCard.class);
            if (obj!=null)
                for (int i=0;i<obj.size();i++){
                    if (obj.get(i).getID() == source.getID()){
                        obj.get(i).removeFromRealm();
                    }
                }
            return null;
        }
        return null;
    }
}

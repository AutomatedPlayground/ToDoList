package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 04.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.CardFactoryInterface;
import pl.automatedplayground.todolist.dataprovider.model.realmmodel.RealmCard;

public class CardManager implements CardFactoryInterface {
    static private CardManager mInstance = null;
    Context mContext;


    public static CardFactoryInterface getInstance() {
        if (mInstance == null)
            mInstance = new CardManager();
        return mInstance;
    }

    public void setContext(Context ctx) {
        mContext = ctx;
    }

    private CardManager() {
        // hiding constructor
    }

    /**
     * Get all cards for selected list
     *
     * @param inputtype@return
     */
    private <T extends ICard<String>> ArrayList<T> getCardsForList(T inputtype) {
        final ArrayList<T> data = new ArrayList<>();
        Realm realm = Realm.getInstance(mContext);
        final RealmResults<RealmCard> localRealm = realm.allObjects(RealmCard.class);
        for (int i = 0; i < localRealm.size(); i++)
            if (localRealm.get(i).getType() == inputtype.getType().toInt()) {
                data.add((T) convert(localRealm.get(i)));
            }
        return data;
    }

    /**
     * Create card from realmCard
     *
     * @param realmCard
     * @return
     */
    private ICard<String> convert(RealmCard realmCard) {
        ICard<String> output;
        if (realmCard.getType() == CardType.DONE.toInt())
            output = new DoneCard();
        else if (realmCard.getType() == CardType.DOING.toInt())
            output = new DoingCard();
        else
            output = new ToDoCard();

        output.setData(realmCard.getLocalID(), realmCard.getTitle(), realmCard.getContent(), realmCard.getID());

        return output;
    }

    /**
     * Update card data in local base
     *
     * @param localID
     * @param newTxt
     * @param newContent
     * @param newNetID
     * @return
     */
    private boolean updateCardData(int localID, String newTxt, String newContent, String newNetID) {
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        RealmQuery<RealmCard> objs = realm.allObjects(RealmCard.class).where().equalTo("localID", localID);
        if (objs == null || objs.count() == 0)
            return false;
        RealmCard object = objs.findFirst();
        object.setTitle(newTxt);
        if (newNetID == null)
            object.setModified(1);
        else
            object.setModified(0); // case for updating just net ID
        object.setContent(newContent);
        object.setID(newNetID);
        realm.commitTransaction();
        return true;
    }

    /**
     * Move card to other list
     *
     * @param localID
     * @param newList
     * @return
     */
    private boolean moveCardToOtherList(int localID, CardType newList) {
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        RealmQuery<RealmCard> objs = realm.allObjects(RealmCard.class).where().equalTo("localID", localID);
        if (objs == null || objs.count() == 0)
            return false;
        RealmCard object = objs.findFirst();
        object.setType(newList.toInt());
        object.setModified(1);
        realm.commitTransaction();
        return true;
    }

    /**
     * Create new card in DB and return its ID
     *
     * @param name
     * @param content
     * @param type
     * @return
     */
    private int createNewCard(String name, String content, CardType type) {
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        RealmCard object = realm.createObject(RealmCard.class);
        object.setID("");
        object.setModified(0);
        object.setContent(content);
        object.setTitle(name);
        object.setType(type.toInt());
        object.setLocalID((int) (realm.where(RealmCard.class).maximumInt("localID") + 1));
        realm.commitTransaction();
        return object.getLocalID();
    }

    /**
     * Remove card from database
     *
     * @param localID
     * @return
     */
    private boolean removeCard(int localID) {
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        RealmQuery<RealmCard> objs = realm.allObjects(RealmCard.class).where().equalTo("localID", localID);
        if (objs == null || objs.count() == 0)
            return false;
        objs.findFirst().removeFromRealm();
        realm.commitTransaction();
        return true;
    }

    /**
     * Get card
     *
     * @param cardID
     */
    @Override
    public ICard<String> getCardByLocalID(int cardID) {
        Realm realm = Realm.getInstance(mContext);
        RealmQuery<RealmCard> objs = realm.allObjects(RealmCard.class).where().equalTo("localID", cardID);
        if (objs == null || objs.count() == 0)
            return null;
        return convert(objs.findFirst());
    }

    @Override
    public void getAllCardsForTODOList(SimpleCallback<ArrayList<ToDoCard>> output) {
        output.onCallback(getCardsForList(new ToDoCard()));
    }

    @Override
    public void getAllCardsForDoingList(SimpleCallback<ArrayList<DoingCard>> output) {
        output.onCallback(getCardsForList(new DoingCard()));
    }

    @Override
    public void getAllCardsForDoneList(SimpleCallback<ArrayList<DoneCard>> output) {
        output.onCallback(getCardsForList(new DoneCard()));
    }

    @Override
    public void createNewCard(String title, String content, CardType listToAdd, SimpleCallback<ICard<String>> onReturn) {
        int cardID = createNewCard(title, content, listToAdd);
        if (onReturn != null)
            onReturn.onCallback(getCardByLocalID(cardID));
    }


    @Override
    public void changeCardType(ICard<String> source, CardType newRequestedType, SimpleCallback<ICard<String>> onReturn) {
        moveCardToOtherList(source.getLocalID(), newRequestedType);
        if (onReturn != null)
            onReturn.onCallback(getCardByLocalID(source.getLocalID()));
    }

    @Override
    public void changeCardData(ICard<String> source, SimpleCallback<ICard<String>> onReturn) {
        updateCardData(source.getLocalID(), source.getTitle(), source.getContent(), source.getID());
        if (onReturn != null)
            onReturn.onCallback(getCardByLocalID(source.getLocalID()));
    }

    @Override
    public void removeCard(ICard<String> source, SimpleCallback<Boolean> onReturn) {
        if (onReturn != null)
            onReturn.onCallback(removeCard(source.getLocalID()));
    }
}

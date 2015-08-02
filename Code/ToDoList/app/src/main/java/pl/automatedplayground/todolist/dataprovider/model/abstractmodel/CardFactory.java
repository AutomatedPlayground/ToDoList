package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.CardFactoryInterface;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;
import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;
import pl.automatedplayground.todolist.dataprovider.model.realmmodel.RealmCard;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CardFactory implements CardFactoryInterface {

    private static CardFactory ourInstance = new CardFactory();
    private Context mContext;

    /**
     * We isolate avaiable functions using class interface
     *
     * @return
     */
    public static CardFactoryInterface getInstance() {
        return ourInstance;
    }

    private CardFactory() {
    }

    /**
     * Hidden function - set context
     *
     * @param context
     */
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * Try to update information with server for items that need it. Also update all data from server
     */
    public void tryToUpdateWithServer() {
        final ArrayList<ToDoCard> data = new ArrayList<>();
        Realm realm = Realm.getInstance(mContext);
        final RealmResults<RealmCard> localRealm = realm.allObjects(RealmCard.class);
        for (int i = 0; i < localRealm.size(); i++)
            if (localRealm.get(i).getID() == null || localRealm.get(i).getID().length() < 3) {
                createNewCard(localRealm.get(i));
            }
    }

    /**
     * Create object using realm card
     *
     * @param card
     * @return
     */
    ICard<String> getCardFromRealmObject(RealmCard card) {
        ICard<String> output;
        switch (CardType.fromId(card.getType())) {
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
        output.setData(card.getTitle(), card.getContent(), card.getID());
        return output;
    }


    /**
     * This method allows offline work
     *
     * @param output
     */
    @Override
    public void getAllCardsForTODOList(final SimpleCallback<ArrayList<ToDoCard>> output) {
        final ArrayList<ToDoCard> data = new ArrayList<>();
        Realm realm = Realm.getInstance(mContext);
        final RealmResults<RealmCard> localRealm = realm.allObjects(RealmCard.class);
        for (int i = 0; i < localRealm.size(); i++)
            if (localRealm.get(i).getType() == CardType.TODO.toInt()) {
                data.add((ToDoCard) getCardFromRealmObject(localRealm.get(i)));
            }
        // local data is ready, now check online one
        NetworkCardProvider.getInstance().getToDoCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(List<TrelloCard> trelloList, Response response) {
                Log.i("TAG", response.getBody().toString());
                ArrayList<ToDoCard> tmp = new ArrayList<ToDoCard>();
                for (int i = 0; i < trelloList.size(); i++)
                    tmp.add(ToDoCard.createListCard(trelloList.get(i)));
                // now check if all data exists locally
                int sizeAtStart = data.size(); // store last unchecked sum of elements
                for (int i = 0; i < tmp.size(); i++) {
                    boolean exists = false;
                    for (int j = 0; j < sizeAtStart && !exists; j++)
                        if (data.get(j).getID() != null && data.get(j).getID().equalsIgnoreCase(tmp.get(i).getID()))
                            exists = true;
                    if (!exists) {
                        // add
                        addObjectToLocalStore(tmp.get(i));
                        data.add(tmp.get(i));
                    }
                }
                // now upload all data that don't exists on web
                for (int i = 0; i < localRealm.size(); i++)
                    if (localRealm.get(i).getID() == null || localRealm.get(i).getID().length() < 3) {
                        createNewCard(localRealm.get(i));
                    }
                output.onCallback(data);
            }

            @Override
            public void failure(RetrofitError error) {
                output.onCallback(data);
            }
        });
    }

    @Override
    public void getAllCardsForDoingList(final SimpleCallback<ArrayList<DoingCard>> output) {
        final ArrayList<DoingCard> data = new ArrayList<>();
        Realm realm = Realm.getInstance(mContext);
        final RealmResults<RealmCard> localRealm = realm.allObjects(RealmCard.class);
        for (int i = 0; i < localRealm.size(); i++)
            if (localRealm.get(i).getType() == CardType.DOING.toInt()) {
                data.add((DoingCard) getCardFromRealmObject(localRealm.get(i)));
            }
        // local data is ready, now check online one
        NetworkCardProvider.getInstance().getDoingCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(List<TrelloCard> trelloList, Response response) {
                Log.i("TAG", response.getBody().toString());
                ArrayList<DoingCard> tmp = new ArrayList<DoingCard>();
                for (int i = 0; i < trelloList.size(); i++)
                    tmp.add(DoingCard.createListCard(trelloList.get(i)));
                // now check if all data exists locally
                int sizeAtStart = data.size(); // store last unchecked sum of elements
                for (int i = 0; i < tmp.size(); i++) {
                    boolean exists = false;
                    for (int j = 0; j < sizeAtStart && !exists; j++)
                        if (data.get(j).getID() != null && data.get(j).getID().equalsIgnoreCase(tmp.get(i).getID()))
                            exists = true;
                    if (!exists) {
                        // add
                        addObjectToLocalStore(tmp.get(i));
                        data.add(tmp.get(i));
                    }
                }
                // now upload all data that don't exists on web
                for (int i = 0; i < localRealm.size(); i++)
                    if (localRealm.get(i).getID() == null || localRealm.get(i).getID().length() < 3) {
                        createNewCard(localRealm.get(i));
                    }
                output.onCallback(data);
            }

            @Override
            public void failure(RetrofitError error) {
                output.onCallback(data);
            }
        });
    }

    @Override
    public void getAllCardsForDoneList(final SimpleCallback<ArrayList<DoneCard>> output) {
        final ArrayList<DoneCard> data = new ArrayList<>();
        Realm realm = Realm.getInstance(mContext);
        final RealmResults<RealmCard> localRealm = realm.allObjects(RealmCard.class);
        for (int i = 0; i < localRealm.size(); i++)
            if (localRealm.get(i).getType() == CardType.DONE.toInt()) {
                data.add((DoneCard) getCardFromRealmObject(localRealm.get(i)));
            }
        // local data is ready, now check online one
        NetworkCardProvider.getInstance().getDoneCards(new Callback<List<TrelloCard>>() {
            @Override
            public void success(List<TrelloCard> trelloList, Response response) {
                Log.i("TAG", response.getBody().toString());
                ArrayList<DoneCard> tmp = new ArrayList<DoneCard>();
                for (int i = 0; i < trelloList.size(); i++)
                    tmp.add(DoneCard.createListCard(trelloList.get(i)));
                // now check if all data exists locally
                int sizeAtStart = data.size(); // store last unchecked sum of elements
                for (int i = 0; i < tmp.size(); i++) {
                    boolean exists = false;
                    for (int j = 0; j < sizeAtStart && !exists; j++)
                        if (data.get(j).getID() != null && data.get(j).getID().equalsIgnoreCase(tmp.get(i).getID()))
                            exists = true;
                    if (!exists) {
                        // add
                        addObjectToLocalStore(tmp.get(i));
                        data.add(tmp.get(i));
                    }
                }
                // now upload all data that don't exists on web
                for (int i = 0; i < localRealm.size(); i++)
                    if (localRealm.get(i).getID() == null || localRealm.get(i).getID().length() < 3) {
                        createNewCard(localRealm.get(i));
                    }
                output.onCallback(data);
            }

            @Override
            public void failure(RetrofitError error) {
                output.onCallback(data);
            }
        });
    }


    private void addObjectToLocalStore(ICard<String> toDoCard) {
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        final RealmCard card = realm.createObject(RealmCard.class);
        card.setTitle(toDoCard.getTitle());
        card.setContent(toDoCard.getContent());
        card.setType(toDoCard.getType().toInt());
        card.setID(toDoCard.getID());
        realm.commitTransaction();
    }

    @Override
    public void createNewCard(String title, String content, DateTime dateForCard, CardType listToAdd, Callback<ICard<String>> onReturn) {
        // first let's try to create object
        Realm realm = Realm.getInstance(mContext);
        realm.beginTransaction();
        final RealmCard card = realm.createObject(RealmCard.class);
        card.setTitle(title);
        card.setContent(content);
        card.setType(listToAdd.toInt());
        card.setID(null);
        realm.commitTransaction();
        NetworkCardProvider.getInstance().putCard(getCardFromRealmObject(card), new Callback<TrelloCard>() {
            @Override
            public void success(TrelloCard trelloCard, Response response) {
                // update ID of object
                Realm realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                card.setID(trelloCard.getId());
                realm.commitTransaction();
            }

            @Override
            public void failure(RetrofitError error) {
                // card can't be synchronized online, so it's just an offline copy
            }
        });
    }

    /**
     * Add card that already exists in local storage
     *
     * @param card
     */
    void createNewCard(final RealmCard card) {

        NetworkCardProvider.getInstance().putCard(getCardFromRealmObject(card), new Callback<TrelloCard>() {
            @Override
            public void success(TrelloCard trelloCard, Response response) {
                // update ID of object
                Realm realm = Realm.getInstance(mContext);
                realm.beginTransaction();
                card.setID(trelloCard.getId());
                realm.commitTransaction();
            }

            @Override
            public void failure(RetrofitError error) {
                // card can't be synchronized online, so it's just an offline copy
            }
        });
    }

    @Override
    public void changeCardType(ICard<String> source, CardType newRequestedType, Callback<ICard<String>> onReturn) {

//    @Override
//    public ICard<String> changeCardType(ICard<String> source, CardType newRequestedType) {
//        if (newRequestedType==null) {
//            // remove card
//            Realm realm = Realm.getInstance(mContext);
//            realm.beginTransaction();
//            RealmResults<RealmCard> obj = realm.allObjects(RealmCard.class);
//            if (obj!=null)
//                for (int i=0;i<obj.size();i++){
//                    if (obj.get(i).getID() == source.getID()){
//                        obj.get(i).removeFromRealm();
//                    }
//                }
//            return null;
//        }
//        return null;
//    }
    }

    @Override
    public void changeCardData(ICard<String> source, Callback<ICard<String>> onReturn) {

    }

    @Override
    public void removeCard(ICard<String> source, Callback<Boolean> onReturn) {

    }
}

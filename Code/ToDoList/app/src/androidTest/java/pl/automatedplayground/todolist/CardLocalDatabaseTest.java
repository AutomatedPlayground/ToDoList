package pl.automatedplayground.todolist;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import java.util.ArrayList;

import io.realm.Realm;
import pl.automatedplayground.todolist.activities.MainActivity;
import pl.automatedplayground.todolist.base.ToDoListApplication;
import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardType;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoneCard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ICard;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.ToDoCard;
import pl.automatedplayground.todolist.dataprovider.model.realmmodel.RealmCard;

public class CardLocalDatabaseTest extends ApplicationTestCase<ToDoListApplication> {
    public CardLocalDatabaseTest() {
        super(ToDoListApplication.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit().putBoolean(MainActivity.KEY_FIRSTRUN, true).commit();
    }

    /**
     * Test adding a card to local database, also testing seek by ID
     */
    public void testAddCard() {
        String title = "title";
        String desc = "desc";
        CardType type = CardType.TODO;
        ((CardManager) CardManager.getInstance()).setContext(getContext());

        // clear all database
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();
        realm.clear(RealmCard.class);
        realm.commitTransaction();

        CardManager.getInstance().createNewCard(title, desc, type, null, new SimpleCallback<ICard<String>>() {
            @Override
            public void onCallback(ICard<String> obj) {
                final int localID = obj.getLocalID();
                // get local card using getter
                CardManager.getInstance().getAllCardsForTODOList(new SimpleCallback<ArrayList<ToDoCard>>() {
                    @Override
                    public void onCallback(ArrayList<ToDoCard> obj) {
                        if (obj.size() == 0)
                            assertEquals(true, false); // error
                        assertEquals(obj.get(0).getLocalID(), localID);
                    }
                });
            }
        });
    }

    /**
     * Test removing card
     */
    public void testRemove() {
        String title = "title";
        String desc = "desc";
        CardType type = CardType.TODO;
        ((CardManager) CardManager.getInstance()).setContext(getContext());

        // clear all database
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();
        realm.clear(RealmCard.class);
        realm.commitTransaction();

        CardManager.getInstance().createNewCard(title, desc, type, null, new SimpleCallback<ICard<String>>() {
            @Override
            public void onCallback(ICard<String> obj) {
                final int localID = obj.getLocalID();
                // get local card using getter
                CardManager.getInstance().getAllCardsForTODOList(new SimpleCallback<ArrayList<ToDoCard>>() {
                    @Override
                    public void onCallback(ArrayList<ToDoCard> obj) {
                        CardManager.getInstance().removeCard(CardManager.getInstance().getCardByLocalID(localID), new SimpleCallback<Boolean>() {
                            @Override
                            public void onCallback(Boolean obj) {
                                // now check if data is really removed
                                assertNull(CardManager.getInstance().getCardByLocalID(localID));
                                // and return statement
                                assertEquals(Boolean.TRUE, obj);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Test moving card to other list
     */
    public void testMoveToOtherList() {
        String title = "title";
        String desc = "desc";
        CardType type = CardType.TODO;
        ((CardManager) CardManager.getInstance()).setContext(getContext());

        // clear all database
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();
        realm.clear(RealmCard.class);
        realm.commitTransaction();

        CardManager.getInstance().createNewCard(title, desc, type, null, new SimpleCallback<ICard<String>>() {
            @Override
            public void onCallback(ICard<String> obj) {
                final int localID = obj.getLocalID();
                CardManager.getInstance().changeCardType(CardManager.getInstance().getCardByLocalID(localID), CardType.DONE, new SimpleCallback<ICard<String>>() {
                    @Override
                    public void onCallback(ICard<String> obj) {
                        CardManager.getInstance().getAllCardsForDoneList(new SimpleCallback<ArrayList<DoneCard>>() {
                            @Override
                            public void onCallback(ArrayList<DoneCard> obj) {
                                assertEquals(obj.get(0).getLocalID(), localID);

                                // also check if card is deleted from origin list
                                CardManager.getInstance().getAllCardsForTODOList(new SimpleCallback<ArrayList<ToDoCard>>() {
                                    @Override
                                    public void onCallback(ArrayList<ToDoCard> obj) {
                                        assertTrue(obj == null || obj.size() == 0);
                                    }
                                });
                            }
                        });
                    }
                });


            }
        });
    }

    /**
     * Test name and description change
     */
    public void testNameChange() {
        final String title = "title";
        final String title2 = "title2";
        final String desc = "desc";
        final String desc2 = "desc2";
        CardType type = CardType.TODO;
        ((CardManager) CardManager.getInstance()).setContext(getContext());

        // clear all database
        Realm realm = Realm.getInstance(getContext());
        realm.beginTransaction();
        realm.clear(RealmCard.class);
        realm.commitTransaction();

        CardManager.getInstance().createNewCard(title, desc, type, null, new SimpleCallback<ICard<String>>() {
            @Override
            public void onCallback(ICard<String> obj) {
                final int localID = obj.getLocalID();
                // get local card using getter
                ToDoCard source = (ToDoCard) CardManager.getInstance().getCardByLocalID(localID);
                assertEquals(source.getTitle(), title);
                assertEquals(source.getContent(), desc);
                source.setData(localID, title2, desc2, "");
                CardManager.getInstance().changeCardData(source, new SimpleCallback<ICard<String>>() {
                    @Override
                    public void onCallback(ICard<String> obj) {
                        // now get object from db and check names
                        ToDoCard changedCard = (ToDoCard) CardManager.getInstance().getCardByLocalID(localID);
                        assertEquals(changedCard.getTitle(), title2);
                        assertEquals(changedCard.getContent(), desc2);
                    }
                });
            }
        });
    }
}
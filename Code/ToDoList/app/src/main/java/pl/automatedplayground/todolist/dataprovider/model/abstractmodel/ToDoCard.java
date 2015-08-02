package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;

import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloCard;

public class ToDoCard implements ICard<String>, Serializable {

    protected String mTitle;
    protected String mContent;
    protected String mID;

    // block for creation
    ToDoCard(){

    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getContent() {
        if (TextUtils.isEmpty(mContent))
            return "";
        return mContent;
    }

    @Override
    public CardType getType() {
        return CardType.TODO;
    }

    @Override
    public void setData(String title, String s,String id) {
        mTitle = title;
        mContent = s;
        mID = id;
    }

    @Override
    public String getID(){
        return mID;
    }

    public static ToDoCard createListCard(TrelloCard trelloList) {
        ToDoCard tmp = new ToDoCard();
        tmp.setData(trelloList.getName(),trelloList.getDescription(),trelloList.getId());
        return tmp;
    }
}

package pl.automatedplayground.todolist.dataprovider.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;

import pl.automatedplayground.todolist.dataprovider.model.api.model.TrelloList;

public class ToDoCard implements ICard<String>, Serializable {

    @SerializedName("Title")
    protected String mTitle;
    @SerializedName("Content")
    protected String mContent;
    @SerializedName("Time")
    protected DateTime mDate;
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
    public DateTime getTimeInserted() {
        return mDate;
    }

    @Override
    public String getTimeInsertedReadable() {
        if (mDate != null)
            return DateTimeFormat.forPattern("hh:mm dd/MM/yyyy").print(mDate);
        return "";
    }

    @Override
    public CardType getType() {
        return CardType.TODO;
    }

    @Override
    public void setData(String title, String s, String dateTime,String id) {
        mTitle = title;
        mContent = s;
        if (dateTime!=null)
            mDate = DateTimeFormat.forPattern("hh:mm dd/MM/yyyy").parseDateTime(dateTime);
        else
            mDate = new DateTime();
        mID = id;
    }

    @Override
    public String getID(){
        return mID;
    }

    public static ToDoCard createListCard(TrelloList trelloList) {
        ToDoCard tmp = new ToDoCard();
        tmp.setData(trelloList.getName(),"Opis",null,trelloList.getId());
        return tmp;
    }
}

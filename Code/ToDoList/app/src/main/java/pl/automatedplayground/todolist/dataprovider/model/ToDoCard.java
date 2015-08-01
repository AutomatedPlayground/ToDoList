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

public class ToDoCard implements ICard<String>, Serializable {

    @SerializedName("Title")
    protected String mTitle;
    @SerializedName("Content")
    protected String mContent;
    @SerializedName("Time")
    protected DateTime mDate;

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

    public static ToDoCard createMockData(int i) {
        ToDoCard card = new ToDoCard();
        card.mTitle = "Tytuł " + (i + 1);
        card.mContent = "Zawartość";
        card.mDate = new DateTime();
        return card;
    }
}

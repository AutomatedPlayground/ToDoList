package pl.automatedplayground.todolist.dataprovider.model.realmmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import io.realm.RealmObject;

public class RealmCard extends RealmObject {
    private String ID;
    private String title;
    private String content;
    private int type;

    public String getID() {
        return ID;
    }

    public void setID(String mID) {
        this.ID = mID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String mTitle) {
        this.title = mTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String mContent) {
        this.content = mContent;
    }

    public int getType() {
        return type;
    }

    public void setType(int nType) {
        type = nType;
    }
}

package pl.automatedplayground.todolist.dataprovider.model.realmmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCard extends RealmObject {
    @PrimaryKey
    private int localID;
    private String ID;
    private String title;
    private String content;
    private int modified;
    private int type;

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int l) {
        localID = l;
    }

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

    public int getModified(){
        return modified;
    }

    public void setModified(int val){
        modified = val;
    }
}

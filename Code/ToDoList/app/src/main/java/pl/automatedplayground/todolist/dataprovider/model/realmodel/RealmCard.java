package pl.automatedplayground.todolist.dataprovider.model.realmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCard extends RealmObject {
   @PrimaryKey
   private String  ID;
   private String title;
   private String content;
   private String dateTime;
   private int type;

   public String  getID() {
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

   public String getDateTime() {
      return dateTime;
   }

   public void setDateTime(String mDateTime) {
      this.dateTime = mDateTime;
   }

   public int getType(){
      return type;
   }

   public void setType(int nType){
      type = nType;
   }
}

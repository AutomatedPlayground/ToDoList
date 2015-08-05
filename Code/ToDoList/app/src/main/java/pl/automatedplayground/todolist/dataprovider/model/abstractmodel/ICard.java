package pl.automatedplayground.todolist.dataprovider.model.abstractmodel;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public interface ICard<CONTENT> {
    public String getTitle();

    public CONTENT getContent();

    //   public DateTime getTimeInserted();
//   public String getTimeInsertedReadable();
    public CardType getType();

    String getID();

    void setData(int localID, String title, CONTENT content, String webId);

    int getLocalID();

    boolean wasModified();

    void setModified();
}

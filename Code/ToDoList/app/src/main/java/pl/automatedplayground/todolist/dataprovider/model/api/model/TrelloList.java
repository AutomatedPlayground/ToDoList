package pl.automatedplayground.todolist.dataprovider.model.api.model;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 02.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import com.google.gson.annotations.SerializedName;

public class TrelloList {
    @SerializedName("id")
    protected String id;
    @SerializedName("name")
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

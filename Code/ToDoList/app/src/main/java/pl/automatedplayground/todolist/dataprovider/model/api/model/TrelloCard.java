package pl.automatedplayground.todolist.dataprovider.model.api.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 02.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import com.google.gson.annotations.SerializedName;

public class TrelloCard {
    @SerializedName("id")
    protected String id;
    @SerializedName("desc")
    protected String description;
    @SerializedName("name")
    protected String name;
//    @SerializedName("dateLastActivity")
//    private String dateLastActivity;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getDateLastActivity() {
//        return dateLastActivity;
//    }
}

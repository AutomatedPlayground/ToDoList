package pl.automatedplayground.todolist.dataprovider.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import java.util.ArrayList;

import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;

public interface SimpleDataProvider<DATATYPE> {
    ArrayList<DATATYPE> getDataForView();

    void refreshData(SimpleCallback<ArrayList<DATATYPE>> simpleCallback);
}

package pl.automatedplayground.todolist.base;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import pl.automatedplayground.todolist.dataprovider.model.CardFactory;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;

public class ToDoListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        ((CardFactory) CardFactory.getInstance()).setContext(this);
        NetworkCardProvider.getInstance().initWithContext(this);
    }
}
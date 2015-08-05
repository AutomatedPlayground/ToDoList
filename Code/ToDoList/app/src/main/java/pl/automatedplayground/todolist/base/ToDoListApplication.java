package pl.automatedplayground.todolist.base;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.CardManager;
import pl.automatedplayground.todolist.dataprovider.model.api.NetworkCardProvider;

public class ToDoListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        ((CardManager) CardManager.getInstance()).setContext(this);
        ((NetworkCardProvider)NetworkCardProvider.getInstance()).initWithContext(this);
    }
}

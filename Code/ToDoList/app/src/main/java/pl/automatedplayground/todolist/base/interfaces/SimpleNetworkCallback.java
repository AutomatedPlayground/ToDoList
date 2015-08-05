package pl.automatedplayground.todolist.base.interfaces;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 05.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public interface SimpleNetworkCallback<CALLBACKOBJ> extends SimpleCallback<CALLBACKOBJ> {
    void onError();
}

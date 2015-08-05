package pl.automatedplayground.todolist.dataprovider.model.api;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 05.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;

public interface SimpleNetworkCallback<CALLBACKOBJ> extends SimpleCallback<CALLBACKOBJ>{
   void onError();
}

package pl.automatedplayground.todolist.base.interfaces;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 05.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import pl.automatedplayground.todolist.dataprovider.model.api.SimpleNetworkCallback;

public interface NetworkCardProviderInterface {

   /**
    * Synchronize all cards to server
    * @param callback
    */
   public void synchronizeCards(SimpleNetworkCallback<String> callback);

   /**
    * Synchronize all cards from server - should be called only once in app lifetime
    * @param callback
    */
   public void initialSyncFromServer(SimpleNetworkCallback<String> callback);
}

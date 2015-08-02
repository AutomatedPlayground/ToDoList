package pl.automatedplayground.todolist.fragments;/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

import android.view.View;

import java.util.ArrayList;

import pl.automatedplayground.todolist.base.interfaces.SimpleCallback;
import pl.automatedplayground.todolist.base.interfaces.SimpleDataProvider;
import pl.automatedplayground.todolist.configuration.ConfigFile;
import pl.automatedplayground.todolist.dataprovider.model.abstractmodel.DoingCard;

public class DoingListFragment extends ACardListFragment<DoingCard,SimpleDataProvider<DoingCard>> implements SimpleDataProvider<DoingCard> {
   @Override
   protected View.OnClickListener createOnAddClicked() {
      return new View.OnClickListener() {
         @Override
         public void onClick(View v) {

         }
      };
   }

   @Override
   protected boolean isAddAvaiable() {
      return ConfigFile.SHOW_ADDBUTTON_ON_ALLVIEWS;
   }

   @Override
   protected SimpleDataProvider<DoingCard> createDataProvider() {
      return this;
   }

   @Override
   public void onCallback(DoingCard obj) {

   }

   @Override
   public ArrayList<DoingCard> getDataForView() {
      return null;
   }

   @Override
   public void refreshData(SimpleCallback<ArrayList<DoingCard>> simpleCallback) {

   }
}

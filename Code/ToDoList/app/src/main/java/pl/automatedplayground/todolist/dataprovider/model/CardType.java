package pl.automatedplayground.todolist.dataprovider.model;
/*
   Created by Adrian Skupień (automatedplayground@gmail.com) on 01.08.15.
   Copyright (c) 2015 Automated Playground under Apache 2.0 License
*/

public enum CardType {
    TODO, DOING, DONE;

    public int toInt() {
        switch (this) {
            case DOING:
                return 1;
            case DONE:
                return 2;
            case TODO:
            default:
                return 0;
        }
    }

    public static CardType fromId(int type) {
        if (type==1)
            return DOING;
        if (type==2)
            return DONE;
        return TODO;
    }
}

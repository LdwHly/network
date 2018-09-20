package com.ldw.network.api;

/**
 * Created by LDW on 2018/9/20.
 */

public interface DataSourse {
    String getSourse(String sourse);

    String getKeyCode();

    String getKeyData();

    String getKeyMsg();

    int isSucc();
}

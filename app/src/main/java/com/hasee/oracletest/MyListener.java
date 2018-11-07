package com.hasee.oracletest;

import net.sf.json.JSONArray;

public interface MyListener {
    void sendMessageToServer(String message);
    void sendMessage(JSONArray jsonArray);
}

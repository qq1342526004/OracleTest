package com.hasee.onlinedb;

import net.sf.json.JSONArray;

public interface MyListener {
    void sendMessageToServer(String message);
    void sendMessage(JSONArray jsonArray);
}

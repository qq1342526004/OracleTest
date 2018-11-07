package com.hasee.oracletest;

import net.sf.json.JSONObject;

public interface MyListener {
    void sendMessageToServer(String message);
    void sendMessage(JSONObject jsonObject);
}

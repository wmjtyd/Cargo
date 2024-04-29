package main.util;

import com.alibaba.fastjson.JSONObject;

public class Response {
    public static JSONObject msg(boolean success, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", success);
        jsonObject.put("msg", msg);
        if (data == null)
            data = new JSONObject();
        jsonObject.put("data", data);
        return jsonObject;
    }
}

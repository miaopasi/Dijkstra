/**
 * Json���������������
 *   _   _   _   _   _   _   _   _   _   _ 
 *  / \ / \ / \ / \ / \ / \ / \ / \ / \ / \
 * ( C | o | d | i | n | g | F | i | s | h )
 *  \_/ \_/ \_/ \_/ \_/ \_/ \_/ \_/ \_/ \_/ 
 *
 * @Author CodingFish
 * @Email zhaoyongchun@pancou.com
 * 2013���12���10���
 **/
//package com.zcool.community.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParseUtil {

    public Map<String, Object> parse(String jsonStr) {

        Map<String, Object> result = null;

        if (null != jsonStr) {
            try {

                JSONObject jsonObject = new JSONObject(jsonStr);
                result = parseJSONObject(jsonObject);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    private Object parseValue(Object inputObject) throws JSONException {
        Object outputObject = null;

        if (null != inputObject) {

            if (inputObject instanceof JSONArray) {
                outputObject = parseJSONArray((JSONArray) inputObject);
            } else if (inputObject instanceof JSONObject) {
                outputObject = parseJSONObject((JSONObject) inputObject);
            } else if (inputObject instanceof String || inputObject instanceof Boolean || inputObject instanceof Integer) {
                outputObject = inputObject;
            }

        }
        return outputObject;
    }

    private List<Object> parseJSONArray(JSONArray jsonArray) throws JSONException {

        List<Object> valueList = null;

        if (null != jsonArray) {
            valueList = new ArrayList<Object>();

            for (int i = 0; i < jsonArray.length(); i++) {
                Object itemObject = jsonArray.get(i);
                if (null != itemObject) {
                    valueList.add(parseValue(itemObject));
                }
            }
        }

        return valueList;
    }

    private Map<String, Object> parseJSONObject(JSONObject jsonObject) throws JSONException {

        Map<String, Object> valueObject = null;
        if (null != jsonObject) {
            valueObject = new HashMap<String, Object>();

            Iterator<String> keyIter = jsonObject.keys();
            while (keyIter.hasNext()) {
                String keyStr = keyIter.next();
                Object itemObject = jsonObject.opt(keyStr);
                if (null != itemObject) {
                    valueObject.put(keyStr, parseValue(itemObject));
                }

            }
        }

        return valueObject;
    }
}
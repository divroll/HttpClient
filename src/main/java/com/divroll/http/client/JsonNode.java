package com.divroll.http.client;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonNode {

    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private boolean array;

    public JsonNode(String json) {
        if (json == null || "".equals(json.trim())) {
            jsonObject = new JSONObject(JSONParser.parseStrict(json).isObject());
        } else {
            try {
                jsonObject = new JSONObject(JSONParser.parseStrict(json).isObject());
            } catch (JSONException e) {
                // It may be an array
                try {
                    jsonArray = new JSONArray(JSONParser.parseStrict(json).isArray());
                    array = true;
                } catch (JSONException e1) {
                    throw new RuntimeException(e1);
                }
            }
        }
    }

    public JSONObject getObject() {
        return this.jsonObject;
    }

    public JSONArray getArray() {
        JSONArray result = this.jsonArray;
        if (array == false) {
            result = new JSONArray();
            result.put(this.jsonObject);
        }
        return result;
    }

    public boolean isArray() {
        return this.array;
    }

    @Override
    public String toString() {
        if (isArray()) {
            if (jsonArray == null)
                return null;
            return jsonArray.toString();
        }
        if (jsonObject == null)
            return null;
        return jsonObject.toString();
    }

}

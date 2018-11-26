/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2018, Divroll, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.divroll.http.client;

import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
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

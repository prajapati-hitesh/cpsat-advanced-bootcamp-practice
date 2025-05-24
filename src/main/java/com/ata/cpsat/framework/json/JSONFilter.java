package com.ata.cpsat.framework.json;

public class JSONFilter {

    public JSONFilter() {
        // To do
    }

    /**
     * To Filter Suplied Json Array By Key-Value match
     *
     * @param jsonAsArray JSONArray
     * @param key         String
     * @param value       String
     * @return JSONArray
     */
    public JSONArray filterByValue(JSONArray jsonAsArray, String key, String value) {

        final JSONArray[] filteredJsonArray = {new JSONArray()};

        try {
            jsonAsArray.forEach(object -> {
                JSONObject jsonObject = (JSONObject) object;

                if (jsonObject.has(key)) {
                    // If key's value of JSONObject matches expected value
                    if (jsonObject.get(key).toString().trim().toUpperCase().equals(value.trim().toUpperCase())) {
                        filteredJsonArray[0].put(jsonObject);
                    }
                } else {
                    filteredJsonArray[0] = jsonAsArray;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return filteredJsonArray[0];
    }
}

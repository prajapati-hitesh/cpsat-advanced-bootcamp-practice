package com.ata.cpsat.framework.json;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONMerger {

    /**
     * Merges given JSONObjects. Values for identical key names are merged
     * if they are objects, otherwise replaced by the latest occurence.
     *
     * @param listOfJsonObject JSONObjects to merge.
     * @return Merged JSONObject.
     */
    public JSONObject mergeAndOverwriteJsonObjectOnSimilarKeys(@NotNull List<JSONObject> listOfJsonObject) {

        JSONObject merged = new JSONObject();
        Object parameter;

        for (JSONObject added : listOfJsonObject) {

            for (String key : toStringArrayList(added.names())) {
                try {

                    parameter = added.get(key);

                    if (merged.has(key)) {
                        // Duplicate key found:
                        if (added.get(key) instanceof JSONObject) {
                            // Object - allowed to merge:
                            parameter =
                                    mergeAndOverwriteJsonObjectOnSimilarKeys(
                                            Arrays.asList(new JSONObject[]{
                                                    (JSONObject) merged.get(key),
                                                    (JSONObject) added.get(key)}));
                        }
                    }

                    // Add or update value on duplicate key:
                    merged.put(
                            key,
                            parameter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return merged;
    }

    /**
     * Merges given JSONObjects. Values for identical key names are merged
     * if they are objects.
     *
     * @param listOfJsonObject
     * @return
     */
    public JSONObject mergeJSONObjectsOnSimilarKeys(@NotNull List<JSONObject> listOfJsonObject) {

        JSONObject merged = new JSONObject();
        Object parameter;

        for (JSONObject added : listOfJsonObject) {

            for (String key : toStringArrayList(added.names())) {
                try {

                    parameter = added.get(key);

                    if (merged.has(key)) {
                        // Duplicate key found:
                        if (added.get(key) instanceof JSONObject) {
                            // Object - allowed to merge:
                            mergeJSONObjectsOnSimilarKeys(
                                    Arrays.asList(new JSONObject[]{
                                            (JSONObject) merged.get(key),
                                            (JSONObject) added.get(key)}));
                        } else if (added.get(key) instanceof JSONArray) {
                            added.getJSONArray(key).forEach(rowInArray -> {
                                merged.getJSONArray(key).put(rowInArray);
                            });
                        }
                    } else {
                        // Add or update value on duplicate key:
                        merged.put(
                                key,
                                parameter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return merged;
    }

    /**
     * Convert JSONArray to ArrayList<String>.
     *
     * @param jsonArray Source JSONArray.
     * @return Target ArrayList<String>.
     */
    private ArrayList<String> toStringArrayList(@NotNull JSONArray jsonArray) {

        ArrayList<String> stringArray = new ArrayList<String>();
        int arrayIndex;

        for (
                arrayIndex = 0;
                arrayIndex < jsonArray.length();
                arrayIndex++) {

            try {
                stringArray.add(
                        jsonArray.getString(arrayIndex));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return stringArray;
    }
}

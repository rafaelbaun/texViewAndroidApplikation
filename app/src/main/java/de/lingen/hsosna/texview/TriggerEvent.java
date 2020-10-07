package de.lingen.hsosna.texview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class TriggerEvent {

    private String type;
    private String schema;
    private String table;
    private JSONArray affectedRows;
    private JSONArray affectedColumns;
    private long timestamp;
    private int nextPosition;


    public TriggerEvent (String type, String schema, String table, JSONArray affectedRows,
                         JSONArray affectedColumns, long timestamp, int nextPosition) {
        this.type            = type;
        this.schema          = schema;
        this.table           = table;
        this.affectedRows    = affectedRows;
        this.affectedColumns = affectedColumns;
        this.timestamp       = timestamp;
        this.nextPosition    = nextPosition;
    }

    public String getType () {
        return type;
    }

    public String getSchema () {
        return schema;
    }

    public String getTable () {
        return table;
    }

    public JSONArray getAffectedRows () {
        return affectedRows;
    }

    public JSONArray getAffectedColumns () {
        return affectedColumns;
    }

    public long getTimestamp () {
        return timestamp;
    }

    public int getNextPosition () {
        return nextPosition;
    }

    public JSONObject getAfterObject() throws JSONException {
        return (JSONObject) affectedRows.getJSONObject(0).get("after");
    }

    public JSONObject getBeforeObject() throws JSONException {
        return (JSONObject) affectedRows.getJSONObject(0).get("before");
    }

}

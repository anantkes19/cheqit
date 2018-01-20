package devops.colby.cheqit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bybsn on 1/20/2018.
 */

public interface userObject {
    void setAttributes (JSONObject jsonString) throws JSONException;
    Object getJSONObject();
}
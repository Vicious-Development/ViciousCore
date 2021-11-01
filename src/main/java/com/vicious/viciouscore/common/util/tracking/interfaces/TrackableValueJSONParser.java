package com.vicious.viciouscore.common.util.tracking.interfaces;

import com.vicious.viciouscore.common.util.tracking.values.TrackableObject;
import org.json.JSONObject;

public interface TrackableValueJSONParser<T> {
    T parse(JSONObject jo, TrackableObject<T> track) throws Exception;
}

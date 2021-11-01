package com.vicious.viciouscore.common.util.tracking.interfaces;

import com.vicious.viciouscore.common.util.tracking.values.TrackableValue;

public interface TrackableValueConverter {
    void convert(TrackableValue<?> trackableValue);
}

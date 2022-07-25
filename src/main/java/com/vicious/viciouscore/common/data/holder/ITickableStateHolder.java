package com.vicious.viciouscore.common.data.holder;

import com.vicious.viciouscore.common.data.implementations.SyncableTickState;

public interface ITickableStateHolder extends ISyncableCompoundHolder{
    SyncableTickState getTickState();
}

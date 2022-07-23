package com.vicious.viciouscore.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class VCDataSyncHandler {
    private static VCDataSyncHandler instance;
    public static VCDataSyncHandler getInstance(){
        if(instance == null) instance = new VCDataSyncHandler();
        return instance;
    }

    private final List<SyncableData> datas = new ArrayList<>();
    private final Stack<Integer> options = new Stack<>();
    private VCDataSyncHandler(){
        options.push(0);
    }
    public void createData(){
        SyncableData sync = new SyncableData();
        sync.instanceID = options.pop();
        if(!options.contains(sync.instanceID+1)){
            options.push(sync.instanceID+1);
        }
    }
    public void purgeData(SyncableData dat){
        datas.set(dat.instanceID,null);
        options.add(0,dat.instanceID);
    }
    public void insert(SyncableData sync){
        datas.set(sync.instanceID,sync);
    }

    public SyncableData get(int instanceID) {
        return datas.get(instanceID);
    }
}

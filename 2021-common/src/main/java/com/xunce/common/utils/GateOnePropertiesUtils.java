package com.xunce.common.utils;

public class GateOnePropertiesUtils extends PropertyReader {

    private static volatile GateOnePropertiesUtils instance;

    private GateOnePropertiesUtils(String filepath) {
        super(filepath);
    }

    public static GateOnePropertiesUtils getInstance() {
        if (instance == null) {
            synchronized (GateOnePropertiesUtils.class){
                if(instance == null){
                    instance = new GateOnePropertiesUtils("gateone.properties");
                }
            }
        }
        return instance;
    }
}

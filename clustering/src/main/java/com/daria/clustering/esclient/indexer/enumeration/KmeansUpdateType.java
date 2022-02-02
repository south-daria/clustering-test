package com.daria.clustering.esclient.indexer.enumeration;

public enum KmeansUpdateType {
    UPDATE_KMEANS("update-kmeans");

    private final String scriptName;

    KmeansUpdateType(String scriptName) {
        this.scriptName = scriptName;
    }

    public String getScriptName() {
        return scriptName;
    }

}

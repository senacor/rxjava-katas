package com.senacor.tecco.reactive.concurrency.model;

/**
 * Created by mmenzel on 27.01.2016.
 */
public class PlaneInfo{
    public String typeName;
    public int numberBuild;

    public PlaneInfo() {
        this.typeName = "";
        this.numberBuild = 0;
    }

    public PlaneInfo(String typeName, int numberBuild) {
        this.typeName = typeName;
        this.numberBuild = numberBuild;
    }

    public void appendTypeName(String name){
        this.typeName += name;
    }

}

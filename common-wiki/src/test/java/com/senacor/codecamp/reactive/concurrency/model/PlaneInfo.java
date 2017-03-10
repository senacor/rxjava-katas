package com.senacor.codecamp.reactive.concurrency.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by mmenzel on 27.01.2016.
 */
public class PlaneInfo {
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

    public void appendTypeName(String name) {
        this.typeName += name;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("typeName", typeName)
                .append("numberBuild", numberBuild)
                .toString();
    }
}

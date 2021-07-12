package com.read;

public class JsonDTO {
    private String sourcetable;
    private String column;
    private String targetPath;
    private String dataType;
    private String container;

    public JsonDTO() {

    }

    public void setSourcetable(String sourcetable) {

        this.sourcetable = sourcetable;
    }

    public void setColumn(String column) {

        this.column = column;
    }

    public void setTargetPath(String targetPath) {

        this.targetPath = targetPath;
    }

    public void setDataType(String dataType) {

        this.dataType = dataType;
    }

    public void setContainer(String container) {

        this.container = container;
    }

    public String getSourcetable() {

        return sourcetable;
    }

    public String getColumn() {

        return column;
    }

    public String getTargetPath() {

        return targetPath;
    }

    public String getDataType() {

        return dataType;
    }

    public String getContainer() {

        return container;
    }
}

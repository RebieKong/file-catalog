package com.rebiekong.tools.file.catalog.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TranslateRule {
    private StringProperty src;
    private StringProperty containStr;
    private StringProperty dst;
    private BooleanProperty isMove;
    public TranslateRule(String src, String containStr, String dst,Boolean isMove) {
        this.setSrc(src);
        this.setContainStr(containStr);
        this.setDst(dst);
        this.setIsMove(isMove);
    }

    public String getSrc() {
        return srcProperty().get();
    }

    public void setSrc(String value) {
        srcProperty().set(value);
    }

    public StringProperty srcProperty() {
        if (src == null) {
            src = new SimpleStringProperty(this, "src");
        }
        return src;
    }


    public Boolean getIsMove() {
        return isMoveProperty().get();
    }

    public void setIsMove(Boolean value) {
        isMoveProperty().set(value);
    }

    public BooleanProperty isMoveProperty() {
        if (isMove == null) {
            isMove = new SimpleBooleanProperty(this, "isMove");
        }
        return isMove;
    }

    public String getContainStr() {
        return containStrProperty().get();
    }

    public void setContainStr(String value) {
        containStrProperty().set(value);
    }

    public StringProperty containStrProperty() {
        if (containStr == null) {
            containStr = new SimpleStringProperty(this, "containStr");
        }
        return containStr;
    }

    public String getDst() {
        return dstProperty().get();
    }

    public void setDst(String value) {
        dstProperty().set(value);
    }

    public StringProperty dstProperty() {
        if (dst == null) {
            dst = new SimpleStringProperty(this, "dst");
        }
        return dst;
    }
}

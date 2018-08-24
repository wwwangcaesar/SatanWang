package com.lovcreate.core.base;


/**
 * 作者：yuanYe创建于2016/11/14
 * QQ：962851730
 * <p>
 * 服务端接口返回实体基本类, 对应后端制定的json串格式
 */
public class BaseBean {
    private String returnState;
    private String returnMsg;
    private String returnData;
    private boolean error;
    private boolean ok;
    private boolean warning;

    public String getReturnState() {
        return returnState;
    }

    public void setReturnState(String returnState) {
        this.returnState = returnState;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isWarning() {
        return warning;
    }

    public void setWarning(boolean warning) {
        this.warning = warning;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "returnState='" + returnState + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", returnData='" + returnData + '\'' +
                ", error=" + error +
                ", ok=" + ok +
                ", warning=" + warning +
                '}';
    }
}

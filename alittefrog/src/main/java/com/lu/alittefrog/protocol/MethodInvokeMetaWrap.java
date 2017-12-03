package com.lu.alittefrog.protocol;

import java.io.Serializable;
import java.util.Map;

public class MethodInvokeMetaWrap implements Serializable {

    private MethodInvokeMeta invokeMeta;
    private Map<Object,Object> attchment;
    private ResultWrap resultWrap;

    public MethodInvokeMetaWrap(MethodInvokeMeta invokeMeta, Map<Object, Object> attchment, ResultWrap resultWrap) {
        this.invokeMeta = invokeMeta;
        this.attchment = attchment;
        this.resultWrap = resultWrap;
    }

    public MethodInvokeMetaWrap() {

    }

    public MethodInvokeMeta getInvokeMeta() {
        return invokeMeta;
    }

    public void setInvokeMeta(MethodInvokeMeta invokeMeta) {
        this.invokeMeta = invokeMeta;
    }

    public Map<Object, Object> getAttchment() {
        return attchment;
    }

    public void setAttchment(Map<Object, Object> attchment) {
        this.attchment = attchment;
    }

    public ResultWrap getResultWrap() {
        return resultWrap;
    }

    public void setResultWrap(ResultWrap resultWrap) {
        this.resultWrap = resultWrap;
    }

    @Override
    public String toString() {
        return "MethodInvokeMetaWrap{" +
                "invokeMeta=" + invokeMeta +
                ", attchment=" + attchment +
                ", resultWrap=" + resultWrap +
                '}';
    }
}

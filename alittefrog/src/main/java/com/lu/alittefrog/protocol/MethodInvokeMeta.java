package com.lu.alittefrog.protocol;

import java.io.Serializable;
import java.util.Arrays;

public class MethodInvokeMeta implements Serializable {

    //类信息
    private Class targetClass;
    //方法名称
    private String methodName;
    //参数类型
    private Class<?>[] parameterTypes;
    //参数
    private Object[] args;

    public MethodInvokeMeta(Class targetClass, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public MethodInvokeMeta() {

    }

    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "MethodInvokeMeta{" +
                "targetClass=" + targetClass +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}

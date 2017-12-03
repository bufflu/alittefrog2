package com.lu.alittefrog.serializer;


import org.springframework.util.SerializationUtils;

import java.io.Serializable;


public class JDKSerializer implements ObjectSerializer {

    public byte[] serializer(Object value) {

        return SerializationUtils.serialize((Serializable) value);
    }

    public Object deserializer(byte[] bytes) {

        return  SerializationUtils.deserialize(bytes);
    }


}

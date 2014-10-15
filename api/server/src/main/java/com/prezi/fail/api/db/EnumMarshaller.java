package com.prezi.fail.api.db;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;

/**
 * I didn't find an easy way to do this in Kotlin 8.11
 */
public class EnumMarshaller implements DynamoDBMarshaller<Enum> {
    @Override
    public String marshall(Enum getterReturnResult) {
        return getterReturnResult.name();
    }

    @Override
    public Enum unmarshall(Class<Enum> clazz, String obj) {
        return Enum.valueOf(clazz, obj);
    }
}

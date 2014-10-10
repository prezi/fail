package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller
import com.linkedin.data.template.StringMap
import com.amazonaws.services.dynamodbv2.datamodeling.JsonMarshaller


public class StringMapMarshaller: DynamoDBMarshaller<StringMap> {
    val jsonMarshaller = JsonMarshaller<Map<String, String>>()

    override fun marshall(getterReturnResult: StringMap?): String? =
        jsonMarshaller.marshall(getterReturnResult)

    override fun unmarshall(clazz: Class<StringMap>?, obj: String?): StringMap? =
            StringMap(
                    jsonMarshaller.unmarshall(javaClass<Map<String, String>>(), obj)
            )
}

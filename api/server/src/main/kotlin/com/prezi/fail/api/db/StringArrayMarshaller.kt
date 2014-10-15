package com.prezi.fail.api.db


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller
import com.linkedin.data.template.StringArray
import com.amazonaws.services.dynamodbv2.datamodeling.JsonMarshaller


public class StringArrayMarshaller: DynamoDBMarshaller<StringArray> {
    val jsonMarshaller = JsonMarshaller<Array<String>>()

    override fun marshall(getterReturnResult: StringArray?): String? =
            jsonMarshaller.marshall(getterReturnResult?.copyToArray())

    override fun unmarshall(clazz: Class<StringArray>?, obj: String?): StringArray? =
            StringArray(
                    jsonMarshaller.unmarshall(javaClass<Array<String>>(), obj)?.toList()
            )
}

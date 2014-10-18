package com.prezi.fail.api.db

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.model.KeyType
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType
import com.amazonaws.services.dynamodbv2.util.Tables
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex
import org.slf4j.LoggerFactory
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.model.Projection
import com.amazonaws.services.dynamodbv2.model.ProjectionType

class DBImpl(config: DBConfig = DBConfig()) {
    val logger = LoggerFactory.getLogger(javaClass)!!
    val client = AmazonDynamoDBClient()
    val mapper = DynamoDBMapper(client);

    {
        client.setEndpoint(config.getDynamoDBEndpoint())
        ensureTablesExist()
    }

    public fun loadAllScheduledFailures(): List<DBScheduledFailure> =
            mapper.scan(javaClass<DBScheduledFailure>(), DynamoDBScanExpression()).toList()

    fun getTableName(clazz: Class<*>): String = clazz.getAnnotation(javaClass<DynamoDBTable>()).tableName()
    fun getGsiName(clazz: Class<*>, getterName: String): String = clazz.getMethod(getterName).getAnnotation(javaClass<DynamoDBIndexRangeKey>()).globalSecondaryIndexName()
    fun getFieldName(clazz: Class<*>, getterName: String): String {
        val method = clazz.getMethod(getterName)
        return method.getAnnotation(javaClass<DynamoDBAttribute>())?.attributeName() ?:
                method.getAnnotation(javaClass<DynamoDBHashKey>()).attributeName()
    }

    fun ensureTablesExist() {
        ensureScheduledFailureTableExists()
        ensureRunTableExists()
    }

    fun ensureTableExists(tableName: String, createTableRequest: () -> CreateTableRequest) {
        if (Tables.doesTableExist(client, tableName)) {
            logger.debug("Table ${tableName} exists, not creating")
        } else {
            logger.info("Creating table ${tableName}...")
            client.createTable(createTableRequest().withTableName(tableName))
            logger.debug("Table ${tableName} created")
        }

        logger.info("Waiting for table ${tableName} to become active")
        Tables.waitForTableToBecomeActive(client, tableName)
        logger.info("Table ${tableName} exists and is active")
    }

    fun ensureScheduledFailureTableExists() {
        val clazz = javaClass<DBScheduledFailure>()
        val tableName = getTableName(clazz)

        ensureTableExists(tableName, {
            val hashKey = getFieldName(clazz, "getId")

            CreateTableRequest()
                    .withProvisionedThroughput(
                            ProvisionedThroughput()
                                    .withReadCapacityUnits(1L)
                                    .withWriteCapacityUnits(1L))
                    .withAttributeDefinitions(
                            AttributeDefinition().withAttributeName(hashKey).withAttributeType(ScalarAttributeType.S))
                    .withKeySchema(KeySchemaElement().withAttributeName(hashKey).withKeyType(KeyType.HASH))
        })
    }

    fun ensureRunTableExists() {
        val clazz = javaClass<DBRun>()
        val tableName = getTableName(clazz)

        ensureTableExists(tableName, {
            val primaryHashKey = getFieldName(clazz, "getId")
            val indexName = getGsiName(clazz, "getAt")
            val indexHashKey = getFieldName(clazz, "getScheduledFailureId")
            val indexRangeKey = getFieldName(clazz, "getAt")

            CreateTableRequest()
                    .withProvisionedThroughput(
                            ProvisionedThroughput()
                                    .withReadCapacityUnits(1L)
                                    .withWriteCapacityUnits(1L))
                    .withAttributeDefinitions(
                            AttributeDefinition().withAttributeName(primaryHashKey).withAttributeType(ScalarAttributeType.S),
                            AttributeDefinition().withAttributeName(indexHashKey).withAttributeType(ScalarAttributeType.S),
                            AttributeDefinition().withAttributeName(indexRangeKey).withAttributeType(ScalarAttributeType.N))
                    .withKeySchema(KeySchemaElement().withAttributeName(primaryHashKey).withKeyType(KeyType.HASH))
                    .withGlobalSecondaryIndexes(
                            GlobalSecondaryIndex()
                                    .withIndexName(indexName)
                                    .withKeySchema(
                                            KeySchemaElement().withAttributeName(indexHashKey).withKeyType(KeyType.HASH),
                                            KeySchemaElement().withAttributeName(indexRangeKey).withKeyType(KeyType.RANGE))
                                    .withProvisionedThroughput(
                                            ProvisionedThroughput()
                                                    .withReadCapacityUnits(1L)
                                                    .withWriteCapacityUnits(1L))
                                    .withProjection(Projection().withProjectionType(ProjectionType.ALL))
                    )
        })
    }

}

val DB = DBImpl()

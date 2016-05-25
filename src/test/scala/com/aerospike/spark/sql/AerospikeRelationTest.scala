package com.aerospike.spark.sql

import com.aerospike.client.AerospikeClient
import com.aerospike.client.Bin
import com.aerospike.client.Key
import com.aerospike.helper.query.QueryEngine
import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.DataFrame
import com.aerospike.client.Value
import org.apache.spark.sql.SaveMode

class AerospikeRelationTest extends FlatSpec {
  var client: AerospikeClient = _
  var conf: SparkConf = _
  var sc:SparkContext = _
  var sqlContext: SQLContext = _
  var thingsDF: DataFrame = _
  
  val TEST_COUNT = 100

  behavior of "Aerospike Relation"
  
  it should " create test data" in {
    client = AerospikeConnection.getClient("localhost", 3000)
    Value.UseDoubleType = true
    for (i <- 1 to TEST_COUNT) {
      val key = new Key("test", "rdd-test", "rdd-test-"+i)
      client.put(null, key,
         new Bin("one", i),
         new Bin("two", "two:"+i),
         new Bin("three", i.toDouble)
      )
    }
    
  }
  
  it should " create Spark contexts" in {
    conf = new SparkConf().setMaster("local[*]").setAppName("Aerospike RDD Tests")
    sc = new SparkContext(conf)
    sqlContext = new SQLContext(sc)
  }
  
  it should " create an AerospikeRelation" in {
		thingsDF = sqlContext.read.
						format("com.aerospike.spark.sql").
						option("aerospike.seedhost", "127.0.0.1").
						option("aerospike.port", "3000").
						option("aerospike.namespace", "test").
						option("aerospike.set", "rdd-test").
						load 
  }
  
  it should " print the schema" in {
		thingsDF.printSchema()
  }

  it should " select the data using scan" in {
		val result = thingsDF.take(50)
		result.foreach(println(_))
  }
  
//  it should " select the data using filter on 'one'" in {
//		thingsDF.registerTempTable("things")
//		val filteredThings = sqlContext.sql("select * from things where one = 55")
//		filteredThings.foreach(println(_))
//  }

  it should " save with overwrite" in {
      thingsDF.write.
        mode(SaveMode.Overwrite).
        format("com.aerospike.spark.sql").
        option("aerospike.seedhost", "127.0.0.1").
						option("aerospike.port", "3000").
						option("aerospike.namespace", "test").
						option("aerospike.set", "rdd-test").
						option("aerospike.updateByDigest", "__digest").
        save()                
  }

  it should " save with ignore" in {
      thingsDF.write.
        mode(SaveMode.Ignore).
        format("com.aerospike.spark.sql").
        option("aerospike.seedhost", "127.0.0.1").
						option("aerospike.port", "3000").
						option("aerospike.namespace", "test").
						option("aerospike.set", "rdd-test").
						option("aerospike.updateByDigest", "__digest").
        save()                
  }

  it should " save with append" in {
      thingsDF.write.
        mode(SaveMode.Append).
        format("com.aerospike.spark.sql").
        option("aerospike.seedhost", "127.0.0.1").
						option("aerospike.port", "3000").
						option("aerospike.namespace", "test").
						option("aerospike.set", "rdd-test").
						option("aerospike.updateByDigest", "__digest").
        save()                
  }

  it should " delete the test data" in {
    client = AerospikeConnection.getClient("localhost", 3000)

    for (i <- 1 to TEST_COUNT) {
      val key = new Key("test", "rdd-test", "rdd-test-"+i)
      client.delete(null, key)
    }
    
  }

}

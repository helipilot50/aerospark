package com.sqream.spark.examples;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class FlightsParquetToJSON 
{
	private static final String dataPath = "../../../data/parquet/flights";
	private static transient Logger log = LoggerFactory.getLogger(FlightsParquetToJSON.class);
	public static void main(String[] args) {
		FlightsParquetToJSON app = new FlightsParquetToJSON();
		app.start();
	}

	private boolean start() {
		log.info("Ingest flights from " + dataPath);
		SparkSession spark = SparkSession.builder()
				.appName("Parquet to SQream")
				.master("local[*]").getOrCreate();

		Dataset<Row> parquetDf = spark.read()
				.format("parquet")
				.option("extensions", "parquet")
				.parquet(dataPath);//.where("Year = 2000").where("Month = 11").where("DayofMonth = 11").where("UniqueCarrier = 'UA'");

		log.info(parquetDf.count() + " flights in " + dataPath);
		parquetDf.printSchema();
		parquetDf.show(5);
		
		parquetDf.write().json("../../../data/json/flights");
		
		return true;
				
	}

}

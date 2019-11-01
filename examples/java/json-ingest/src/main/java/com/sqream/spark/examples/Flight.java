package com.sqream.spark.examples;
import org.apache.spark.sql.types.*;
public class Flight {
	public static StructType schema(){
		return new StructType(new StructField[]{
				new StructField("Year", DataTypes.	LongType, true, Metadata.empty()), 
				new StructField("Month", DataTypes.	LongType, true, Metadata.empty()), 
				new StructField("DayofMonth", DataTypes.LongType, true, Metadata.empty()),
				new StructField("DayOfWeek", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("DepTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("CRSDepTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("ArrTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("CRSArrTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("UniqueCarrier", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("FlightNum", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("TailNum", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("ActualElapsedTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("CRSElapsedTime", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("AirTime", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("ArrDelay", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("DepDelay", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("Origin", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("Dest", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("Distance", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("TaxiIn", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("TaxiOut", DataTypes.LongType, true, Metadata.empty()), 
				new StructField("Cancelled", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("CancellationCode", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("Diverted", DataTypes.	StringType, true, Metadata.empty()), 
				new StructField("CarrierDelay", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("WeatherDelay", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("NASDelay", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("SecurityDelay", DataTypes.StringType, true, Metadata.empty()), 
				new StructField("LateAircraftDelay", DataTypes.StringType, true, Metadata.empty()), 
		});
	}
	
}

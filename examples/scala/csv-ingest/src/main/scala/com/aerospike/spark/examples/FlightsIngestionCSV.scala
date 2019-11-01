import org.apache.spark.sql.SparkSession
// import com.aerospike.spark.sql._

object FlightsIngestionCSV {
    def main(args: Array[String]) {
        val final String dataPath = "../../../data/csv/flights";
        val spark = SparkSession.builder.appName("Flights Ingest CSV")
            .master("local[*]")
            .getOrCreate()
        val cvsDf = spark.read()
                    .format("csv")
                    .schema(Flight.schema())
                    .option("extensions", "csv")
                    .option("inferSchema", "true")
                    .csv(dataPath).where("Year = 2000").where("Month = 11").where("DayofMonth = 11").where("UniqueCarrier = 'UA'");
        
        cvsDf.createOrReplaceTempView("flights");
        
        val newDF = sqlContext.createDataFrame(cvsDf, schema)
		newDF.withColumn("Key", newDF("Year")+newDF("Month")+newDF("DayofMonth")+newDF("Origin")+newDF("UniqueCarrier")+newDF("FlightNum")) 
        
        cvsDf.printSchema();
        cvsDf.show(5);
        val start = System.currentTimeMillis();
		// Write it to Aerospike
		// newDF.write()
        //     .mode(SaveMode.Append)
        //     .format("com.aerospike.spark.sql")
        //     .option("aerospike.seedhost", "127.0.0.1")
        //     .option("aerospike.port", "3000")
        //     .option("aerospike.namespace", "test")
        //     .option("aerospike.set", "flights")
        //     .option("aerospike.updateByKey", "key")
        //     .save();
		val end = System.currentTimeMillis();
        val time = end-start
		println(s"Flights saved in $time milliseconds")	
        spark.stop()
    }
}
# Spark Java example using SQream DataSource

## How to build
This example uses [Maven](https://maven.apache.org/) as the build tool. This is a very simple example and any build tool will work providing the dependencies are included.

```bash
mvn package
```


## How to run 

Using Maven with the dependencies installed in your local Maven `.m2` repository:
```bash
mvn exec:java
```

## Code discussion

### Creating the DataFrame from Parquet
Create a Spark session and a DataFrame by loading flight data from Parquet files:
```java
SparkSession spark = SparkSession.builder()
	.appName("Parquet to SQream")
	.master("local[*]").getOrCreate();

Dataset<Row> parquetDf = spark.read()
	.format("parquet")
	.option("extensions", "parquet")
	.parquet(dataPath).where("Year = 2000").where("Month = 11");//.where("DayofMonth = 11").where("UniqueCarrier = 'UA'");
```
Note that the filters reduce the number of rows, if you omit all filters you will be loading 123 million rows into the DataFrame.
### Inspecting the DataFrame
Printing the schema of the DataFrame to the console
```java
parquetDf.printSchema();
```
Printing 5 rows and the number of rows in the DataFrame to the console
```bash
parquetDf.show(5);
System.out.println("Total:" + parquetDf.count());
```

### Saving a DataFrame to SQream

The SQream DataSource is Write Only in the first release. This means that you can save a DataFrame to SQream, but not read data from SQream into a DataFrame. Future releases will provide this feature.

This code saves the contents of the DataFrame to SQream:
```bash
parquetDf.write()
	.mode(SaveMode.Append)
	.format("com.sqream.spark.sql")
	.option("sqream.host", "31.154.184.250")
	.option("sqream.port", 5000)
	.option("sqream.user", "sqream")
	.option("sqream.password", "sqream")
	.option("sqream.database", "master")
	.option("sqream.table", "spark.flights")
	.option("sqream.batch.insert.size", 10000)
	.save();
```
### SQream options
Saving to scream requires you to provide a number of options:

Option | Description
-------|------------
SaveMode|This is the standard Spark SaveMode - in this release only these save modes are supported: Append, OverWrite
sqream.host|Host name for the SQream instance or cluster
sqream.port|Port for the SQream instance or cluster
sqream.database|SQream database name
sqream.user|SQream user name
sqream.password|SQream user password
sqream.table|SQream database table, it can include the schema
sqream.batch.insert.size|Sqream inserts rows in a batch, this option specifies the size of the batch, if omitted the default batch size is `1000`
sqream.error.mode|Specifies what to do if there is an error on insert, the default is `sqream.reject.row`

### Error mode

Mode|Description
----|-----------
sqream.reject.row|Abort insert operation of current row & continue to next row   
sqream.abort.all|Abort all operations and exit   
sqream.abort.spark.job|Abort current Spark job, All existing / planned Spark’s jobs continue as planned
sqream.abort.save|Abort current save operation & continue to next data frame
 

### Dependent Jars (current versions)
- sqream-datasource-0.1.0.jar
- sqream-jdbc-2.3.1


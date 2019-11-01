# Spark REPL example using SQream DataSource

## How to run Spark REPL
Launch the Spark shell with dependent JARS: 
```bash
spark-shell --jars sqream-datasource-<version>.jar,sqream-jdbc-<version>.jar
```
and see the REPL output:
```bash
Spark context available as 'sc' (master = local[*], app id = local-1525029843968).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.3.0
      /_/

Using Scala version 2.11.8 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_121)
Type in expressions to have them evaluated.
Type :help for more information.

scala>
```
Create a DataFrame by loading flight data from Parquet files:

```bash
scala> val parquetDf = spark.read.format("parquet").option("extensions", "parquet").parquet("../../data/parquet/flights").where("Year = 2000").where("Month = 11")
```
Note: if you omit all filters, the DataFrame will contain 123 million rows.

The result:
```bash
parquetDf: org.apache.spark.sql.Dataset[org.apache.spark.sql.Row] = [Year: bigint, Month: bigint ... 27 more fields]
```
To print the schema of the DataFrame to the console, use this command:
```bash
parquetDf.printSchema()
```
To print the number of rows in the DataFrame to the console, use this command:
```bash
parquetDf.count()
```

## Saving a DataFrame to SQream

The SQream DataSource is is Write Only in the first release. This means that you can save a DataFrame to SQream, but not read data from SQream into a DataFrame. Future releases will provide this feature.

This command will save the contents of the DataFrame to SQream:
```bash
scala> parquetDf.write.mode("append").format("com.sqream.spark.sql").option("sqream.host", "<your host address>").option("sqream.port", 5000).option("sqream.user", "<sqream user name>").option("sqream.password", "<sqream password>").option("sqream.database", "master").option("sqream.table", "spark.flights").option("sqream.batch.insert.size", 10000).save;

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
- sqream-jdbc-2.3.1 (lean Jar)


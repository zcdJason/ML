package base

import sta.StaFlow

/**
  * spark 中的DataFrame na 填充,如果填充是数字类型，则对字符类型无效；如果填充字符串则对数字类型无效
  * na.drop 会删除所有列中包含null的
  * 测试的目的是在交叉分析的时候出现null 值为填充的情况，原因就是数字类型和字符串类型都有null值，需要分别处理
  */
object SparkDataFrameNaFill extends App{
  val spark = StaFlow.spark
  import spark.implicits._

  var test = StaFlow.loadCSVData("csv", "C:\\NewX\\newX\\ML\\docs\\testData\\base3.csv").withColumn("m1", $"m1".cast("int"))
  test.printSchema()
  test.show()
  val max = test.describe("ad").where($"summary" === "max").first().getAs[String](1)
  val min = test.describe("ad").where($"summary" === "min").first().getAs[String](1)
  println("max:", max)
  println("min:", min)
  /**
    * +---+----+----+----+----+----+----+----+----+---+---------+
    * |d14|day7|  m1|  m3|  m6| m12| m18| m24| m60|age|       ad|
    * +---+----+----+----+----+----+----+----+----+---+---------+
    * |  0|-1.0|   2| 6.0|13.0|42.0|48.0|54.0|  大学| 10|2018/6/19|
    * |  0|-1.0|   2| 6.0|13.0|42.0|48.0|54.0|  大学| 10|2018/6/20|
    * |  0|-1.0|   2| 6.0|13.0|42.0|48.0|54.0|  初中| 10|2018/6/21|
    * |  0| 4.0|   5|12.0|21.0|67.0|73.0|80.0|  初中| 20|2018/6/19|
    * |  0| 4.0|   5|12.0|21.0|67.0|73.0|80.0|  初中| 20|2018/6/22|
    * |  0| 4.0|   5|12.0|21.0|67.0|73.0|80.0|  初中| 20|2018/6/24|
    * |  1| 3.0|  10|25.0|36.0|66.0|68.0|68.0|  大学| 30|2018/6/23|
    * |  1| 3.0|  10|25.0|36.0|66.0|68.0|68.0|  大学| 30|2018/6/26|
    * |  1| 3.0|  10|25.0|36.0|66.0|68.0|68.0|  大学| 30|2018/6/27|
    * |  0|-1.0|null|33.0|33.0|33.0|33.0|null|null| 40|2018/6/29|
    * |  0|-1.0|  16|33.0|33.0|33.0|33.0|null|  博士| 40|2018/6/29|
    * |  0|-1.0|  16|33.0|33.0|33.0|33.0|null|  博士| 40|2018/6/18|
    * |  0|-1.0|  16|33.0|33.0|33.0|33.0|null|  博士| 40|2018/6/18|
    * +---+----+----+----+----+----+----+----+----+---+---------+
    */
  //test.na.drop(Array("f1","f2"))
    test.na.drop().show()
  /**
    * +---+----+---+----+----+----+----+----+---+---+---------+
    * |d14|day7| m1|  m3|  m6| m12| m18| m24|m60|age|       ad|
    * +---+----+---+----+----+----+----+----+---+---+---------+
    * |  0|-1.0|  2| 6.0|13.0|42.0|48.0|54.0| 大学| 10|2018/6/19|
    * |  0|-1.0|  2| 6.0|13.0|42.0|48.0|54.0| 大学| 10|2018/6/20|
    * |  0|-1.0|  2| 6.0|13.0|42.0|48.0|54.0| 初中| 10|2018/6/21|
    * |  0| 4.0|  5|12.0|21.0|67.0|73.0|80.0| 初中| 20|2018/6/19|
    * |  0| 4.0|  5|12.0|21.0|67.0|73.0|80.0| 初中| 20|2018/6/22|
    * |  0| 4.0|  5|12.0|21.0|67.0|73.0|80.0| 初中| 20|2018/6/24|
    * |  1| 3.0| 10|25.0|36.0|66.0|68.0|68.0| 大学| 30|2018/6/23|
    * |  1| 3.0| 10|25.0|36.0|66.0|68.0|68.0| 大学| 30|2018/6/26|
    * |  1| 3.0| 10|25.0|36.0|66.0|68.0|68.0| 大学| 30|2018/6/27|
    * +---+----+---+----+----+----+----+----+---+---+---------+
    */
    test.na.drop(Array("m1")).show()


  val labelCol = "d14"
  test.na.fill("XXXX").show()
  test.na.fill(value = -99, Array("m1", "m60")).show()

  test.withColumn("m1", $"m1".cast("int")).withColumn("m12", $"m12".cast("int")).printSchema()
  //data1.filter("gender<>''").select("gender").limit(10).show
  //data1.filter( data1("gender").isNull ).select("gender").limit(10).show
  //data1.filter("gender is not null").select("gender").limit(10).show
  //ata1.filter("gender is null").select("gender").limit(10).show

  // 删除所有列的空值和NaN
  //val resNull=data1.na.drop()

  //删除某列的空值和NaN
  //val res=data1.na.drop(Array("f1","f2"))

  // 删除某列的非空且非NaN的低于10的
  //data1.na.drop(10,Array("f1","f2"))

  //对指定的列空值填充
  //val res2=data1.na.fill(value="filValue",cols=Array("f1","f2") )
  //val res3=data1.na.fill(Map("f1"->"xxxx","f2"->"rrrr") )

  // //填充所有空值的列
  //val res123=data1.na.fill("wangxiao123")
}

package matrixMulti

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object MatrixMultiplication {
  def main(args: Array[String]): Unit = {
    val spConfig = (new SparkConf).setAppName("Spark Matrix Multiplication").setMaster("local[*]");
    val sc = new SparkContext(spConfig);
    
    val matrixCom = sc.textFile("input_matrices.txt", 3).map(line => line.split(","));
    val matrixA = matrixCom.filter(elem => elem(0) == "A").map(elem => (elem(2).toInt, (elem(1).toInt, elem(3).toInt)));
    val matrixB = matrixCom.filter(elem => elem(0) == "B").map(elem => (elem(1).toInt, (elem(2).toInt, elem(3).toInt)));
    
    val matrixC = matrixA.join(matrixB).map(ele => ((ele._2._1._1, ele._2._2._1), ele._2._1._2*ele._2._2._2)).reduceByKey(_ + _)
      .sortBy(elem => (elem._1._1, elem._1._2)).map(elem => "C" + "," + elem._1._1.toString() + "," +  
        elem._1._2.toString() + "," + elem._2.toString());
    
    matrixC.saveAsTextFile("Matrix-Multiplied1");
    
    sc.stop();
    
  }
}
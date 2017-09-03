package my_package

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.sun.org.apache.xalan.internal.xsltc.compiler.ForEach

class OverlapRegion(shingleLength: Int = 5, processedDocuments: IndexedSeq[(String, Int)]) {
  val documentShingles: Map[Int, Set[String]] = processedDocuments.map {document =>
    val shingles = document._1.toList.sliding(shingleLength, 1).map(_.mkString).toSet
    (document._2, shingles)}.toMap

  val shingleVocab = documentShingles.values.flatten.toSet.toIndexedSeq.zipWithIndex.toMap
  
  val data = documentShingles.map{ elem => 
    val arr = new Array[Double](shingleVocab.size);
    shingleVocab.foreach{ text =>
      if(elem._2.contains(text._1))
        arr(text._2) = 1;
      else
        arr(text._2) = 0;
    }
    arr.toSeq;
  }.toSeq;
  def findOverlap(text1:String, text2:String): List[(String, Int, Int)] = {
    val str_shingles1 = text1.sliding(shingleLength, 1).toSeq;
    val str_shingles2 = text2.sliding(shingleLength, 1).toSeq;
    val indexed_arr1 = new Array[Double](shingleVocab.size);
    val indexed_arr2 = new Array[Double](shingleVocab.size);
    
    shingleVocab.foreach{ele => 
      if(str_shingles1.contains(ele._1))
        indexed_arr1(ele._2) = 1;
      else 
        indexed_arr1(ele._2) = 0;
      if(str_shingles2.contains(ele._1))
        indexed_arr2(ele._2) = 1;
      else 
        indexed_arr2(ele._2) = 0;
    }
    
    var index = List[(String, Int, Int)]();
    
    for(i <- 0 to shingleVocab.size-1){
      if(indexed_arr1(i) == 1 && indexed_arr2(i) == 1){
        val str = shingleVocab.map(_.swap).get(i) match{
          case Some(value) => index = index ++ List((value, text1.indexOf(value), text2.indexOf(value)));
          case None => None;
        }
      }
    }
    
    index;
    
  }
}
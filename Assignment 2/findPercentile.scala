def findPercentile(data: RDD[Int], percentile: Double, count: Int): Int = {
      if(data.count() < 1){
        return -1;
      }
      else{
        val pivot = data.takeSample(false, 1).apply(0);
      
        val filteredData = data.filter(x => x <= pivot);
        if(filteredData.count() + count == percentile){
          return pivot;
        }
        else if(filteredData.count() + count < percentile){
          return findPercentile(data.filter(x => x > pivot), percentile, filteredData.count().toInt);
        }
        else{
          return findPercentile(filteredData, percentile, count);
        }
      }
}
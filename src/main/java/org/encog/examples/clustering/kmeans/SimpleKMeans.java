package org.encog.examples.clustering.kmeans;

import java.util.Arrays;

import org.encog.ml.MLCluster;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.kmeans.KMeansClustering;

public class SimpleKMeans {
	public static final double[][] DATA = {
        {28,15,22},
        {16,15,32},
        {32,20,44},
        {1,2,3},
        {3,2,1} };
	
    public static void main (String args[]){
                
        BasicMLDataSet set = new BasicMLDataSet();
        
        for(int i=0;i<DATA.length;i++)
        {
        	set.add(new BasicMLData(DATA[i]));
        }

        KMeansClustering kmeans = new KMeansClustering(2,set);
        
        kmeans.iteration(100);
        System.out.println("Final WCSS: " + kmeans.getWCSS());
               
        int i = 1;
        for(MLCluster cluster: kmeans.getClusters())
        {
        	System.out.println("*** Cluster " + (i++) + " ***");
        	MLDataSet ds = cluster.createDataSet();
            MLDataPair pair = BasicMLDataPair.createPair(ds.getInputSize(), ds.getIdealSize());
            for(int j=0;j<ds.getRecordCount();j++)
            {
            	ds.getRecord(j, pair);
            	System.out.println(Arrays.toString(pair.getInputArray()));
            	
            }
        }       
    }
}

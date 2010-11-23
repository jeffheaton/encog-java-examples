package org.encog.examples.clustering.kmeans;

import java.util.Arrays;

import org.encog.engine.cluster.kmeans.KMeansCluster;
import org.encog.engine.cluster.kmeans.KMeansClustering;
import org.encog.engine.data.BasicEngineData;
import org.encog.engine.data.EngineData;
import org.encog.engine.data.EngineIndexableSet;
import org.encog.neural.data.basic.BasicNeuralData;
import org.encog.neural.data.basic.BasicNeuralDataSet;

public class SimpleKMeans {
	public static final double[][] DATA = {
        {28,15,22},
        {16,15,32},
        {32,20,44},
        {1,2,3},
        {3,2,1} };
	
    public static void main (String args[]){
                
        BasicNeuralDataSet set = new BasicNeuralDataSet();
        
        for(int i=0;i<DATA.length;i++)
        {
        	set.add(new BasicNeuralData(DATA[i]));
        }

        KMeansClustering kmeans = new KMeansClustering(2,set);
        
        kmeans.iteration(100);
        System.out.println("Final WCSS: " + kmeans.getWCSS());
               
        int i = 1;
        for(KMeansCluster cluster: kmeans.getClusters())
        {
        	System.out.println("*** Cluster " + (i++) + " ***");
        	EngineIndexableSet ds = cluster.createDataSet();
            EngineData pair = BasicEngineData.createPair(ds.getInputSize(), ds.getIdealSize());
            for(int j=0;j<ds.getRecordCount();j++)
            {
            	ds.getRecord(j, pair);
            	System.out.println(Arrays.toString(pair.getInputArray()));
            	
            }
        }       
    }
}

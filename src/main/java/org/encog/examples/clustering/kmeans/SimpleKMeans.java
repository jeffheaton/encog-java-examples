package org.encog.examples.clustering.kmeans;

import java.util.Arrays;

import org.encog.ml.MLCluster;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataPair;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.kmeans.KMeansClustering;

/**
 * This example performs a simple KMeans cluster.  The numbers are clustered
 * into two groups.
 */
public class SimpleKMeans {
	
	/**
	 * The data to be clustered.
	 */
	public static final double[][] DATA = { { 28, 15, 22 }, { 16, 15, 32 },
			{ 32, 20, 44 }, { 1, 2, 3 }, { 3, 2, 1 } };

	/**
	 * The main method.
	 * @param args Arguments are not used.
	 */
	public static void main(final String args[]) {

		final BasicMLDataSet set = new BasicMLDataSet();

		for (final double[] element : SimpleKMeans.DATA) {
			set.add(new BasicMLData(element));
		}

		final KMeansClustering kmeans = new KMeansClustering(2, set);

		kmeans.iteration(100);
		System.out.println("Final WCSS: " + kmeans.getWCSS());

		// Display the cluster
		int i = 1;
		for (final MLCluster cluster : kmeans.getClusters()) {
			System.out.println("*** Cluster " + (i++) + " ***");
			final MLDataSet ds = cluster.createDataSet();
			final MLDataPair pair = BasicMLDataPair.createPair(
					ds.getInputSize(), ds.getIdealSize());
			for (int j = 0; j < ds.getRecordCount(); j++) {
				ds.getRecord(j, pair);
				System.out.println(Arrays.toString(pair.getInputArray()));

			}
		}
	}
}

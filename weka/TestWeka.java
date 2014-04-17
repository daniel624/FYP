package weka;

/*
 * update log
 * 
 * ===== 2014-01-09 =====
 * - add weka RandomForest to help on string fields
 * - use course project data from CSCI 3230 last sem as learning data
 * 
 * ===== 2014-01-13 =====
 * - add length of each field and number of words as testing attributes
 * 
 * ===== 2014-01-23 =====
 * - add 1 more attribute, whether the field contains abbreviated initials in author names 
 * 
 * ===== 2014-01-28 =====
 * - add exact keyword matching (breakdown each type)
 * - length of longest word in a field
 * - preposition mostly appear in title/journal
 * - ':' in title
 * 
 * ===== 2014-03-03 =====
 * - add ordinal number checking for different journal/conference
 * 
 */

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import java.io.*;
import java.util.*;

public class TestWeka {
	private double[] clsLabel;
	private double[][] distribution;
	private int test_size;
	private RandomForest tree = new RandomForest();
	
	public double[] getClassLabel() {
		//System.out.println("get class label\n");
		return clsLabel;
	}
	
	public double[][] getClassDistribution() {
		return distribution;
	}
	
	public int getTestSize() {
		return test_size;
	}
	
	public int[] checkResult() {
		/** check result and test arff **/
    	int[] result = {0,0};
		try {
	    	BufferedReader buf1 = new BufferedReader(new FileReader("webapps/WEB-INF/classes/data/publication-test.arff"));
	    	BufferedReader buf2 = new BufferedReader(new FileReader("webapps/WEB-INF/classes/data/publication-result.arff"));
			//BufferedReader buf1 = new BufferedReader(new FileReader("src/data/publication-test.arff"));
	    	//BufferedReader buf2 = new BufferedReader(new FileReader("src/classes/data/publication-result.arff"));
	    	String in1, in2;
	    	int correct=0, total=0;
	    	
	    	do {
	    		in1 = buf1.readLine();
	    		in2 = buf2.readLine();
	    		total++;
	    		
	    		if (in1!=null && in1.equals(in2)) {
	    			correct++;
	    		} else if (in1!=null && !in1.equals(in2)) {
	    			System.out.println(total + ", " + (total-20));
	    		}
	    	} while (in1!=null);
	    	
	    	//System.out.println(correct +"/"+ total);
	    	//System.out.println((double) correct/total);
	    	result[0] = correct;
	    	result[1] = total;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
	}
	
	public int testMethod() {
		return 12345;
	}
	
	public void buildTree() {
		try {
	    	String filename = "webapps/FYP_UI/WEB-INF/classes/data/publication-train.arff";
			//String filename = "src/data/publication-train.arff";
	    	BufferedReader buf = new BufferedReader(new FileReader(filename));
	    	Instances train = new Instances(buf);
	    	train.setClassIndex(0);

	    	tree.setNumTrees(15);
	    	tree.buildClassifier(train);

	    	Evaluation eval = new Evaluation(train);
	    	eval.crossValidateModel(tree, train, 10, new Random(1));	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runResult_new() {
		try {
			String filename = "webapps/FYP_UI/WEB-INF/classes/data/out-data.arff";
			//String filename = "src/data/out-data.arff";
	    	BufferedReader buf = new BufferedReader(new FileReader(filename));
	    	Instances test = new Instances(buf);
	    	test.setClassIndex(0);
	    	test_size = test.size();
	    	//System.out.println("TestWeka: test_size: " + test_size);
	    	buf.close();
			
			Instances labeled = new Instances(test);
	    	clsLabel = new double[test_size];
	    	distribution = new double[test_size][4];
	    	for (int i=0; i<test_size; i++) {
	    		clsLabel[i] = tree.classifyInstance(test.instance(i));
	    		labeled.instance(i).setClassValue(clsLabel[i]);
	    		distribution[i] = tree.distributionForInstance(test.instance(i));
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void runResult() {
    	try {
	    	String filename;
	    	BufferedReader buf;
	    	//BufferedWriter out = new BufferedWriter(new FileWriter("src/data/test-result.txt"));
	    	
	    	filename = "webapps/FYP_UI/WEB-INF/classes/data/publication-train.arff";
	    	//filename = "src/data/publication-train.arff";
	    	buf = new BufferedReader(new FileReader(filename));
	    	Instances train = new Instances(buf);
	    	train.setClassIndex(0);
	    	//train.setClassIndex(train.numAttributes()-1);
	    	
	    	filename = "webapps/FYP_UI/WEB-INF/classes/data/publication-test.arff";
	    	//filename = "src/data/publication-test.arff";
	    	buf = new BufferedReader(new FileReader(filename));
	    	Instances test = new Instances(buf);
	    	test.setClassIndex(0);
	    	test_size = test.size();
	    	//test.setClassIndex(test.numAttributes()-1);
	    	
	    	buf.close();
	
	    	// 20140211 check importance of each feature
	    	RandomForest tree = new RandomForest();
	    	tree.setNumTrees(15);
	    	tree.buildClassifier(train);
	    	
	    	//exact match / fuzzy match
	    	
	    	// add cross validation
	    	Evaluation eval = new Evaluation(train);
	    	eval.crossValidateModel(tree, train, 10, new Random(1));
	    	eval.evaluateModel(tree, test);
	    	
	    	Instances labeled = new Instances(test);
	    	clsLabel = new double[test_size];
	    	//System.out.println("test size: " + test_size);
	    	distribution = new double[test_size][4];
	    	int flag;
	    	for (int i=0; i<test_size; i++) {
	    		flag = 0;
	    		clsLabel[i] = tree.classifyInstance(test.instance(i));
	    		labeled.instance(i).setClassValue(clsLabel[i]);
	    		distribution[i] = tree.distributionForInstance(test.instance(i));
	    		/*out.write("Instance no. " + (i+1) + "\n");
				out.write("class Author: " + distribution[0] + "\n");
				if (distribution[i][0] >= 0.8) flag = 1;
				out.write("class Title: " + distribution[1] + "\n");
				if (distribution[i][1] >= 0.8) flag = 1;
				out.write("class Journal: " + distribution[2] + "\n");
				if (distribution[i][2] >= 0.8) flag = 1;
				out.write("class Proceeding: " + distribution[3] + "\n");
				if (distribution[i][3] >= 0.8) flag = 1;
	    		out.write("\n");
	    		if (flag==0) System.out.println(i+1);*/
	    	}
	    	
	    	/*filename = "src/data/publication-result.arff";
	    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    	writer.write(labeled.toString());
	    	writer.write("\n");
	    	writer.close();
	    	out.close();*/
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}


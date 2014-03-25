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
 * - add ordinal number checking for different issues
 * 
 */

import weka.classifiers.trees.*;
import java.io.*;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.Evaluation;
import java.util.*;

public class TestWeka {
	private static int correct;
	private static int total;
	
	public int getCorrect() {
		return correct;
	}
	
	public int getTotal() {
		return total;
	}
	
	public static void checkResult() throws FileNotFoundException, IOException{
		/** check result and test arff **/
    	BufferedReader buf1 = new BufferedReader(new FileReader("src/data/publication-test.arff"));
    	BufferedReader buf2 = new BufferedReader(new FileReader("src/data/publication-result.arff"));
    	String in1, in2;
    	correct = 0;
    	total = 0;
    	
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
    	
    	System.out.println(correct +"/"+ total);
    	System.out.println((double) correct/total);
	}
	
    public static void main(String[] args) throws Exception {
    	String filename;
    	BufferedReader buf;
    	BufferedWriter out = new BufferedWriter(new FileWriter("src/data/test-result.txt"));
    	
    	//filename = "src/data/RA-train.arff";
    	filename = "src/data/publication-train.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances train = new Instances(buf);
    	train.setClassIndex(0);
    	//train.setClassIndex(train.numAttributes()-1);
    	
    	//filename = "src/data/RA-test.arff";
    	filename = "src/data/publication-test.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances test = new Instances(buf);
    	test.setClassIndex(0);
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
    	double clsLabel;
    	int flag;
    	double[] distribution = new double[4];
    	for (int i=0; i<test.numInstances(); i++) {
    		flag = 0;
    		clsLabel = tree.classifyInstance(test.instance(i));
    		labeled.instance(i).setClassValue(clsLabel);
    		distribution = tree.distributionForInstance(test.instance(i));
    		out.write("Instance no. " + (i+1) + "\n");
			out.write("class Author: " + distribution[0] + "\n");
			if (distribution[0] >= 0.8) flag = 1;
			out.write("class Title: " + distribution[1] + "\n");
			if (distribution[1] >= 0.8) flag = 1;
			out.write("class Journal: " + distribution[2] + "\n");
			if (distribution[2] >= 0.8) flag = 1;
			out.write("class Proceeding: " + distribution[3] + "\n");
			if (distribution[3] >= 0.8) flag = 1;
    		out.write("\n");
    		//if (flag==0) System.out.println(i+1);
    	}
    	
    	filename = "src/data/publication-result.arff";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(labeled.toString());
    	writer.write("\n");
    	writer.close();
    	out.close();
    }
}


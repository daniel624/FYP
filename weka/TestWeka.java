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
 */

import weka.classifiers.trees.*;
import weka.core.Instances;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import weka.classifiers.Evaluation;
import java.util.*;

public class TestWeka {
    public static void main(String[] args) throws Exception {
    	String filename;
    	BufferedReader buf;
    	
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
    	for (int i=0; i<test.numInstances(); i++) {
    		clsLabel = tree.classifyInstance(test.instance(i));
    		labeled.instance(i).setClassValue(clsLabel);
    	}
    	
    	filename = "src/data/publication-result.arff";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(labeled.toString());
    	writer.write("\n");
    	writer.close();
    	
    	
    	/** check result and test arff **/
    	BufferedReader buf1 = new BufferedReader(new FileReader("src/data/publication-test.arff"));
    	BufferedReader buf2 = new BufferedReader(new FileReader("src/data/publication-result.arff"));
    	String in1, in2;
    	int correct=0, total=0;
    	
    	do {
    		in1 = buf1.readLine();
    		in2 = buf2.readLine();
    		total++;
    		
    		if (in1!=null && in1.equals(in2)) correct++;
    	} while (in1!=null);
    	
    	System.out.println(correct +"/"+ total);
    	System.out.println((double) correct/total);
    }
}


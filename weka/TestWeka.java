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
    	filename = "src/data/train.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances train = new Instances(buf);
    	train.setClassIndex(3);
    	//train.setClassIndex(train.numAttributes()-1);
    	
    	//filename = "src/data/RA-test.arff";
    	filename = "src/data/test.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances test = new Instances(buf);
    	test.setClassIndex(3);
    	//test.setClassIndex(test.numAttributes()-1);
    	
    	buf.close();

    	// 20140211 check importance of each feature
    	RandomForest tree = new RandomForest();
    	tree.setNumTrees(10);
    	tree.buildClassifier(train);
    	
    	//exact match / fuzzy match
    	
    	// add cross validation
    	Evaluation eval = new Evaluation(train);
    	eval.crossValidateModel(tree, train, 10, new Random(1));
    	eval.evaluateModel(tree, test);
    	System.out.println(eval.toSummaryString("\nResults\n======\n", false));
    	System.out.println(eval.confusionMatrix()[0][0] + "\t" + eval.confusionMatrix()[0][1]);
    	System.out.println(eval.confusionMatrix()[1][0] + "\t" + eval.confusionMatrix()[1][1]);
    	
    	Instances labeled = new Instances(test);
    	double clsLabel;
    	for (int i=0; i<test.numInstances(); i++) {
    		clsLabel = tree.classifyInstance(test.instance(i));
    		labeled.instance(i).setClassValue(clsLabel);
    	}
    	
    	filename = "src/data/result.arff";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(labeled.toString());
    	writer.close();
    }
}


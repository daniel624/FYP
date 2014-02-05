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
 * - add exact keyword matching
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
    	
    	//filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/RA-train.arff";
    	filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/train.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances train = new Instances(buf);
    	train.setClassIndex(3);
    	//train.setClassIndex(train.numAttributes()-1);
    	
    	//filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/RA-test.arff";
    	filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/test.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances test = new Instances(buf);
    	test.setClassIndex(3);
    	//test.setClassIndex(test.numAttributes()-1);
    	
    	buf.close();

    	RandomForest tree = new RandomForest();
    	tree.setNumTrees(10);
    	tree.buildClassifier(train);
    	
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
    	
    	filename = "C:/Users/Lenovo/Documents/workspace/FYP/src/data/result.arff";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(labeled.toString());
    	writer.close();
    }
}


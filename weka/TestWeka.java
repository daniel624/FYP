package weka;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.*;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestWeka {
    public static void main(String[] args) throws Exception {
    	String filename;
    	BufferedReader buf;
    	
    	//filename = "C:/Users/Lenovo/Documents/notes/Fall 2013/CSCI 3230/splicing/RA-train.arff";
    	filename = "C:/Users/Lenovo/Desktop/train.arff";;
    	buf = new BufferedReader(new FileReader(filename));
    	Instances train = new Instances(buf);
    	train.setClassIndex(train.numAttributes()-1);
    	
    	//filename = "C:/Users/Lenovo/Documents/notes/Fall 2013/CSCI 3230/splicing/RA-test.arff";
    	filename = "C:/Users/Lenovo/Desktop/test.arff";
    	buf = new BufferedReader(new FileReader(filename));
    	Instances test = new Instances(buf);
    	test.setClassIndex(test.numAttributes()-1);
    	
    	buf.close();

    	RandomForest tree = new RandomForest();
    	tree.setNumTrees(10);
    	tree.buildClassifier(train);
    	
    	Instances labeled = new Instances(test);
    	double clsLabel;
    	for (int i=0; i<test.numInstances(); i++) {
    		clsLabel = tree.classifyInstance(test.instance(i));
    		labeled.instance(i).setClassValue(clsLabel);
    		//System.out.println(clsLabel);
    	}
    	
    	filename = "C:/Users/Lenovo/Desktop/result.arff";
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    	writer.write(labeled.toString());
    	writer.close();
    	//System.out.println(labeled.toString());
    }
}


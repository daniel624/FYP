package weka;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.*;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestWeka {
    public static void main(String[] args) throws Exception {
    	
    	String filename = "C:/Users/Lenovo/Documents/notes/Fall 2013/CSCI 3230/splicing/weka_raw_training2.arff";
    	//String filename = "C:/Users/Lenovo/Documents/notes/Fall 2013/CSCI 3230/weka-3-7-10/data/contact-lenses.arff";
    	BufferedReader buf = new BufferedReader(new FileReader(filename));
    	Instances train = new Instances(buf);
    	train.setClassIndex(train.numAttributes()-1);
    	buf.close();

    	RandomForest tree = new RandomForest();
    	tree.buildClassifier(train);
    	tree.setNumTrees(5);
    	Evaluation eval = new Evaluation(train);
    	eval.evaluateModel(tree, train);
    	System.out.println(eval.toSummaryString("== Results ==", true));
    }
}


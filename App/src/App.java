import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.bayes.NaiveBayesMultinomialUpdateable;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.GaussianProcesses;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.Logistic;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SGD;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.functions.SimpleLinearRegression;
import weka.classifiers.functions.SimpleLogistic;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.trees.HoeffdingTree;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class App {

	public static void main(String[] args) throws Exception {
		
		/*AbstractClassifier[] classifiers = {/*new REPTree(), new RandomTree(),
				new RandomForest(),new LMT(), new J48(), new HoeffdingTree(), new LWL(),
				new KStar(), new IBk(),new MultilayerPerceptron(),/*
				new SMO(), new Logistic(), new SGD(), new SimpleLogistic(),*/
				/*new BayesNet(),*/ /*new NaiveBayes()/*, 
				new NaiveBayesUpdateable()*//*};*/
	
		
		double debut;
		
		List<Double> precisionsTraining;
		List<Double> precisionsCross;
		ArrayList<AbstractClassifier> classifier = new ArrayList<AbstractClassifier>();
		
		/*for (AbstractClassifier a: classifiers)
			classifier.add(a);*/
		
		/*NaiveBayes NB = new NaiveBayes();
		String[] options = new String[1];
		options[0] = "-D";
		NB.setOptions(options);
		classifier.add(NB);*/
		
		/*RandomTree tree = new RandomTree();
		classifier.add(tree);*/
		
		REPTree tree = new REPTree();
		tree.setMaxDepth(10);
		tree.setMinNum(4);
		classifier.add(tree);
		
		/*MultilayerPerceptron layer = new MultilayerPerceptron();
		classifier.add(layer);*/
		
		/*SMO smo = new SMO();
		classifier.add(smo);*/
		
		/*NaiveBayesUpdateable NBU = new NaiveBayesUpdateable();
		classifier.add(NBU);*/
		
		for(int j = 0; j<classifier.size(); j++) {
			
			debut = System.currentTimeMillis();
			precisionsTraining = new ArrayList<Double>();
			precisionsCross = new ArrayList<Double>();
			System.out.println("==========================================");
			System.out.println("Classifier: "+classifier.get(j).toString());
			for(int i = 2008; i<2018; i++) {
				if(i != 2016) {
					System.out.print("Jeu "+i+" is starting");
					DataSource source = new DataSource("/Z:\\FouillesData\\donnees-matchs\\arffs\\"+i+"\\gamesAdvancedStats-StdDevs.arff");
					Instances data = source.getDataSet();
					
					if (data.classIndex() == -1)
						data.setClassIndex(5) ;
					
					Remove filter = new Remove();
					filter.setAttributeIndices("7,9,53");
					filter.setInputFormat(data);
					
					data = Filter.useFilter(data, filter);
					
					filter.setAttributeIndices("4,5,6,7,55,12,50");
					filter.setInvertSelection(true);
					filter.setInputFormat(data);
					
					data = Filter.useFilter(data, filter);
					
					if (data.classIndex() == -1)
						data.setClassIndex(5);
					
					/*NaiveBayes NB = new NaiveBayes();
					String[] options = new String[1];
					options[0] = "-K";
					NB.setOptions(options);*/
					classifier.get(j).buildClassifier(data);
					
					Evaluation eval = new Evaluation(data);
					
					//System.out.println("Training: \n");
					eval.evaluateModel(classifier.get(j), data);
					//System.out.println(eval.toSummaryString());
					//System.out.println(eval.toClassDetailsString());
					precisionsTraining.add(eval.weightedPrecision());
					
					//System.out.println("Cross-Validation: \n");
					eval.crossValidateModel(classifier.get(j), data, 10, new Random(1));
					//System.out.println(eval.toSummaryString());
					//System.out.println(eval.toClassDetailsString());
					precisionsCross.add(eval.weightedPrecision());
					System.out.println(" ----> end");
				}
				
			}
			
			double sum = 0;
			for(double p: precisionsTraining) {
				sum += p;
			}
			
			System.out.println("Average precision Training: "+ sum/((2017-2008)));
			sum = 0;
			
			for(double p: precisionsCross) {
				sum += p;
			}
			System.out.println("Average precision Cross: "+ sum/((2017-2008)));
			
			System.out.println("Average duration: " + ((System.currentTimeMillis()-debut)/3600) + " seconds\n\n");
		}
		
		
		
		

	}

}
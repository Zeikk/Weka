import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.GreedyStepwise;
import weka.attributeSelection.WrapperSubsetEval;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.RandomTree;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class SelectAttribute {
	
	public static void main(String[] args) throws Exception {
	
		ArrayList<int[]> indicesAttributs = new ArrayList<int[]>();
		
		/*NaiveBayes NB = new NaiveBayes();
		String[] options = new String[1];
		options[0] = "-K";
		NB.setOptions(options);*/
		
		ArrayList<AbstractClassifier> classifier = new ArrayList<AbstractClassifier>();
		
		//classifier.add(NB);
		
		/*BayesNet BN = new BayesNet();
		classifier.add(BN);*/
		
		/*REPTree tree = new REPTree();
		classifier.add(tree);*/
		
		/*RandomTree tree = new RandomTree();
		classifier.add(tree);*/
		
		/*J48 treeJ = new J48();
		classifier.add(treeJ);*/
		
		/*SMO smo = new SMO();
		classifier.add(smo);*/
		
		NaiveBayesUpdateable NBU = new NaiveBayesUpdateable();
		classifier.add(NBU);
		
		int seed = 3;
		System.out.println("Seed "+seed+": ");
		for(int j = 0; j<classifier.size(); j++) {
	
			System.out.println("Classifier: "+classifier.get(j).toString());
			
			for(int i = 2008; i<2018; i++) {
				if(i != 2016) {
					DataSource source = new DataSource("/Z:\\FouillesData\\donnees-matchs\\arffs\\"+i+"\\gamesAdvancedStats-StdDevs.arff");
					Instances data = source.getDataSet();
					
					if (data.classIndex() == -1)
						data.setClassIndex(5) ;
					
					Remove filter = new Remove();
					filter.setAttributeIndices("7,9,53");
					filter.setInputFormat(data);
					
					data = Filter.useFilter(data, filter);
					
					System.out.println("Jeu "+i+": ");
					AttributeSelection attributes = new AttributeSelection ();
					WrapperSubsetEval wrapper = new WrapperSubsetEval();
					   
					int[] indices;
					GreedyStepwise search = new GreedyStepwise();
					search.setGenerateRanking(false);
					search.setSearchBackwards(false);
		
					wrapper.setClassifier(classifier.get(j));
		
					attributes.setEvaluator(wrapper);
					attributes.setSearch(search);
					attributes.setFolds(5);
					attributes.setSeed(seed);
		
					attributes.SelectAttributes(data);  
					indices=attributes.selectedAttributes();
					
					indicesAttributs.add(indices);
					System.out.println("END");
					
				}
			}
		}
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int[] indices : indicesAttributs) {
			for(int i: indices) {
				if(map.containsKey(i)) {
					int nbNum = (int) map.get(i);
					map.replace(i, nbNum +1);
				}
				else {
					map.put(i, 1);
				}
			}
		}
		
		Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry mapentry = (Map.Entry) iterator.next();
          System.out.println("Indice: "+mapentry.getKey()
                            + " | Fréquence: " + mapentry.getValue());
        } 
	}
}
package SimpleSearchEngine;

import java.util.*;
import javafx.util.*;

public class SimpleSearchEngine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleSearchEngine search = new SimpleSearchEngine();
		
		ArrayList<String> results = search.runSearchEngine();
		System.out.println("Result: " + results);

	}
	
	private ArrayList<String> runSearchEngine(){
		ArrayList<String> basicCorpus = createCorpus();
		ArrayList<ArrayList<String>> corpus = initializeCorpus(basicCorpus);
		
		ArrayList<String> results = new ArrayList<String>();
		
		Map<String, ArrayList<Integer>> index = createIndex(corpus);
		
		String searchValue = requestSearchValue();
		
		ArrayList<Integer> foundDocuments = index.get(searchValue);
		
		try{
			//System.out.println("The following documents contains " + searchValue + ": " + foundDocuments);
		
			if (foundDocuments.size()>1){
				foundDocuments = sortDocuments(foundDocuments, corpus, searchValue);
				for(int documentIndex:foundDocuments){
					results.add(basicCorpus.get(documentIndex));
				}
		
			} else if(foundDocuments.size() == 1){
				results.add(basicCorpus.get(foundDocuments.get(0)));
			}
		} catch(NullPointerException e){
			System.out.println("No documents found with the word " + searchValue + ".");
		}
		return results;
		
	}
	
	private ArrayList<ArrayList<String>> initializeCorpus(ArrayList<String> basicCorpus){
		ArrayList<ArrayList<String>> corpus = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < basicCorpus.size(); i++){
			corpus.add(initializeDocument(basicCorpus.get(i)));
		}
		
		return corpus;
	}
	
	private ArrayList<String> createCorpus(){
		ArrayList<String> originalCorpus = new ArrayList<String>();
		
		originalCorpus.add("the brown fox jumped over the brown dog");
		originalCorpus.add("the lazy brown dog sat in the corner");
		originalCorpus.add("the red fox bit the lazy dog");
		
		return originalCorpus;
	}
	
	private ArrayList<String> initializeDocument(String document){

		
		ArrayList<String> newDocument = new ArrayList<String>();
		String[] splitted = document.split(" ");
		
		for(String s:splitted){
			newDocument.add(s);
		}
		
		return newDocument;
		
	}
	
	private Map<String, ArrayList<Integer>> createIndex(ArrayList<ArrayList<String>> corpus){
		Map<String, ArrayList<Integer>> index = new HashMap<>();
		
		for(int i = 0; i<corpus.size(); i++){
			for(String word:corpus.get(i)){
				if(!index.containsKey(word)){
					index.put(word, new ArrayList<Integer>(Arrays.asList(i)));
				} else{
					ArrayList<Integer> tmpList = index.get(word);
					if(!tmpList.contains(i)){
						tmpList.add(i);
						index.remove(word);
						index.put(word, tmpList);
					}
					
				}
				
			}
		}
		
		return index;
		
	}

	private String requestSearchValue(){
		Scanner s = new Scanner(System.in);
		System.out.println("Enter your search value:");
		String searchValue = s.nextLine();
		s.close();
		return searchValue;
	}

	private ArrayList<Integer> sortDocuments(ArrayList<Integer> documents, ArrayList<ArrayList<String>> corpus, String word){
		ArrayList<Integer> sortedDocuments = new ArrayList<Integer>();
		ArrayList<Pair<Integer, Double>> tfidfValues = new ArrayList<Pair<Integer, Double>>();
		
		double idfValue = findIdf(documents, corpus);
		
		for(int i:documents){
			double tfValue = findTf(corpus.get(i), word);
			tfidfValues.add(new Pair<Integer, Double>(i, tfValue*idfValue));
		}
		
		tfidfValues = sortedValues(tfidfValues);
		
		for(Pair<Integer, Double> pair:tfidfValues){
			sortedDocuments.add(pair.getKey());
		}
		
		return sortedDocuments;
	}
	
	private double findTf(ArrayList<String> document, String word){
		int occurences = Collections.frequency(document, word);
		return Double.valueOf(occurences)/Double.valueOf(document.size());
	}
	
	private double findIdf(ArrayList<Integer> documents, ArrayList<ArrayList<String>> corpus){
		double idf = Math.log(Double.valueOf(corpus.size()+1)/Double.valueOf(documents.size()+1)); //Adds 1 to both numerator and denominator to avoid division by zero and also get accurate results.
		return idf;
	}
	
	private ArrayList<Pair<Integer, Double>> sortedValues(ArrayList<Pair<Integer, Double>> tfidfValues){
		
		tfidfValues.sort(new Comparator<Pair<Integer, Double>>() {
	        @Override
	        public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
	            if (o1.getValue() > o2.getValue()) {
	                return -1;
	            } else if (o1.getValue().equals(o2.getValue())) {
	                return 0;
	            } else {
	                return 1;
	            }
	        }
	    });
		
		return tfidfValues;
	}

}

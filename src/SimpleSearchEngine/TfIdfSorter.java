package SimpleSearchEngine;

import java.util.*;
import javafx.util.*;

public class TfIdfSorter {

	/**
	   * Sorts the documents by TF-IDF. First finds the common IDF for all relevant documents, and then calculates TF-value per document. Finally sorts and puts the result in a list of integers.
	   * 
	   * @param documents a list of the documents to be sorted, defined by their index in the corpus
	   * @param corpus the corpus containing all the documents, saved as lists of strings
	   * @param word the word the list is supposed to be TF-IDF sorted by.
	   * @return a list of documents defined by their index in the corpus, but sorted
	   */
	public ArrayList<Integer> sortDocuments(ArrayList<Integer> documents, ArrayList<ArrayList<String>> corpus, String word){
		ArrayList<Integer> sortedDocuments = new ArrayList<>();
		ArrayList<Pair<Integer, Double>> tfidfValues = new ArrayList<>();
		
		double idfValue = findIdf(documents, corpus);
		
		for(int i:documents){
			double tfValue = findTf(corpus.get(i), word);
			tfidfValues.add(new Pair<Integer, Double>(i, tfValue*idfValue));
		}
		
		tfidfValues = sortIndexes(tfidfValues);
		
		for(Pair<Integer, Double> pair:tfidfValues){
			sortedDocuments.add(pair.getKey());
		}
		
		return sortedDocuments;
	}
	
	/**
	   * Finds the term frequency of a word in a document.
	   * 
	   * @param document the document to be searched
	   * @param word the word to be searched for
	   * @return the value of term frequency
	   */
	private double findTf(ArrayList<String> document, String word){
		int occurences = Collections.frequency(document, word);
		return Double.valueOf(occurences)/Double.valueOf(document.size());
	}
	
	/**
	   * Finds the inverse document frequency of a word in a corpus.
	   * 
	   * @param documents the document that contain the word
	   * @param corpus the corpus to be searched
	   * @return the value of inverse document frequency
	   */
	private double findIdf(ArrayList<Integer> documents, ArrayList<ArrayList<String>> corpus){

		//Adds 1 to both numerator and denominator to avoid division by zero and also get accurate results.
		double corpusSize = corpus.size()+1;
		double documentsWithWord = documents.size()+1;
		
		return Math.log(corpusSize / documentsWithWord);
	}
	
	/**
	   * Sorts the indexes of documents based on connected values of those indexes.
	   * 
	   * @param pairedIndexValues the list of indexes to be sorted, paired with the values connected to the index.
	   * @return the sorted list
	   */
	private ArrayList<Pair<Integer, Double>> sortIndexes(ArrayList<Pair<Integer, Double>> pairedIndexValues){
		
		pairedIndexValues.sort(new Comparator<Pair<Integer, Double>>() {
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
		
		return pairedIndexValues;
	}

}

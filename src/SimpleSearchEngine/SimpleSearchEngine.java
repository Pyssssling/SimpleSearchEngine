package SimpleSearchEngine;

import java.util.*;
import javafx.util.*;

public class SimpleSearchEngine {

	public static void main(String[] args) {
		SimpleSearchEngine search = new SimpleSearchEngine();
		
		ArrayList<String> results = search.runSearchEngine();
		System.out.println("Result: " + results);

	}
	
	/**
	   * Main method for the class. Initializes corpus, builds index, requests a search value, finds the indexed values, and puts together the results.
	   * 
	   * @return results from the search 
	   * @throws NullPointerException if no documents contains the words searched for
	   */
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

	/**
	   * The initial creation of the corpus. Define the documents in the form of strings here.
	   * 
	   * @return the corpus made of documents in the form of long strings
	   */
	private ArrayList<String> createCorpus(){
		ArrayList<String> originalCorpus = new ArrayList<String>();
		
		originalCorpus.add("the brown fox jumped over the brown dog");
		originalCorpus.add("the lazy brown dog sat in the corner");
		originalCorpus.add("the red fox bit the lazy dog");
		
		return originalCorpus;
	}
	
	/**
	   * Transforms the documents in the corpus from long strings to lists of words in the document.
	   * 
	   * @param basicCorpus the corpus made of documents in the form of long strings
	   * @return the corpus made of documents in the form of a list of strings
	   */
	private ArrayList<ArrayList<String>> initializeCorpus(ArrayList<String> basicCorpus){
		ArrayList<ArrayList<String>> corpus = new ArrayList<ArrayList<String>>();
		
		for (int i = 0; i < basicCorpus.size(); i++){
			corpus.add(stringToList(basicCorpus.get(i)));
		}
		
		return corpus;
	}
	
	/**
	   * Transforms a single string into a list of its words divided by spaces.
	   * 
	   * @param document the string to be divided
	   * @return the string split up and put into a list of strings
	   */
	private ArrayList<String> stringToList(String document){

		
		ArrayList<String> newDocument = new ArrayList<String>();
		String[] splitted = document.split(" ");
		
		for(String s:splitted){
			newDocument.add(s);
		}
		
		return newDocument;
		
	}
	
	/**
	   * Creates an inverse index by going through all documents in the corpus. Adds a new document to the index only if the document is not added already.
	   * 
	   * @param corpus the corpus to be indexed
	   * @return the index as a map with each word mapped to a list of all the documents that contain it
	   */
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

	/**
	   * Asks the user to put in a word to be searched for.
	   * 
	   * @return the string the user put in
	   */
	private String requestSearchValue(){
		Scanner s = new Scanner(System.in);
		System.out.println("Enter your search value:");
		String searchValue = s.nextLine();
		s.close();
		return searchValue;
	}

	/**
	   * Sorts the documents by TF-IDF. First finds the common IDF for all relevant documents, and then calculates TF-value per document. Finally sorts and puts the result in a list of integers.
	   * 
	   * @param documents a list of the documents to be sorted, defined by their index in the corpus
	   * @param corpus the corpus containing all the documents, saved as lists of strings
	   * @param word the word the list is supposed to be TF-IDF sorted by.
	   * @return a list of documents defined by their index in the corpus, but sorted
	   */
	private ArrayList<Integer> sortDocuments(ArrayList<Integer> documents, ArrayList<ArrayList<String>> corpus, String word){
		ArrayList<Integer> sortedDocuments = new ArrayList<Integer>();
		ArrayList<Pair<Integer, Double>> tfidfValues = new ArrayList<Pair<Integer, Double>>();
		
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
		double idf = Math.log(Double.valueOf(corpus.size()+1)/Double.valueOf(documents.size()+1)); //Adds 1 to both numerator and denominator to avoid division by zero and also get accurate results.
		return idf;
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

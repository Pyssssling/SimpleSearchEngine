package SimpleSearchEngine;

import java.util.*;

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
		
		TfIdfSorter sorter = new TfIdfSorter();
		ArrayList<String> results = new ArrayList<>();
		
		ArrayList<String> basicCorpus = createCorpus();
		ArrayList<ArrayList<String>> corpus = convertCorpus(basicCorpus);
		
		Map<String, ArrayList<Integer>> index = createIndex(corpus);
		
		String searchValue = requestSearchValue();
		
		ArrayList<Integer> foundDocuments = index.get(searchValue);
		
		if(foundDocuments instanceof ArrayList){
			if (foundDocuments.size()>1){
				foundDocuments = sorter.sortDocuments(foundDocuments, corpus, searchValue);
				for(int documentIndex:foundDocuments){
					results.add(basicCorpus.get(documentIndex));
				}
		
			} else if(foundDocuments.size() == 1){
				results.add(basicCorpus.get(foundDocuments.get(0)));
			}
		} else {
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
		ArrayList<String> originalCorpus = new ArrayList<>();
		
		originalCorpus.add("the brown fox jumped over the brown dog");
		originalCorpus.add("the lazy brown dog sat in the corner");
		originalCorpus.add("the red fox bit the lazy dog");
		
		return originalCorpus;
	}
	
	/**
	   * Converts the documents in the corpus from long strings to lists of words in the document.
	   * 
	   * @param basicCorpus the corpus made of documents in the form of long strings
	   * @return the corpus made of documents in the form of a list of strings
	   */
	private ArrayList<ArrayList<String>> convertCorpus(ArrayList<String> basicCorpus){
		ArrayList<ArrayList<String>> corpus = new ArrayList<>();
		
		for (String document : basicCorpus){
			corpus.add(stringToList(document));
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

		String[] splitted = document.split(" ");
		
		return new ArrayList<>(Arrays.asList(splitted));
		
	}
	
	/**
	   * Creates an inverse index by going through all documents in the corpus. Adds a new document to the index only if the document is not added already.
	   * 
	   * @param corpus the corpus to be indexed
	   * @return the index as a map with each word mapped to a list of all the documents that contain it
	   */
	private Map<String, ArrayList<Integer>> createIndex(ArrayList<ArrayList<String>> corpus){
		Map<String, ArrayList<Integer>> index = new HashMap<>();
		
		for(ArrayList<String> document:corpus){
			for(String word:document){
				if(!index.containsKey(word)){
					index.put(word, new ArrayList<>(Arrays.asList(corpus.indexOf(document))));
				} else{
					index = addAdditionalIndex(index, word, corpus.indexOf(document));
				}
				
			}
		}
		
		return index;
		
	}
	
	/**
	   * Checks if the word is already found in a document according to the index. If not, adds the document to the list of documents that contain the word.
	   * 
	   * @param index the index where the word should be added to
	   * @param word the word to be added
	   * @param i the location of the document in the corpus in which the word can be found
	   * @return the index with the word and location included
	   */
	private Map<String, ArrayList<Integer>> addAdditionalIndex(Map<String, ArrayList<Integer>> index, String word, int i){
		ArrayList<Integer> tmpList = index.get(word);
		
		if(!tmpList.contains(i)){
			tmpList.add(i);
			index.remove(word);
			index.put(word, tmpList);
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

}

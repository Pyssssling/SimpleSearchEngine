package SimpleSearchEngine;

import java.util.ArrayList;

public class SimpleSearchEngine {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleSearchEngine search = new SimpleSearchEngine();
		
		search.runSearchEngine();

	}
	
	private void runSearchEngine(){
		ArrayList<ArrayList<String>> corpus = new ArrayList<ArrayList<String>>();
		
		corpus = initializeCorpus(corpus);
		
	}
	
	private ArrayList initializeCorpus(ArrayList corpus){
		ArrayList<String> originalCorpus = declareCorpus();
		
		for (int i = 0; i < originalCorpus.size(); i++){
			corpus.add(initializeDocument(originalCorpus.get(i)));
		}
		
		System.out.println(corpus);
		
		return corpus;
	}
	
	private ArrayList declareCorpus(){
		ArrayList<String> originalCorpus = new ArrayList<String>();
		
		originalCorpus.add("the brown fox jumped over the brown dog");
		originalCorpus.add("the lazy brown dog sat in the corner");
		originalCorpus.add("the red fox bit the lazy dog");
		
		return originalCorpus;
	}
	
	private ArrayList initializeDocument(String document){
		
		ArrayList<String> newDocument = new ArrayList<String>();
		String[] splitted = document.split(" ");
		
		for(String s:splitted){
			newDocument.add(s);
		}
		
		return newDocument;
		
	}
	
	

}

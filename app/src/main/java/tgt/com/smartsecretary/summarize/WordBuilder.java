package tgt.com.smartsecretary.summarize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Takes sentenceObjects and splits them into Word-objects. 
 * @author Piraveen
 */
public class WordBuilder {
	private static ArrayList<Word> dirtyWordObjects = new ArrayList<Word>();						// Word-objects with stop-words
	private static ArrayList<Word> cleanWordObjects;												// Word-objects without stop-words	
	private static LinkedHashMap<Word, Integer> freqMap = new LinkedHashMap<Word, Integer>();		// Word:Frequency-map 			why use linkedhashmap?   order = insertion-order
	private static ArrayList<Word> maxWordList = new ArrayList<>();									// Top N words based on occurrence
	
	//FIXME: Need to ignore numbers
	/**
	 * Empty class constructor
	 */
	public WordBuilder(){}

	// retrieve words from the sentence list
	/**
	 * Turn the sentences in {@link SentenceBuilder#sentenceObjects sentenceObjects} to a list of Word-objects. Since this is done with custom
	 * Word-objects, each Word has info about which sentence it was affiliated with.
	 * @param language two letter language code (ISO 639-1)
	 * @param path file path
	 * @return list of Word-objects
	 * @see Word
	 */
	public List<Word> getWords(String language, String path){		 
		// get list of sentence objects from SentenceBuilder
		//SentenceBuilder sb = new SentenceBuilder();
		ArrayList<Sentence> sentenceObjects = SentenceBuilder.getSentenceObjects();

		String[] wordsForCurrentSentence = null;

		// loop through every sentence
		for (Sentence sentence : sentenceObjects) {
			if(language.equals("NO")) 
				wordsForCurrentSentence = sentence.getText().split("([^\\w������]+)");	// norwegian: split for every non-word (including ���)
			else if(language.equals("EN"))
				wordsForCurrentSentence = sentence.getText().split("([^\\w']+)");		// 	english:  split for every non-word (including ')
			else
				System.err.println("Please set a valid language code.");

			// sentence number
			int sentenceNo = sentence.getSentenceNo();

			// for every sentence, add every word
			for (String word : wordsForCurrentSentence) {
				Word w = new Word(word.toLowerCase(), sentenceNo);
				dirtyWordObjects.add(w); 	
			}

		}
		return dirtyWordObjects;
	}
	
	// getter
	public static ArrayList<Word> getCleanWordObjects(){
		return cleanWordObjects;
	}
	
	// getter
	public static ArrayList<Word> getDirtyWordObjects(){
		return dirtyWordObjects;
	}
	
	// getter
	public static LinkedHashMap<Word, Integer> getfreqMap(){
		return freqMap;
	}

	
	/**
	 * Clean up word-list by removing stop-words. 
	 * {@link SentenceBuilder#dirtyWordObjects dirtyWordObjects} is the input, and a clean list without stop-words will be found in
	 * {@link SentenceBuilder#cleanWordObjects cleanWordObjects}.  
	 * @param language sets stop-word file to be used. Set <i>"NO"</i> for norwegian (bokm�l) or <i>"EN"</i> for english. 
	 * @return list of Word-objects
	 * @see Word
	 */
	public ArrayList<Word> removeStopWords(String language) {
		List<String> stopWords = null;

		if (language.equals("NO"))
			stopWords = FileHandler.readFile("files/stopwords-no_nb.txt");
		else if (language.equals("EN"))
			stopWords = FileHandler.readFile("files/stopwords-en.txt");
		else
			System.err.println("Please set a valid language code.");


		cleanWordObjects = new ArrayList<>(Word.getAllDirtyWords());// copy array

//		if (dirtyWordObjects!=null) {
//			for (Word word : dirtyWordObjects) {
//				if (word != null) {
//					if (stopWords.contains(word.getWordText())) {
//						// remove stop-words
//						cleanWordObjects.remove(word);
//					}
//
//				}
//			}
//		}
		return cleanWordObjects;

	}


	// count occurrence of each word
	public LinkedHashMap<Word, Integer> doCount(ArrayList<Word> list){
		int freq;
		Word currentWord;

		for (int i = 0; i < list.size(); i++) {
			currentWord = list.get(i);
			
			// only run code IFF the key doesn't already exist in the freqMap
			if(!freqMap.containsKey(currentWord)){
				// check frequency of the given string
				freq = Collections.frequency(list, currentWord);	
				freqMap.put(currentWord, freq);	// (word, frequency)
				
				currentWord.setWordOccurence(freq);
				
			}

		}

		return freqMap;
	}
	
	/**
	 * Find top n words that occur the most in the document. Every word is from a different sentence. 
	 * @param nTopEntries number of words to find
	 */
	public void findTopNWords(int nTopEntries /* boolean nonConsecutive*/){
		if(nTopEntries == 0){
			System.err.println("Can't be 0.");
			return;
		}
		else if(nTopEntries > freqMap.size()){
			System.err.println("Entry number higher than number of total entries.");
			System.err.println("Aborting...");
			return;
		}
			
		
		Word tempWord = null;
		List<Word> keys = new ArrayList<>(freqMap.keySet());
		

		
		// hente ut n antall entries
		int counter=0;
		while(counter!=nTopEntries){
			Word maxWord = null;
			
			// loop gjennom listen for � finne maks
			for (Word word : keys) {
				if (tempWord == null || word.compareTo(tempWord) > 0)
			    {
					tempWord = word;
					
					
			    }
			}
			// after finding top word for this run in the for-loop
			maxWord = tempWord;
			
			// add word to final result-list only if there isn't already
			// a word that belongs to the same sentence
			if(!checkSameSentenceNo(maxWordList, maxWord)){
				maxWordList.add(maxWord);
				counter++;
			}
			
			//remove max-word to find second max-word and so on..
			keys.remove(maxWord);		
			//reset 
			tempWord = null;			
			maxWord = null;
			
			
		}		
	}
	
	
	
	/**
	 * Check if entries in list has same sentence no.
	 * @param list the list we're checking against
	 * @param w Word
	 * @return true if thers's already a word with same sentence no.
	 */
	private boolean checkSameSentenceNo(ArrayList<Word> list, Word w){
		if(w==null)
			return false;
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getBelongingSentenceNo() == w.getBelongingSentenceNo())
				return true;
		}
		
		return false;
	}
	
	public static ArrayList<Word> getMaxWordList(){
		return maxWordList;
	}
	

}








package tgt.com.smartsecretary.summarize;

/**
 * @author Piraveen
 * @version 1.0.0
 */
public class Main {

	public static void main(String[] args) {
		
		String FILEPATH = "files/file-medium_en.txt";	// the file you want to summarize
		String LANGCODE = "EN";							// "NO"=norwegian or "EN"=english (decides which stop-word dict to use)				
		int LENGTH = 5;									// Summary length
		
		// read file and create Sentence objects
		SentenceBuilder sb = new SentenceBuilder(LANGCODE, FILEPATH);		
		
		// split to Word objects, remove stop words and count frequency of each unique word 
		WordBuilder wb = new WordBuilder();
		wb.getWords(LANGCODE, FILEPATH);
		wb.removeStopWords(LANGCODE);	
		wb.doCount(wb.getCleanWordObjects());
		
		// debugging
		//DebugClass.printInfo();
		DebugClass.printFreqMap();
		DebugClass.printStats();
		
		// find top N words from N different sentences
		wb.findTopNWords(LENGTH);
		
		// sort top N words with regards to belonging sentence no
		Summarizer sumrizr = new Summarizer();
		sumrizr.sortTopNWordList();
		
		// finally, generate the summary
		DebugClass.printTopNWords();
		sumrizr.createSummary();
		
	}

}

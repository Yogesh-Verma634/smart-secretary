package tgt.com.smartsecretary;

import android.os.Environment;

import tgt.com.smartsecretary.summarize.DebugClass;
import tgt.com.smartsecretary.summarize.SentenceBuilder;
import tgt.com.smartsecretary.summarize.Summarizer;
import tgt.com.smartsecretary.summarize.WordBuilder;

public class SummarizeService {

    private static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/smart-secretary/Sample-text.txt";

    public String  summarize(){
        String LANGCODE = "EN";
        int LENGTH = 5;

        SentenceBuilder sb = new SentenceBuilder(LANGCODE, FILEPATH);
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
        return sumrizr.createSummary();

    }
}

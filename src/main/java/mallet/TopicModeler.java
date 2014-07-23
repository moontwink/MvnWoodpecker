/*
 * McCallum, Andrew Kachites.  "MALLET: A Machine Learning for Language Toolkit."
 *   http://mallet.cs.umass.edu. 2002.
 */

package mallet;

import cc.mallet.util.*;
import cc.mallet.types.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.topics.*;
import gui.Start;

import java.util.*;
import java.util.regex.*;
import java.io.*;
import javax.swing.JOptionPane;
import model.tweetModel;

public class TopicModeler {
    private ArrayList<TopicOutput> allTopics;
    private int dataSize = 0;
    
    /**
     * Imports tweets data into a MALLET TXT file.
     * @param tweets
     */
    public void importData(ArrayList<tweetModel> tweets) {
        String filePath = "src/malletfile.txt";
        
        //Rewrites tweet to text file
        try{
            File file = new File(filePath);
            if(file.exists()) {
                System.out.println("~~!!! FILE EXISTS.. now deleting"); file.delete(); }
            database.Writer write = new database.Writer(filePath, true);
            for(tweetModel tm : tweets){
                write.writeToFile("TWEET-00"+dataSize + " 	X	"+tm.getMessage());
                dataSize++;
            }
        }catch(IOException ex){
            System.out.println("__! Sorry, No Can Do! Failed import data. <mallet> ");
        }
    }
    
    /**
     * Sorts list of topics by TF-IDF in DESCENDING order.
     * @param list 
     */
    public static void sortTopicsList(ArrayList<TopicOutput> list){
        Collections.sort(list, new MyComparator());
    }
    
    /**
     * Comparator used by sortTopicsList for sorting.
     */
      public static class MyComparator implements Comparator<TopicOutput> {
   

        @Override
        public int compare(TopicOutput o1, TopicOutput o2) {
          
        try{
            if (o1.getRelevance() > o2.getRelevance()) {
                return -1;
            } else if (o1.getRelevance() < o2.getRelevance()) {
                return 1;
        }
        }catch(Exception e){
            System.err.println(e.toString());
        }
        return 0;
        }
    }
    
    /**
     * Trains topics using imported mallet file.
     */
    public void trainTopics(){
        Start.systemOutArea.append("\tTraining Topics\n");
        // Begin by importing documents from text to feature sequences
        ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

        // Pipes: lowercase, tokenize, remove stopwords, map to features
        pipeList.add( new CharSequenceLowercase() );
        pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("src/main/java/stoplists/en.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequenceRemoveStopwords(new File("src/main/java/stoplists/ph.txt"), "UTF-8", false, false, false) );
        pipeList.add( new TokenSequence2FeatureSequence() );

        InstanceList instances = new InstanceList (new SerialPipes(pipeList));

        try {
            Reader fileReader;
            fileReader = new InputStreamReader(new FileInputStream(new File("src/malletfile.txt")), "UTF-8");
            instances.addThruPipe(new CsvIterator (fileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
                                                                                   3, 2, 1)); // data, label, name fields
        } catch (UnsupportedEncodingException ex) {
            System.out.println("UNSUPPORTED ENCODING EXCEPTION");
        } catch (FileNotFoundException ex) {
            System.out.println("FILE NOT FOUND EXCEPTION");
        }
        

        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
        //  Note that the first parameter is passed as the sum over topics, while
        //  the second is 
        int numTopics = 10;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);

        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);

        // Run the model for 50 iterations and stop (this is for testing only, 
        //  for real applications, use 1000 to 2000 iterations)
        int iterNum = dataSize+10;
        if(dataSize > 1000 && dataSize < 2000){
            iterNum = 1500;
        }else if (dataSize > 2000) {
            iterNum = 2000;
        }
//        int iterNum = 2000;
        model.setNumIterations(iterNum);
            
        try {
            model.estimate();
        } catch (IOException ex) {
            System.out.println("MODEL ESTIMATE EXCEPTION");
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Mallet Overflow.\n Please press Begin again.", "Mallet", JOptionPane.ERROR_MESSAGE);
        }

        // Show the words and topics in the first instance

        // The data alphabet maps word IDs to strings
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().get(0).instance.getData();
        LabelSequence topics = model.getData().get(0).topicSequence;

        
        Formatter out = new Formatter(new StringBuilder(), Locale.US);
        /*
        for (int position = 0; position < tokens.getLength(); position++) {
                out.format("%s-%d ", dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)), topics.getIndexAtPosition(position));
        }
        System.out.println(out);
        */

        // Estimate the topic distribution of the first instance, 
        //  given the current Gibbs state.
        double[] topicDistribution = model.getTopicProbabilities(0);

        // Get an array of sorted sets of word ID/count pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();

        // Show top 5 words in topics with proportions for the first document
        allTopics = new ArrayList<>();
        
        for (int topic = 0; topic < numTopics; topic++) {
                Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
                
                out = new Formatter(new StringBuilder(), Locale.US);
                out.format("%d\t%.3f\t", topic, topicDistribution[topic]);
                int rank = 0;
                
                ArrayList<String> keywords = new ArrayList<>();
                while (iterator.hasNext() && rank < 20) {
                        IDSorter idCountPair = iterator.next();
                        out.format("%s ", dataAlphabet.lookupObject(idCountPair.getID()));
                        rank++;
                        
                        keywords.add(""+dataAlphabet.lookupObject(idCountPair.getID()));
                }
                
                TopicOutput topicsOut = new TopicOutput(topic, topicDistribution[topic], keywords);
                allTopics.add(topicsOut);
                
                System.out.println(out);
        }

        // Create a new instance with high probability of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();

        int rank = 0;
        while (iterator.hasNext() && rank < 20) {
                IDSorter idCountPair = iterator.next();
                topicZeroText.append(dataAlphabet.lookupObject(idCountPair.getID()) + " ");
                rank++;
        }

        // Create a new instance named "test instance" with empty target and source fields.
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inferencer = model.getInferencer();
        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 10, 1, 5);
        System.out.println("0\t" + testProbabilities[0]);
    }
	
    /**
     * @return the allTopics
     */
    public ArrayList<TopicOutput> getAllTopics() {
        return allTopics;
    }

}
package y1s2_a4;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer 
{

	private File f;
	private String normalized="";
	private String clean="";
	private HashMap<String, Integer>  wordsCount;

	// constructor 
	public TextAnalyzer(String textPath) throws IOException
	{

		f = new File(textPath);	
		if (!f.exists() || !f.isFile())
		{
			throw new FileNotFoundException("File not found");
		}
		if (!textPath.substring(textPath.length()-4,textPath.length()).equals(".txt"))
		{
			throw new IOException ("Not a proper text file");	
		}
	}

	
	// method that normalizes and cleans the text file

	public void cleanText() throws CleanFailException
	{

		try
		{
			//normalize

			//read original text file
			FileReader reader = new FileReader(f.getAbsoluteFile());
			int ch;
			do
			{
				ch = reader.read();
				normalized += (char) ch;
			}
			while (ch != -1 );
			reader.close();


			// normalize string
			for(int i=0; i< normalized.length(); i++)
			{
				// capitalization
				if (normalized.charAt(i) >= 65  &&  normalized.charAt(i) <= 90)
				{
					normalized = normalized.toLowerCase();
				}
				// non letters
				else if (!(normalized.charAt(i)>=97 && normalized.charAt(i)<=122) && normalized.charAt(i)!=' ' && normalized.charAt(i)!='\n')
				{
					if(normalized.charAt(i)=='\'')
						normalized = normalized.replace("\'", "");
					else
						normalized = normalized.replace(Character.toString(normalized.charAt(i)), " ");
				}
			}
			// delete unnecessary white space  
			normalized = normalized.replaceAll("\\u0020+", " ");
			normalized = normalized.replaceAll("\n ", "\n");

			// write normalized text file
			FileWriter writer = new FileWriter(f.getAbsolutePath().replace(".txt", "Normalize.txt"));
			writer.write(normalized);
			writer.close();


			// normalization done, lets clean!

			String stopWords="";

			// read stop words text into string
			reader = new FileReader(f.getParent()+"\\stopWords.txt");
			do 
			{
				ch = reader.read();
				stopWords += (char) ch;
			}
			while (ch != -1);
			reader.close();


			// count stop words
			int numOfStops=0;
			if(stopWords!="")
				numOfStops=1;
			for (int i =0; i<stopWords.length()-1; i++)
			{
				if (stopWords.charAt(i+1)=='\n' || stopWords.charAt(i+1)=='\0' )
					numOfStops++;
			}

			// load stop words into array
			String[] stopsArr = new String[numOfStops];
			String word2Arr="";
			for (int j=0; j<stopsArr.length; j++)
			{
				for (int i =0; i<stopWords.length()-1; i++)
				{
					if (stopWords.charAt(i)!='\r' && stopWords.charAt(i)!= '\n' && stopWords.charAt(i	)!='\0')
						word2Arr+=stopWords.charAt(i);
					if (i+1==stopWords.length()-1 ||
							stopWords.charAt(i+1)=='\r'  || (stopWords.charAt(i+1)=='\n' && stopWords.charAt(i)!='\r'))
					{
						stopsArr[j]=word2Arr;
						j++;
						word2Arr="";
					}
				}
			}

			// clean normalized text -> basically replaces stop words with space and then deletes unnecessary space
			clean = normalized;
			for (int i=0; i<stopsArr.length; i++)
			{

				if (stopsArr[i]!=null)
				{
					clean=clean.replaceAll(" "+stopsArr[i]+" ", "  ");
					clean=clean.replaceAll("^"+stopsArr[i]+" ", "  ");
					clean=clean.replaceAll("\n"+stopsArr[i]+" ", "\n");
					clean=clean.replaceAll(" "+stopsArr[i]+" ", "  ");
					clean=clean.replaceAll("\\u0020+", " ");
					clean=clean.replaceAll("\n ", "\n");
				}
			}
			clean = clean.replaceAll("\\u0020+", " ");


			// write clean text into file
			writer = new FileWriter(f.getAbsolutePath().replace(".txt", "Clean.txt"));
			writer.write(clean);
			writer.close();

		}
		catch (IOException ex)
		{
			File f1= new File(f.getAbsolutePath().replace(".txt", "Clean.txt"));
			f1.deleteOnExit();
			f1= new File(f.getAbsolutePath().replace("Clean.txt", "Normalize.txt"));
			f1.deleteOnExit();
			throw new CleanFailException(ex+" Clean Text Failed !");
		}

	}




	// method counts how many times each word is in the clean text
	// stores the data in a Hash map : key = word , value = times the word appears in clean text

	public void countWords() throws IOException
	{
		try
		{
			wordsCount = new HashMap<>();
			// read text file
			String clean="";
			FileReader reader = new FileReader(f.getAbsolutePath().replace(".txt", "Clean.txt"));
			int ch;
			do
			{
				ch = reader.read();
				clean += (char) ch;
			}
			while(ch!=-1);
			reader.close();

			// load words into Hash map, and count how many times they appear
			String word="";

			//go thru 'clean' string, load words into 'word'
			for (int i=0; i<clean.length(); i++)
			{
				if (clean.charAt(i)>=97 && clean.charAt(i)<=122)
					word+=clean.charAt(i);
				else if (clean.charAt(i)=='\n' || clean.charAt(i)==' ')
				{
					// check if word is already in our Hash map
					if( wordsCount.containsKey(word))
					{
						wordsCount.replace(word, wordsCount.get(word), wordsCount.get(word)+1);
					}				
					else if (word!="" && word!="\n")
						wordsCount.put(word, +1);
					word="";
				}
			}
		}
		catch (IOException e)
		{
			throw new CountFailException(e+"Count Failed");
		}

	}




	// method to generate statistics about our clean text into a new file

	public void generateStatistics() throws GenerationFailException
	{
		try 
		{
			//array to hold all words that start with the same letter
			//index 0=a, 1=b, 2=c ... 26=z
			String[] letters = new String[26];
			for (int i=0; i<letters.length; i++)
				letters[i]="";
			//generate our Hash map
			countWords();
			//find most common words and count their appearance
			String commonWord="";
			int totalWords=0;
			int maxValue=(Collections.max(wordsCount.values()));
			for (Entry<String, Integer> entry : wordsCount.entrySet())
			{
				// count
				totalWords+=entry.getValue();
				// common word
				if (entry.getValue()==maxValue)
				{	
					commonWord+=entry.getKey()+", ";
				}
				// fill up our letters array
				letters[(int)entry.getKey().charAt(0) -97]+=entry.getKey()+", ";
			}

			// write to file
			FileWriter writer = new FileWriter(f.getAbsolutePath().replace(".txt", "Stat.txt"));
			writer.write("Total words: "+totalWords+"\n"
					+ "Total unique words: "+wordsCount.size()+"\n"+
					"Most occured word: "+commonWord+"\n"+
					"________________________\n"+
					"Words By Letters: \n"
					);

			// words by letters	
			//count the number of words we got in each letter
			for (int i=0; i<letters.length; i++)
			{
				int count=0;
				for (int j=0; j<letters[i].length(); j++)
				{
					if (letters[i].charAt(j)==',')
						count++;
				}
				writer.write((char)(i+97) +": "+count+"\n");
			}


			//			word count

			// write all statistics to file
			writer.write("Word count:\nWord\t\tcount\t\t% from total\n_______________\n");
			// in order to sort our Hash map, i used an ArrayList
			ArrayList<String> wordStatsArrayList= new ArrayList<>();
			// load up our ArrayList
			for (Entry<String, Integer> entry : wordsCount.entrySet())
			{
				wordStatsArrayList.add(entry.getKey()+"\t\t"+entry.getValue()+"\t\t"+
						String.format("%.4f", (double) entry.getValue()/ (double) totalWords));
			}
			// sort
			Collections.sort(wordStatsArrayList);
			// write to file
			writer.write(wordStatsArrayList.toString().replace(',', '\n').substring(1, wordStatsArrayList.toString().length()-1).replaceAll(" ", ""));
			writer.close();
		}
		catch (IOException e)
		{
			throw new GenerationFailException(e+"Statistics file wasn't generated");
		}
	}
	
	
	    public static void main(String[] args) 
	    {
			try
			{
				TextAnalyzer a = new TextAnalyzer("C:\\Users\\PC\\eclipse-workspace\\y1s2\\src\\y1s2_a4\\beatles.txt");	
				a.cleanText();
			//	a.countWords();
				a.generateStatistics();
				System.out.println("done");
				System.out.println("\u0020+"); // <- regular expressions for space or something
			}
			catch (Exception e)
			{
				e.printStackTrace();

		}

	    }


}


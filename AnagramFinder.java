import java.io.*;
import java.util.*;

public class AnagramFinder {
	
	//Same concept as wordQuicksort(), except we do it over the entire dictionary of words.
	
	public static void dictionaryQuicksort(Word[] dict, int p, int r){
		if (p<r){
			int q = partitionDictionary(dict,p,r);
			dictionaryQuicksort(dict,p,q-1);
			dictionaryQuicksort(dict,q,r);
		}
	}
	
	//Same concept as partitionWord(), except we do it over the entire dictionary of words.
	//This allows us to sort our words by their scrambled values, which are created in
	//wordQuicksort() and partitionWord()
	public static int partitionDictionary(Word[] dict, int p, int r){
		int low = p;
		int high = r;
		Word pivot = dict[p];
		while(true){
			while((dict[high].getScrambled().compareTo(pivot.getScrambled())>=0) && (low<high)){
				high--;
			}
			while((dict[low].getScrambled().compareTo(pivot.getScrambled())<0) && (low<high)){
				low++;
			}
			if(low<high){
				Word temp = dict[low];
				dict[low]=dict[high];
				dict[high]=temp;
			}
			else return low+1;
		}
	}
	
	
	//Recursively calls itself and partitionWord() to allow words to be sorted based on a pivot.	
	public static void wordQuicksort(char[] letters, int p, int r){
		if (p<r){
			int q = partitionWord(letters,p,r);
			wordQuicksort(letters,p,q-1);
			wordQuicksort(letters,q,r);
		}
	}
	
	//This basically takes a word from the given dictionary and partitions it according to the pivot position.
	//By comparing the characters above and below the pivot, we can move it accordingly in addition to swapping values.
	//This is where the majority of the work is done for the wordQuickSort() method.
	//Since the quickSort is being done over a string instead of a set of numbers, 
		//we effectively sort each individual string alphabetically within itself
		//and use this as a means of sorting overall.
	public static int partitionWord(char[] letters, int p, int r){
		int low = p;
		int high = r;
		char pivot = letters[p];
		while(true){
			while((letters[high]>=pivot) && (low<high)){
				high--;
			}
			while((letters[low]<pivot) && (low<high)){
				low++;
			}
			if(low<high){
				char temp = letters[low];
				letters[low]=letters[high];
				letters[high]=temp;
			}
			else return low+1;
		}
	}
	
	public static void readDictionary(String dictName) throws IOException{
		
		String fileName = dictName;
		
		try{
			
		//This first scanner basically reads in the dictionary to get the number of words present in it for later use.
		Scanner dictReader = new Scanner(new FileInputStream("/some/file/path/"+fileName));
		
		char[] curWord;
		int numOfWords = 0;
		
		while(dictReader.hasNext()){			
			dictReader.next();
			numOfWords++;				
		}
		//System.out.println(numOfWords);
		
		dictReader.close();
		
		//Calls for a quickSort() on each string in the dictionary and stores result in a separate array of Words.
		//Each Word has a scrambled value stored in it and an actual (ie: unscrambled) value stored in it.
		//We can use this array to compare the scrambled values against each other for each word; if the value is the same, then we get an anagram.
		//
		Scanner dictCopier = new Scanner(new FileInputStream("/some/file/path/"+fileName));
		Word[] sortedDict = new Word[numOfWords];
		int i = 0;
		
		while(dictCopier.hasNext()){
			
			curWord = dictCopier.next().toCharArray();
			sortedDict[i]=new Word(new String(curWord),new String(curWord));
			wordQuicksort(curWord,0,curWord.length-1);
			sortedDict[i].setScrambled(new String(curWord));
			i++;
		}
		
		dictionaryQuicksort(sortedDict,0,sortedDict.length-1);
		
		dictCopier.close();
		if(fileName.contains("1")){
			toFile(sortedDict,"anagram1");
		}
		if(fileName.contains("2")){
			toFile(sortedDict,"anagram2");
		}
		}catch(IOException e){
			
		}
		
	}
	
	//Just a method to write to a file.  Basically writes each line based on whether or not the current word is an anagram of the next word
	//in the (already sorted) dictionary.  If it's an anagram, it'll print it on the same line, and if it's not an anagram, we simply
	//print the word out and increment a counter that keeps track of the number of different anagram classes in the file.
	public static void toFile(Word[] dict, String fileName){
		int numberOfClasses = 0;
		Writer write = null;
		try{
			write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + ".txt")));
			int i = 0;
			int d = i+1;
			while(i < dict.length-1){
				if(dict[i].getActual().length() != dict[d].getActual().length()){
					write.write(dict[i].getActual());
					write.write(System.lineSeparator());
					numberOfClasses++;					
				}
				
				else if(dict[i].getScrambled().equals(dict[d].getScrambled())){
					write.write(dict[i].getActual() + "  ");					
				}
				
				else if(dict[i].getActual().length() == dict[d].getActual().length()){
					for(int x = 0; x<dict[i].getActual().length();x++){
						if(dict[i].getScrambled().toCharArray()[x]!=dict[d].getScrambled().toCharArray()[x]){
							write.write(dict[i].getActual());
							write.write(System.lineSeparator());
							numberOfClasses++;
							break;
						}						
					}					
				}				
				i=d;
				d++;				
			}
			System.out.println("This dictionary has " + numberOfClasses + " different anagram classes in it.");
		}
		catch(IOException e){			
		}
		try{
			write.close();
		}
		catch(Exception e){		
		}
	}
	
	
	public static void main(String[] args)throws IOException {
		
		long startTimeForDict1 = System.currentTimeMillis();
		readDictionary("dict1");		
		long endTimeForDict1   = System.currentTimeMillis();
		long totalTimeForDict1 = endTimeForDict1 - startTimeForDict1;
		System.out.println("Total time taken to find all anagrams in Dict1: " + totalTimeForDict1 + " milliseconds");
		
		long startTimeForDict2 = System.currentTimeMillis();
		readDictionary("dict2");		
		long endTimeForDict2   = System.currentTimeMillis();
		long totalTimeForDict2 = endTimeForDict2 - startTimeForDict2;
		System.out.println("Total time taken to find all anagrams in Dict2: " + totalTimeForDict2 + " milliseconds");
				
	}
}
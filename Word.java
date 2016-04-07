
public class Word {

	
	String scrambledWord;
	String actualWord;
	
	public Word(String scrambled, String actual){
		scrambledWord = scrambled;
		actualWord = actual;
	}
	
	public String getScrambled(){
		return this.scrambledWord;
	}
	
	public String getActual(){
		return this.actualWord;
	}
	
	public void setScrambled(String setScrambled){
		this.scrambledWord = setScrambled;
	}
	
	public void setActual(String setActual){
		this.actualWord = setActual;
	}

}

package regex;

public class NFAnode {
	public int number;
	public char skipChar;
	NFAnode next;
	boolean isend;
	public NFAnode(int num,char skip){
		number = num;
		skipChar = skip;
		next = null;
		isend = false;
	}
	public NFAnode(){
		next = null;
	}
}

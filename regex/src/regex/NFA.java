package regex;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NFA {
  
  final int MAX_NODE = 10000;
  final int MAX_LAYER = 100;
  final int MAX_SEPARATION = 100; //最大分歧点数目
  
  int numOfNode;  //节点数
  NFAnode[] nodeList;
  int[][] separations;//分歧点数组，separations[i]表示第i层的分歧点数组
  int[] numOfSeparation; //每一层括号的分歧点数目
  int[] currentSeparation;
  int[] lastSeparation; //用来保存被覆盖的当前分歧点,每层保留一个即可，因为对于相同的输入，所有的分歧点的指向都是一样的
  int layerOfBracket;//当前括号层数，最开始为0层，读入一个(后变为1层，读入一个)变为0层，以此类推
  int separationLayer;
  Set<Character> specialChars;
  
  public NFA(String regex){
	  numOfNode = 1;  //当前节点数
	  nodeList = new NFAnode[MAX_NODE];
	  for(int i=0;i<MAX_NODE;i++){
		  nodeList[i] = new NFAnode(i,' ');
	  }
	  	 
	  separations = new int[MAX_LAYER][MAX_SEPARATION];
	 
	  numOfSeparation = new int[MAX_LAYER];  //记录分歧点数目
	  for(int i=0;i<MAX_LAYER;i++)
	  {
		  numOfSeparation[i] = 1;
	  }
	  currentSeparation = new int[MAX_LAYER];  //从currentSeparation[i]到numOfSeparation[i]-1是当前层的前置点在separations数组中的第二个下标，默认初始化为0 
	  lastSeparation  = new int[MAX_LAYER];  //默认初始化为0
	  layerOfBracket = 0;  
	  separationLayer = -1;

	  specialChars = new HashSet<Character>();
	  specialChars.add(new Character('('));
	  specialChars.add(new Character(')'));
	  specialChars.add(new Character('|'));
	  specialChars.add(new Character('*'));
	  specialChars.add(new Character('?'));
	  
	  for(int i=0;i<regex.length();i++){
		  Character currentCharacter = new Character(regex.charAt(i));
		  
		  if(!specialChars.contains(currentCharacter)){
			  if(separationLayer==-1){
				  last(nodeList[0]).next = new NFAnode(numOfNode,currentCharacter);
				  lastSeparation[layerOfBracket] = 0;
			  }
			  else{
			    for(int j=currentSeparation[separationLayer];j<numOfSeparation[separationLayer];j++){
			      last(nodeList[separations[separationLayer][j]]).next = new NFAnode(numOfNode,currentCharacter);
			     }
			    lastSeparation[layerOfBracket] = separations[separationLayer][currentSeparation[separationLayer]];
			  }
			  
			    
			  numOfNode++;
			  separationLayer = layerOfBracket;
			  separations[layerOfBracket][currentSeparation[layerOfBracket]] = numOfNode-1;
			  numOfSeparation[layerOfBracket] = currentSeparation[layerOfBracket] +1;
			       
		  }
		  else if(currentCharacter.charValue()=='('){
			  layerOfBracket++;	
			  separationLayer = layerOfBracket -1;
			  		  
		  }
		  else if(currentCharacter.charValue()=='|'){
			  separationLayer = layerOfBracket -1;
			  numOfSeparation[layerOfBracket]++; 
			  currentSeparation[layerOfBracket] = numOfSeparation[layerOfBracket] -1;
			  
		  }
		  else if(currentCharacter.charValue()==')'){
			  separations[layerOfBracket][numOfSeparation[layerOfBracket]] = numOfNode-1;
			  lastSeparation[layerOfBracket-1] = separations[layerOfBracket-1][currentSeparation[layerOfBracket-1]];			  
			  for(int j=0;j<numOfSeparation[layerOfBracket];j++){
				  separations[layerOfBracket-1][j+currentSeparation[layerOfBracket-1]] = separations[layerOfBracket][j];
			  }
			  
			  numOfSeparation[layerOfBracket-1] = currentSeparation[layerOfBracket-1] + numOfSeparation[layerOfBracket];
			  numOfSeparation[layerOfBracket] = 1;

			  currentSeparation[layerOfBracket] = 0;
			  layerOfBracket--;
			  separationLayer = layerOfBracket;
		  }
		  else if(currentCharacter.charValue()=='+'){
			  //*无非就是多增加几条边
			      for(int j=currentSeparation[layerOfBracket];j<numOfSeparation[layerOfBracket];j++){
				      NFAnode  node = nodeList[lastSeparation[layerOfBracket]].next;
				      while(node!=null){
				          last(nodeList[separations[layerOfBracket][j]]).next = new NFAnode(node.number,node.skipChar);
				          node = node.next;
				      }
			  }
		  }
	  }
	  //找出所有的终点状态
	 for(int i=0;i<numOfSeparation[layerOfBracket];i++)
	 nodeList[separations[layerOfBracket][i]].isend = true; 
  }
  
  public NFAnode last(NFAnode node){
			while(node.next!=null){
				node = node.next;
			}
			return node;
		}
  
  public void print(){
	  for(int i=0;i<numOfNode;i++){
		  NFAnode node = nodeList[i];
		  if(node.isend)
			  System.out.print("end");
		  while(node!=null){
			  System.out.print(node.number + "/" + node.skipChar + " ");
			  node = node.next;	  
		  }
		  System.out.print("\n");
	  }
  }
  
  public boolean matches(String input){
	  int currentState = 0;
	  for(int i =0;i<input.length();i++){
		  NFAnode node = nodeList[currentState].next;
		  while(node!=null){
			  if(input.charAt(i)==node.skipChar){
				  currentState = node.number;
				  break;
			  }
			  node=node.next;
		  }
	  }
	  if(nodeList[currentState].isend==true){
		  return true;
		  }
	  else{
	      return false;
	      }
	  
  } 
  
  public static void main(String args[]){
	  NFA nfa = new NFA("abc(a|b|c)+cba");
	  nfa.print();
	  Scanner scanner = new Scanner(System.in);
	  while(true){	      
	      String  input = scanner.nextLine();
	      if(nfa.matches(input)){
		      System.out.println("matches!");
	      }
	      else{
		      System.out.println("does not match!");
	      }
	  }
  }  
}
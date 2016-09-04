package regex;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



public class DFA {
	int[][] skipTable;
	final int NUMOFCHAR=128;  //assic�ַ�����
	final int MAXSTATE=100;
	int numOfState; 

	public DFA(NFA nfa){
		skipTable = new  int[MAXSTATE][NUMOFCHAR];  //Ĭ�ϳ�ʼ��Ϊ0
		numOfState = 1;
		Queue<DFAstate> queue = new LinkedList<DFAstate>();
		Set<DFAstate> DFAset = new HashSet<DFAstate>();
		
		DFAstate start = new DFAstate();  
		start.add(new Integer(0));
		DFAset.add(start);
		queue.add(start);
		
		while(!queue.isEmpty()){
			DFAstate state = queue.poll();
			Set<Integer> NFAset = state.NFAnodeSet;
			Iterator<Integer> NFAit = NFAset.iterator();
			Set<Character> skipChars = new HashSet<Character>();
			
			
			//��һ��ѭ���� �ҳ�������ת�ַ��ļ���
			while(NFAit.hasNext()){
				int nodeNo = ((Integer)NFAit.next()).intValue();
				NFAnode node = nfa.nodeList[nodeNo].next;
				while(node!=null){
					skipChars.add(new Character(node.skipChar));
					node = node.next;
				}
			}
			//�ڶ���ѭ�����ҳ�ÿһ����ת�ַ� ��Ӧ��NFA״̬����
			Iterator<Character> charIt = skipChars.iterator();
			while(charIt.hasNext()){
				char currentChar = charIt.next();
				NFAit = NFAset.iterator();
				DFAstate tempState = new DFAstate();
				while(NFAit.hasNext()){
					int nodeNo = NFAit.next().intValue();
					NFAnode node = nfa.nodeList[nodeNo].next;
					while(node!=null){
						if(node.skipChar == currentChar){
							tempState.add(new Integer(node.number));			
						}
						node = node.next;
					}
				}
				
				//�����µ�DFA״̬�����set�в����ڣ�
				if(!DFAset.contains(tempState)){
					tempState.number = numOfState;
					numOfState++;
					DFAset.add(tempState);
					queue.add(tempState);
					
				}
				else{
					Iterator<DFAstate> DFAit = DFAset.iterator();
					while(DFAit.hasNext()){
						DFAstate dfaState = DFAit.next();
					    if(tempState.equals(dfaState)){
					    	tempState = dfaState;
					    	break;
					    }
					}
				}
				
				//������ת��
				skipTable[state.number][currentChar] = tempState.number;				
			}

		}
		Iterator itrator =  DFAset.iterator();
		while(itrator.hasNext()){
			DFAstate st =  (DFAstate)itrator.next();
			Iterator innerIt = st.NFAnodeSet.iterator();
			while(innerIt.hasNext()){
				System.out.print(innerIt.next()+" ");
			}
			System.out.println("");
		}
		}
	
	public void print(){
		for(int i=0;i<numOfState;i++){
			System.out.print(i+" ");
			for(int j=0;j<NUMOFCHAR;j++){
				if(skipTable[i][j]!=0)
				{
					System.out.print((char)j);
					System.out.print("/"+skipTable[i][j]+" ");
				}
			}
			
			System.out.println("");
		}
	}
	
	public static void main(String args[]){
		NFA nfa = new NFA("abc(a|b|c)+cba");
		DFA dfa = new DFA(nfa);
		dfa.print();
		
	}
	

}

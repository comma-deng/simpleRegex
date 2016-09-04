package regex;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DFAstate {
	Set<Integer> NFAnodeSet;  //DFA对应的NFA状态集合
	int number;  //DFA状态编号
	
	public DFAstate(){
		NFAnodeSet = new HashSet<Integer>();
	}
	
	@Override
	public boolean equals(Object obj){
		DFAstate state = (DFAstate) obj;
		if(state.NFAnodeSet.size()!=this.NFAnodeSet.size())
			return false;
		
		Iterator<Integer> it = state.NFAnodeSet.iterator();
	    while(it.hasNext()){
			Integer stateNo = (Integer)it.next();
			if(!this.NFAnodeSet.contains(stateNo)){
				return false;
			}
		}
		return true;
	}
	public void add(Integer integer){
		NFAnodeSet.add(integer);
	}
	
	@Override
	public int hashCode(){
		return NFAnodeSet.size();
	}

	public static void main(String args[]){
		DFAstate s1 = new DFAstate();
		s1.NFAnodeSet.add(new Integer(0));
		s1.NFAnodeSet.add(new Integer(3));
		DFAstate s2 = new DFAstate();
		s2.NFAnodeSet.add(new Integer(0));
		s2.NFAnodeSet.add(new Integer(3));
		Set<DFAstate> set = new HashSet<DFAstate>();
		set.add(s1);
		System.out.println(set.contains(s2));
		System.out.println(s1.equals(s2));
		
	}
}


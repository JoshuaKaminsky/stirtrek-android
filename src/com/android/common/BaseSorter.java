package com.android.common;

import java.util.Comparator;

public class BaseSorter<T> implements Comparator<T>{

	private Comparator<T> _comparer;

	public BaseSorter(Comparator<T> comparer) {
		_comparer = comparer;
	}
	
	public int compare(T lhs, T rhs) {
		if(lhs == null && rhs == null)
			return 0;
		if(lhs == null)
			return 1;
		if(rhs == null)
			return -1;
		
		return _comparer.compare(lhs, rhs);
	}

}

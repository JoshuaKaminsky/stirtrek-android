package com.stirtrek.common;

import java.util.Comparator;

import com.stirtrek.model.Track;

public class TrackSorter implements Comparator<Track>{

	public int compare(Track lhs, Track rhs) {
		if(lhs == null && rhs == null)
			return 0;
		if(lhs == null)
			return 1;
		if(rhs == null)
			return -1;
		
		return lhs.Name.compareTo(rhs.Name);
	}

}

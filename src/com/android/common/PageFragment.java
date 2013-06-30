package com.android.common;

import stirtrek.activity.R;
import android.os.Bundle;  
import android.support.v4.app.Fragment;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
  
public class PageFragment extends Fragment {  
       
     public static PageFragment newInstance() {
 
         PageFragment pageFragment = new PageFragment();
         return pageFragment;
     }
       
     @Override  
     public void onCreate(Bundle savedInstanceState) {  
         super.onCreate(savedInstanceState);  
     }  
       
     @Override  
     public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { 
         View view = inflater.inflate(R.layout.schedule, container, false);  
         
         
         return view;  
     }  
}  
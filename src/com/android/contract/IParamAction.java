package com.android.contract;

public interface IParamAction<TResult, TParam>{
	
	TResult Execute(TParam item);
	
}

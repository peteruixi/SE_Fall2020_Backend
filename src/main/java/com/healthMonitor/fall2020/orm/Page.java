package com.healthMonitor.fall2020.orm;


import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class Page implements Serializable {

	private long mPageNow = 1;
	private int mPageSize = 10;

	@ApiModelProperty(hidden=true)
	private long mToltalSize = 0;
	@ApiModelProperty(hidden=true)
	private long mPageCount = 0;
	@ApiModelProperty(hidden=true)
	private long mPagePre = 0;
	@ApiModelProperty(hidden=true)
	private long mPageNext = 0;
	@ApiModelProperty(hidden=true)
	private long mPageLast = 0;
	@ApiModelProperty(hidden=true)
	private List mList;
	@ApiModelProperty(hidden=true)
	private Object otherObject;
	// 忽略分页
	@ApiModelProperty(hidden=true)
	private boolean ignorePage = false;

	public long getmPageNow() {
		return mPageNow;
	}

	public void setmPageNow(long mPageNow) {
		this.mPageNow = mPageNow;
	}

	/*
	 * public void setmPageNow(String mPageNow) { if(mPageNow!=null) { try {
	 * this.mPageNow = Long.parseLong( mPageNow); } catch (Exception ex) {
	 * 
	 * } } }
	 */
	public int getmPageSize() {
		return mPageSize;
	}

	public void setmPageSize(int mPageSize) {
		this.mPageSize = mPageSize;
	}

	public long getmToltalSize() {
		return mToltalSize;
	}

	public void setmToltalSize(long mToltalSize) {
		this.mToltalSize = mToltalSize;
	}

	public long getmPageCount() {
		return mPageCount;
	}

	public void setmPageCount(long mPageCount) {
		this.mPageCount = mPageCount;
	}

	public long getmPagePre() {
		return mPagePre;
	}

	public void setmPagePre(long mPagePre) {
		this.mPagePre = mPagePre;
	}

	public long getmPageNext() {
		return mPageNext;
	}

	public void setmPageNext(long mPageNext) {
		this.mPageNext = mPageNext;
	}

	public long getmPageLast() {
		return mPageLast;
	}

	public void setmPageLast(long mPageLast) {
		this.mPageLast = mPageLast;
	}

	public List getmList() {
		return mList;
	}

	public void setmList(List mList) {
		this.mList = mList;
	}

	public void setOtherObject(Object otherObject) {
		this.otherObject = otherObject;
	}

	public Object getOtherObject() {
		return otherObject;
	}

	
	public boolean ignorePage() {
		return ignorePage;
	}

	public Page ignorePage(boolean ignorePage) {
		this.ignorePage = ignorePage;
		return this;
	}

}

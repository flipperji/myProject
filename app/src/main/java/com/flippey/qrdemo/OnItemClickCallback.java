package com.flippey.qrdemo;

public interface OnItemClickCallback {

	public void onItemClick(String url, boolean useWebview);
	public void onItemClickByWebview(String url);

}

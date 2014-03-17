package com.example.android_qqfix;

public class LoginUserInfo {
	public int userPhoto;
	public String userQQ=null;
	public int deleteButtonRes;
	/**
	 * @param userPhoto
	 * @param userQQ
	 * @param deleteButtonRes
	 */
	public LoginUserInfo(int userPhoto, String userQQ, int deleteButtonRes) {
		super();
		this.userPhoto = userPhoto;
		this.userQQ = userQQ;
		this.deleteButtonRes = deleteButtonRes;
	}
	
}

package com.m104.transfer;

public class AcInfo {
	String abc;
	String email;
	String familyName;
	String firstName;
	String sex;
	String birthday;
	String cellphone;
	String tel;
	String telArea;
	String postNum;
	String address;
	String identity;
	String pid;
	String mdmkey;
	
	public String getMdmkey() {
		return mdmkey;
	}
	public void setMdmkey(String mdmkey) {
		this.mdmkey = mdmkey;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTelArea() {
		return telArea;
	}
	public void setTelArea(String telArea) {
		this.telArea = telArea;
	}
	public String getPostNum() {
		return postNum;
	}
	public void setPostNum(String postNum) {
		this.postNum = postNum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	public String toString(){
		return this.familyName+" "+this.firstName+" "+this.email;
		
	}


}

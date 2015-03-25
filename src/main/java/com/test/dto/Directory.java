package com.test.dto;

public class Directory {
	private String owner;
	private String contents;
	private String name;
	private String group;
	private long perms;

	public Directory() {}
	
	public Directory(Directory file) {
		owner = file.getOwner();
		contents = file.getContents();
		name = file.getName();
		group = file.getGroup();
		perms = file.getPerms();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String author) {
		this.owner = author;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPerms() {
		return perms;
	}

	public void setPerms(long perms) {
		this.perms = perms;
	}

	public void chmod(String mod) {

	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroup() {
		return group;
	}

}
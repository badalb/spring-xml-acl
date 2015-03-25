package com.test.dao;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.test.dto.Directory;

public class FileHandle {
	private Map<String, FileHandle> children = new HashMap<String, FileHandle>();
	private Directory file;

	public void add(String name, FileHandle file) {
		children.put(name, file);
	}

	public FileHandle get(String name) {
		return children.get(name);
	}

	public void delete(String name) throws FileNotFoundException {
		if (children.remove(name) == null) {
			throw new FileNotFoundException(name);
		}
	}

	public void setFile(Directory file) {
		this.file = file;
	}

	public Directory getFile() {
		return file;
	}
}
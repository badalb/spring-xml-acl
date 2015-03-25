package com.test.dao;

import java.io.FileNotFoundException;

import org.springframework.stereotype.Repository;

import com.test.dto.Directory;

@Repository
public class FileRepository {

	private FileHandle root;

	public Directory create(String path, String user, String group, long perms)
			throws FileNotFoundException {
		// Tokenize the path
		String[] elements = (path.substring(1)).split("/");
		if (elements.length < 1) {
			throw new FileNotFoundException("Path name error " + path);
		}

		/**
		 * Create the root of the file system if it doesn't exist.
		 */
		if (root == null) {
			root = new FileHandle();
			Directory file = new Directory();
			file.setPerms(perms);
			file.setGroup(group);
			file.setOwner(user);
			root.setFile(file);
		}

		FileHandle node = root;
		int i = 0;
		for (; i < elements.length - 1; i++) {
			FileHandle newNode = node.get(elements[i]);
			if (newNode == null) {
				break;
			}
			node = newNode;
		}

		for (; i < elements.length; i++) {
			FileHandle fileHandle = new FileHandle();

			Directory file = new Directory();
			fileHandle.setFile(file);
			file.setPerms(perms);
			file.setGroup(group);
			file.setOwner(user);
			file.setName(path);
			node.add(elements[i], fileHandle);
			node = fileHandle;
		}

		return new Directory(node.getFile());
	}

	public Directory get(String path) throws FileNotFoundException {

		String[] elements = path.split("/");
		if (elements.length == 0) {
			return new Directory(root.getFile());
		}
		FileHandle node = root;
		for (int i = 1; i < elements.length; i++) {
			node = node.get(elements[i]);
			if (node == null) {
				throw new FileNotFoundException("Path name error " + path);
			}
		}

		return new Directory(node.getFile());
	}

	public void delete(String path) throws FileNotFoundException {

		String[] elements = (path.substring(1)).split("/");
		if (elements.length < 1) {
			throw new FileNotFoundException(path);
		}
		FileHandle node = root;
		for (int i = 0; i < elements.length - 1; i++) {
			node = node.get(elements[i]);
			if (node == null) {
				throw new FileNotFoundException(path);
			}
		}
		try {
			node.delete(elements[elements.length - 1]);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(path);
		}
	}

	public void update(String path, Directory file)
			throws FileNotFoundException {

		Directory newFile = new Directory(file);

		String[] elements = (path.substring(1)).split("/");
		if (elements.length < 1) {
			throw new FileNotFoundException(path);
		}
		FileHandle node = root;
		for (int i = 0; i < elements.length; i++) {
			node = node.get(elements[i]);
			if (node == null) {
				throw new FileNotFoundException(path);
			}
		}

		node.setFile(newFile);
	}
}
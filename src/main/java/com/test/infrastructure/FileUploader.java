package com.test.infrastructure;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUploader {

	public static void copyFile(String sourcePath, String destPath){
		File source = new File(sourcePath);
		File desc = new File(destPath);
		try {
		    FileUtils.copyFile(source, desc);
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}

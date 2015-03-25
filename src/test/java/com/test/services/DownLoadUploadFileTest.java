package com.test.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.dto.DirPermission;
import com.test.dto.Directory;
import com.test.infrastructure.FileUploader;
import com.test.services.FileSystemServices;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext-business.xml",
		"classpath:applicationContext-security.xml" })
public class DownLoadUploadFileTest implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Autowired
	FileSystemServices reportServices;

	@Test
	public void uploadFileToDirectory() {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"admin", "secure");
		SecurityContextHolder.getContext().setAuthentication(token);
		Directory file;
		try {
			//destination path (physical directory like D:/temp/myfile/)
			String destpath = "";
			//destination file name (like test.docx)
			String dfileName = "";
			//source file like (D:/temp/JCR2.docx)
			String sourceFile = "";
			
			file = reportServices.create(destpath, dfileName, sourceFile);
			assertEquals("admin", file.getOwner());
			System.out.println(file.getPerms());

			// set perms to u=rw,g=c,o=
			// set group to accounts
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value() + DirPermission.GROUP_CREATE
					.value());
			file.setGroup("accounts");
			file.setPerms(perms);
			reportServices.update(destpath+dfileName, file);
		
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}

	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}

	
	@Test
	public void downLoadFile(){

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"ranjan", "pass3");
		SecurityContextHolder.getContext().setAuthentication(token);
		Directory file;
		try {
			//path (physical directory like D/temp/myfile)
			String path = "";
			//file name (like test.docx)
			String fileName = "test.docx";
			
			file = reportServices.read(path + fileName);
			System.out.println(file.getPerms());
			//source file like D:/temp/download/aclt.docx as second argument
			FileUploader.copyFile(path+fileName,"");
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}

	
	}
	
	
}

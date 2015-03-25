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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.dto.DirPermission;
import com.test.dto.Directory;
import com.test.services.FileSystemServices;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext-business.xml",
		"classpath:applicationContext-security.xml" })
public class ServicesTest implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Autowired
	FileSystemServices reportServices;

	@Test
	public void testAddAdminFile() {/*
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"admin", "secure");
		SecurityContextHolder.getContext().setAuthentication(token);
		Directory file;
		try {
			file = reportServices.create("/", "myfile");
			assertEquals("admin", file.getOwner());
			System.out.println(file.getPerms());

			// set perms to u=rw,g=c,o=
			// set group to accounts
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value() + DirPermission.GROUP_CREATE
					.value());
			file.setGroup("accounts");
			file.setPerms(perms);
			reportServices.update("/myfile", file);
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}

	*/}

	/**
	 * Try and update file without write permission for owner
	 */
	@Test
	public void testUpdateNoPermFile() {

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"badal", "pass1");
		SecurityContextHolder.getContext().setAuthentication(token);

		try {
			Directory file = new Directory();
			reportServices.update("/myfile", file);
			assertEquals("badal", file.getOwner());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		} catch (AccessDeniedException e) {
			return; 
		}
	}

	/**
	 * Grant permissions and try again
	 */
	@Test
	public void testUpdateWithPermFile() {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					"admin", "secure");
			SecurityContextHolder.getContext().setAuthentication(token);
			Directory file = reportServices.read("/myfile");
			// set perms to u=rw,g=r,o=w
			// set group to accounts
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value()
					+ DirPermission.GROUP_READ.value() + DirPermission.OTHER_WRITE
					.value());
			file.setPerms(perms);
			reportServices.update("/myfile", file);

			token = new UsernamePasswordAuthenticationToken("abid", "pass2");
			SecurityContextHolder.getContext().setAuthentication(token);

			file.setGroup("marketing");
			reportServices.update("/myfile", file);
			file = reportServices.read("/myfile");
			assertEquals("marketing", file.getGroup());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Grant permissions and try again
	 */
	@Test
	public void testNoReadPermFile() {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					"admin", "secure");
			SecurityContextHolder.getContext().setAuthentication(token);
			Directory file = reportServices.read("/myfile");
			// set perms to u=rw,g=r,o=w
			// set group to accounts
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value()
					 + DirPermission.OTHER_WRITE
					.value());
			file.setPerms(perms);
			reportServices.update("/myfile", file);

			token = new UsernamePasswordAuthenticationToken("abid", "pass2");
			SecurityContextHolder.getContext().setAuthentication(token);

			file.setGroup("marketing");
			reportServices.update("/myfile", file);
			// this will fail
			file = reportServices.read("/myfile");
			assertEquals("marketing", file.getGroup());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}catch (AccessDeniedException e) {
			return; // ok
		}
	}

	@Test
	public void testAddNoPermFile() {/*
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"david", "pass1");
		SecurityContextHolder.getContext().setAuthentication(token);
		Directory file;
		try {
			file = reportServices.create("/myfile/", "fred");
			assertEquals("badal", file.getOwner());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		} catch (AccessDeniedException e) {
			// File is now part of marketing group with no CREATE permission on
			// other
			return; // ok
		}
	*/}

	@Test
	public void testAddFile() {/*

		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					"admin", "secure");
			SecurityContextHolder.getContext().setAuthentication(token);
			Directory file = reportServices.read("/myfile");
			// set perms to u=rw,g=,o=c
			// set group to accounts
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value() + DirPermission.OTHER_CREATE
					.value());
			file.setPerms(perms);
			reportServices.update("/myfile", file);

			token = new UsernamePasswordAuthenticationToken("badal", "pass1");
			SecurityContextHolder.getContext().setAuthentication(token);

			file = reportServices.create("/myfile/", "bad");
			assertEquals("badal", file.getOwner());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		} catch (AccessDeniedException e) {
			// File is now part of marketing group with no CREATE permission on
			// other
			return; // ok
		}
	*/}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;
	}
}
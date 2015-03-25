package com.test.services;

import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.dto.Directory;
import com.test.services.FileSystemServices;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext-business.xml",
		"classpath:applicationContext-security.xml" })
public class ServicesAuthorizationTest implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private FileSystemServices reportServices;
	
	@Autowired
	private Directory rootFile;
/*
	@Test
	public void testAddArticle() {
		// has ROLE_SECURITY
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"auth1", "pass1");
		SecurityContextHolder.getContext().setAuthentication(token);

		File a = reportServices.addArticle("myarticle", "my contents");
		a.setName("myarticle");
		a.setGroup("GROUP_MARKETING");

		// set perms to u=rw,g=r
		a.setPerms(FilePermission.OWNER_READ.value()
				+ FilePermission.OWNER_WRITE.value()
				+ FilePermission.GROUP_READ.value());
		a = reportServices.saveArticle(a);

		// now use user without EMPLOYEE role
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("testUser", "test"));
		try {
			reportServices.addArticle("article2", "some more content");
			fail("should throw AccessDeniedException");
		} catch (AccessDeniedException e) {
			return; // ok
		} catch (Exception e) {
			System.err.println(e);
		}

		// now use Admin user, should work
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("admin", "secure"));

		reportServices.saveArticle(a);

	}

	@Test
	public void testReadArticleGroup() {
		System.out.println("testEditArticleGroup");
		// has ROLE_AUTHOR and GROUP_MARKETING (group permission)
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("mark1", "pass2"));

		File a = new File();
		a.setName("myarticle");
		a = reportServices.getArticle(a);

		// this should fail
		try {
			a = reportServices.saveArticle(a);
			fail("should throw AccessDeniedException");
		} catch (AccessDeniedException e) {
			return; // ok
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	@Test
	public void testEditReportBadUser() {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"auth2", "pass2");

		SecurityContextHolder.getContext().setAuthentication(token);

		File a = new File();
		a.setName("myarticle");

		try {
			a = reportServices.saveArticle(a);

			fail("should throw AccessDeniedException");
		} catch (AccessDeniedException e) {
			return; // ok
		} catch (Exception e) {
			System.err.println(e);
		}
	}*/

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;

	}
}
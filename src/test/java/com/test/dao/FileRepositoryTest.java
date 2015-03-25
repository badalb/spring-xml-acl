package com.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.test.dao.FileRepository;
import com.test.dto.DirPermission;
import com.test.dto.Directory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:applicationContext-business.xml",
		"classpath:applicationContext-security.xml" })
public class FileRepositoryTest implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

	@Autowired
	private FileRepository repository;
	@Autowired
	JdbcUserDetailsManager userManager;

	@Test
	public void testAddFile() {
		// has ROLE_SECURITY
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				"ranjan", "pass3");
		SecurityContextHolder.getContext().setAuthentication(token);

		String user = (String) token.getPrincipal();
		UserDetails ud = userManager.loadUserByUsername(user);

		Collection<GrantedAuthority> authorities = ud.getAuthorities();
		String group = null;
		for (GrantedAuthority authority : authorities) {
			group = authority.getAuthority();
			if (!group.startsWith("ROLE_")) {
				break;
			}
		}

		try {
			// set perms to u=rw,g=r
			long perms = (DirPermission.OWNER_READ.value()
					+ DirPermission.OWNER_WRITE.value() + DirPermission.GROUP_READ
					.value());

			Directory file = repository.create("/hello/world", user, group, perms);
			repository.create("/hello/ranj", user, group, perms);
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testGetFile() {
		try {
			Directory file = repository.get("/hello/world");
			assertEquals("ranjan", file.getOwner());

			file = repository.get("/hello/ranj");
			assertEquals("ranjan", file.getOwner());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testDeleteFile() {
		try {
			repository.delete("/hello/world");
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testUpdateFile() {
		try {
			Directory file = repository.get("/hello/ranj");
			assertEquals("ranjan", file.getOwner());
			file.setContents("bla blah");
			repository.update("/hello/ranj", file);

			file = repository.get("/hello/ranj");
			assertEquals("bla blah", file.getContents());
		} catch (FileNotFoundException e) {
			fail(e.toString());
		}
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		applicationContext = arg0;

	}
}
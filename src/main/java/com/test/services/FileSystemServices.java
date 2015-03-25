package com.test.services;

import java.io.FileNotFoundException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import com.test.dao.FileRepository;
import com.test.dto.Directory;
import com.test.infrastructure.FileUploader;

@Service
public class FileSystemServices {
	@Autowired
	private FileRepository repository;
	@Autowired
	JdbcUserDetailsManager userManager;
	@Autowired
	AclCache cache;
	@Value("#{applicationProperties.umask}")
	long umask = 0;

	@Secured({ "ROLE_ADMIN", "ACL_FILE_CREATE" })
	public Directory create(String path, String fileName, String sourcePath)
			throws FileNotFoundException {
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		User user = (User) authentication.getPrincipal();
		UserDetails ud = userManager.loadUserByUsername(user.getUsername());

		Collection<GrantedAuthority> authorities = ud.getAuthorities();
		String group = null;
		for (GrantedAuthority authority : authorities) {
			group = authority.getAuthority();
			if (!group.startsWith("ROLE_")) {
				break;
			}
		}

		//update db here
		Directory dir = repository.create(path + fileName, user.getUsername(),
				group, umask);
		FileUploader.copyFile(sourcePath, path+fileName);
		
		return dir;
	}

	@Secured({ "ROLE_ADMIN", "ACL_FILE_WRITE" })
	public void update(String path, Directory file) throws FileNotFoundException {
		// evict the ACL from cache
		cache.evictFromCache(path);
		repository.update(path, file);
	}

	@Secured({ "ROLE_ADMIN", "ACL_FILE_READ" })
	public Directory read(String path) throws FileNotFoundException {
		return repository.get(path);
	}
}
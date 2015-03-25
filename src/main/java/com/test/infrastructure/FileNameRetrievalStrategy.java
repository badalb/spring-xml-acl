package com.test.infrastructure;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;


public class FileNameRetrievalStrategy implements
		ObjectIdentityRetrievalStrategy {

	public ObjectIdentity getObjectIdentity(Object domainObject) {
		// File article = (File) domainObject;
		// return new ObjectIdentityImpl(File.class, article.getName());

		String path = (String) domainObject;
		return new ObjectIdentityImpl(String.class, path);
	}

}
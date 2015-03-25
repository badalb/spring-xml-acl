package com.test.infrastructure;

import java.util.List;
import java.util.Map;

import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;


public class CustomLookupStrategy implements LookupStrategy {

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> arg0,
			List<Sid> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}

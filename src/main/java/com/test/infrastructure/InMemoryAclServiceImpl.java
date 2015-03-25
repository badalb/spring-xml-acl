package com.test.infrastructure;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.test.dao.FileRepository;
import com.test.dto.DirPermission;
import com.test.dto.Directory;


@Service
public class InMemoryAclServiceImpl implements AclService {
	@Autowired
	private FileRepository repository;
	@Autowired
	AclCache cache;
	private final Log logger = LogFactory.getLog(InMemoryAclServiceImpl.class);

	public Acl readAclById(ObjectIdentity object, List<Sid> sids)
			throws NotFoundException {

		ArrayList<ObjectIdentity> objects = new ArrayList<ObjectIdentity>();
		objects.add(object);

		Map<ObjectIdentity, Acl> map = readAclsById(objects, sids);
		Assert.isTrue(map.containsKey(object),
				"There should have been an Acl entry for ObjectIdentity "
						+ object);

		return map.get(object);
	}

	public Acl readAclById(ObjectIdentity object) throws NotFoundException {
		return readAclById(object, null);
	}

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects)
			throws NotFoundException {
		return readAclsById(objects, null);
	}

	public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects,
			List<Sid> sids) throws NotFoundException {
		Map<ObjectIdentity, Acl> result = new HashMap<ObjectIdentity, Acl>();

		for (ObjectIdentity object : objects) {
			MutableAcl acl = cache.getFromCache(object);
			if (acl != null) {
				logger.info("Using acl value in cache " + acl);
				result.put(object, acl);
			} else {
				/*
				 * The concept is that each secure domain object has an
				 * associated ACL that defines who can do what with that domain
				 * object
				 * 
				 * Create an object identity for the File, check the permissions
				 * flag to set the ACL here we work with owner and admin
				 * permissions are access, admin, create, delete, read, write
				 * inject Admin User Role
				 */
				Directory file;
				try {
					file = repository.get((String) object.getIdentifier());
				} catch (FileNotFoundException e) {
					throw new NotFoundException(e.toString());
				}
				long perms = file.getPerms();

				ObjectIdentity fileId = new ObjectIdentityImpl(String.class,
						object.getIdentifier());

				// Owner Acl

				CumulativePermission ownerPermission = new CumulativePermission();
				if (DirPermission.OWNER_WRITE.isSet(perms)) {
					ownerPermission.set(BasePermission.WRITE);
				}
				if (DirPermission.OWNER_READ.isSet(perms)) {
					ownerPermission.set(BasePermission.READ);
				}
				if (DirPermission.OWNER_CREATE.isSet(perms)) {
					ownerPermission.set(BasePermission.CREATE);
				}
				acl = new SimpleAclImpl(fileId,
						new ArrayList<AccessControlEntry>());
				List<AccessControlEntry> entries = acl.getEntries();
				entries.add(new AccessControlEntryImpl("aceOwner", acl,
						new PrincipalSid(file.getOwner()), ownerPermission,
						true, true, true));

				// Group Permissions
				CumulativePermission groupPermission = new CumulativePermission();
				if (DirPermission.GROUP_WRITE.isSet(perms)) {
					groupPermission.set(BasePermission.WRITE);
				}
				if (DirPermission.GROUP_READ.isSet(perms)) {
					groupPermission.set(BasePermission.READ);
				}
				if (DirPermission.GROUP_CREATE.isSet(perms)) {
					groupPermission.set(BasePermission.CREATE);
				}
				entries.add(new AccessControlEntryImpl("aceGroup", acl,
						new GrantedAuthoritySid(file.getGroup()),
						groupPermission, true, true, true));

				// Other Permissions
				CumulativePermission otherPermission = new CumulativePermission();
				if (DirPermission.OTHER_WRITE.isSet(perms)) {
					otherPermission.set(BasePermission.WRITE);
				}
				if (DirPermission.OTHER_READ.isSet(perms)) {
					otherPermission.set(BasePermission.READ);
				}
				if (DirPermission.OTHER_CREATE.isSet(perms)) {
					otherPermission.set(BasePermission.CREATE);
				}
				// need default group
				entries.add(new AccessControlEntryImpl("aceOther", acl,
						new GrantedAuthoritySid("*"), otherPermission, true,
						true, true));

				cache.putInCache(acl);
				result.put(object, acl);
			}
		}

		return result;
	}

	public List<ObjectIdentity> findChildren(ObjectIdentity arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
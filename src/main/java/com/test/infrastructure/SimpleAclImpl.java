package com.test.infrastructure;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.acls.domain.AuditLogger;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;

public class SimpleAclImpl implements MutableAcl {
	private static final long serialVersionUID = 1L;
	private ObjectIdentity oi;
	private List<AccessControlEntry> aces;
	private transient AuditLogger auditLogger = new ConsoleAuditLogger();
	private final Log logger = LogFactory.getLog(InMemoryAclServiceImpl.class);

	public SimpleAclImpl(ObjectIdentity oi, List<AccessControlEntry> aces) {
		this.oi = oi;
		this.aces = aces;
	}

	public List<AccessControlEntry> getEntries() {
		return aces;
	}

	public ObjectIdentity getObjectIdentity() {
		return oi;
	}

	public Sid getOwner() {
		return null; // owner concept is optional, we don't use it
	}

	public Acl getParentAcl() {
		return null; // we don't use inheritance
	}

	public boolean isEntriesInheriting() {
		return false; // we don't use inheritance
	}

	
	public boolean isGranted(List<Permission> permissions, List<Sid> sids,
			boolean administrativeMode) throws NotFoundException {

		AccessControlEntry firstRejection = null;

		/**
		 * Loop over all supplied permissions
		 */
		for (Permission permission : permissions) {
			/*
			 * For each permission loop over security identifiers
			 */
			for (Sid sid : sids) {
				// Attempt to find exact match for this permission mask and SID
				boolean scanNextSid = true;

				for (AccessControlEntry ace : aces) {

					logger.info(permission + " " + ace.getPermission());

					if (((ace.getPermission().getMask() & permission.getMask()) != 0)) {
						logger.info(sid + " getSid: " + ace.getSid());
						logger.info("id " + ace.getId());

						if (ace.getSid().equals(sid)
								|| ace.getId().equals("aceOther")) {
							/*
							 * Found a matching ACE, so its authorization
							 * decision will prevail
							 */
							if (ace.isGranting()) {
								// Success
								if (!administrativeMode) {
									auditLogger.logIfNeeded(true, ace);
								}

								return true;
							} else {
								/*
								 * Failure for this permission, so stop search
								 * We will see if they have a different
								 * permission (this permission is 100% rejected
								 * for this SID)
								 */
								if (firstRejection == null) {
									// Store first rejection for auditing
									// reasons
									firstRejection = ace;
								}

								scanNextSid = false; // helps break the loop

								break; // exit "aces" loop
							}
						}
					}
				}// for:ace

				if (!scanNextSid) {
					break; // exit SID for loop (now try next permission)
				}
			}// for:sid
		}// for:permission

		if (firstRejection != null) {
			/*
			 * We found an ACE to reject the request at this point, as no other
			 * ACEs were found that granted a different permission
			 */
			if (!administrativeMode) {
				auditLogger.logIfNeeded(false, firstRejection);
			}

			return false;
		}

		throw new NotFoundException(
				"Unable to locate a matching ACE for passed permissions and SIDs");
	}

	public boolean isSidLoaded(List<Sid> arg0) {
		/*
		 * we use in-memory structure, not external DB, so all entries are (if I
		 * correctly understand meaning of this method)
		 */
		return true;
	}

	public void deleteAce(int arg0) throws NotFoundException {
		// TODO Auto-generated method stub

	}

	public Serializable getId() {
		return oi.getIdentifier();
	}

	public void insertAce(int arg0, Permission arg1, Sid arg2, boolean arg3)
			throws NotFoundException {
		// TODO Auto-generated method stub

	}

	public void setEntriesInheriting(boolean arg0) {
		// TODO Auto-generated method stub

	}

	public void setOwner(Sid arg0) {
		// TODO Auto-generated method stub

	}

	public void setParent(Acl arg0) {
		// TODO Auto-generated method stub

	}

	public void updateAce(int arg0, Permission arg1) throws NotFoundException {
		// TODO Auto-generated method stub

	}

}
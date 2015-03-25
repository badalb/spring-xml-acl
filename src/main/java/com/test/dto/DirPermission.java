package com.test.dto;

/* 
 * Helper for setting permissions
 * 
 * Permissions field:
 * 16   ,8     ,4     ,2   ,1
 * admin,create,delete,read,write
 * repeated for owner,group,other
 */
public enum DirPermission {
	// OTHER PERMISSIONS
	OTHER_WRITE (1 << 0), // 1
	OTHER_READ (1 << 1), // 2
	OTHER_DELETE (1 << 2), // 4
	OTHER_CREATE (1 << 3), // 8
	OTHER_ADMIN (1 << 4), // 16
	// GROUP PERMISSIONS
	GROUP_WRITE (1 << 5), // 32
	GROUP_READ (1 << 6), // 64
	GROUP_DELETE (1 << 7), // 128
	GROUP_CREATE (1 << 8), // 256
	GROUP_ADMIN (1 << 9), // 512
	// OWNER PERMISSIONS
	OWNER_WRITE(1 << 10), // 1024
	OWNER_READ(1 << 11), // 2048
	OWNER_DELETE(1 << 12), // 4096
	OWNER_CREATE(1 << 13), // 8192
	OWNER_ADMIN(1 << 14); // 16254
	
	private final long perm;

	private DirPermission(int p) {
		perm = p;
	}

	public long value() {
		return perm;
	}

	public boolean isSet(long perm) {
		if ((perm & this.perm) != 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {

		long perms = DirPermission.OWNER_READ.value()
				+ DirPermission.OWNER_WRITE.value();
		System.out.println("perms " + perms);
		boolean v = DirPermission.OWNER_READ.isSet(perms);
		System.out.println("perms " + v);
		boolean v2 = DirPermission.OWNER_DELETE.isSet(perms);
		System.out.println("perms " + v2);
	}

}

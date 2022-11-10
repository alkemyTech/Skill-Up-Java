package com.alkemy.wallet.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum RoleList implements GrantedAuthority{
    ADMIN, USER;
	
	public String getAuthority() {
		return name();
	}
}

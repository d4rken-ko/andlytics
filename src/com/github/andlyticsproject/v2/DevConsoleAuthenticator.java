package com.github.andlyticsproject.v2;

import com.github.andlyticsproject.exception.AuthenticationException;

public interface DevConsoleAuthenticator {

	String getAccountName();

	// XXX uglish
	// Get rid of the whole exception package thing
	AuthInfo authenticate() throws AuthenticationException;
}

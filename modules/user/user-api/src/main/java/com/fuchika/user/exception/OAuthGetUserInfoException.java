package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public class OAuthGetUserInfoException extends BaseException {

	public OAuthGetUserInfoException(String message) {
		super(500, "Connot get user info from oauth.", message);
	}
}

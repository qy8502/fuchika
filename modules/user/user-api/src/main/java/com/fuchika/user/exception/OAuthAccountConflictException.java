package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;
import com.fuchika.user.model.OAuthAccountProvider;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public class OAuthAccountConflictException extends BaseException {

	public OAuthAccountConflictException(OAuthAccountProvider provider) {
		super(500, String.format(
				"There is already a %s account that belongs to you",
				provider.capitalize()), String.format(
				"There is already a %s account that belongs to you",
				provider.capitalize()));
	}
}

package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;
import com.fuchika.user.model.OAuthAccountProvider;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public class OAuthAccountOnlyException extends BaseException {

	public OAuthAccountOnlyException(OAuthAccountProvider provider) {
		super(500,
				String.format("Could not unlink %s account because it is your only sign-in method",
						provider.capitalize()),
				String.format("Could not unlink %s account because it is your only sign-in method",
						provider.capitalize()));
	}
}

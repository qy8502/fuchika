package com.fuchika.oauth2.exception;


import com.fuchika.exception.BaseException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 25/04/2013
 */
public class AuthorizationException extends BaseException {

    public AuthorizationException(String applicationMessage) {
        super(403, "Not authorized", applicationMessage);
    }

}

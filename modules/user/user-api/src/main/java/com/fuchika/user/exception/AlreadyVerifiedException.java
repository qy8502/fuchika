package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;

/**
 * @version 1.0
 * @author: Iain Porter
 * @since 13/05/2013
 */
public class AlreadyVerifiedException extends BaseException {

    public AlreadyVerifiedException() {
        super(409, "Already verified", "The token has already been verified");
    }
}

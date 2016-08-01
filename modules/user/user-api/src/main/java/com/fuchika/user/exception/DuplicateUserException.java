package com.fuchika.user.exception;

import com.fuchika.exception.BaseException;

public class DuplicateUserException extends BaseException {

    public DuplicateUserException() {
        super(409, "User already exists", "An attempt was made to create a user that already exists");
    }
}


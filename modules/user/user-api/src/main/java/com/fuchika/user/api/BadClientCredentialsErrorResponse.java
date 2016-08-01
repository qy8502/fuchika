package com.fuchika.user.api;

import com.fuchika.api.ErrorResponse;

/**
 * Created by iainporter on 06/11/2014.
 */
public class BadClientCredentialsErrorResponse extends ErrorResponse {

    public BadClientCredentialsErrorResponse(String errorMessage) {
        super("401", "Client Credentials were incorrect", "Client Credentials were incorrect. Useage: Base64Encode(username:password) ");
    }
}

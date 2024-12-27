package com.grit.backend.server;

public class HttpResponse {

    public static String createJsonResponse(String message, int statusCode) {
        return "{ \"message\": \"" + message + "\", \"status\": " + statusCode + " }";
    }
}
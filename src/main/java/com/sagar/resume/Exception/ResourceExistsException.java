package com.sagar.resume.Exception;

public class ResourceExistsException extends RuntimeException{

    public ResourceExistsException(String message)
    {
        super(message);
    }
}

package com.blog.platform.blog.platform.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String resource , String field , Object value){
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}

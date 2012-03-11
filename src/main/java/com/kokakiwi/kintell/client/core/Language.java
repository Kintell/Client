package com.kokakiwi.kintell.client.core;

public class Language
{
    private final String name;
    private final String contentType;
    
    public Language(String name, String contentType)
    {
        this.name = name;
        this.contentType = contentType;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getContentType()
    {
        return contentType;
    }
}

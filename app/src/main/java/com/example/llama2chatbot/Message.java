package com.example.llama2chatbot;

public class Message {
    private String sender;
    private String content;

    public Message(String sender, String content)
    {
        this.sender = sender;
        this.content = content;
    }

    public String getSender()
    {
        return sender;
    }

    public String getContent()
    {
        return content;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}

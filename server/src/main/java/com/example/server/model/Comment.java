package com.example.server.model;

import java.util.Date;

public class Comment {

    private String text;
    private Date timestamp;
    private Integer characterId; 

    public Integer getCharacterId() {
        return characterId;
    }
    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    
}

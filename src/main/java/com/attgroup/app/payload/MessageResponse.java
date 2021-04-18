package com.attgroup.app.payload;

import lombok.Data;

@Data
public class MessageResponse {
    public MessageResponse(String message) {
    	this.message = message;
    }
	private String message;
}
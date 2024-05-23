package com.ebook.service;

public interface EmailService {
    public void sendMessage(String from, String to, String subject, String text);
}

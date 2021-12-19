package com.example.hw05;

public interface CommunicationInterface {
    void goToForums();
    void goToForum(Forum forum);
    void goToCreateForum(String name, String uid);
    void popBackStack();
    void goToRegister();
    void goToLogin();
    void logout();
}

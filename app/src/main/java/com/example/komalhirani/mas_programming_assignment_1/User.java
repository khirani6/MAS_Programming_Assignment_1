package com.example.komalhirani.mas_programming_assignment_1;

public class User {

    private String mFirstName;
    private String mLastName;
    private String mEmail;

    public User() {

    }

    User(String firstName, String lastName, String email) {
        mFirstName = firstName;
        mLastName = lastName;
        mEmail = email;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    /**
     * Firebase does not allow us to store "." in the database, so this allows us to encode it
     * for storage
     */
    public String getEncodedEmail() {
        return mEmail.replace('.', ',');
    }

    public void setEmail(String email) {
        mEmail = email;
    }
}

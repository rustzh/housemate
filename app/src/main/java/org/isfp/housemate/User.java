package org.isfp.housemate;

public class User {

    public String uid;
    public String name;
    public String email;
    public String password;
    public String number;
    public String friendNumber;
    public String profileImageUrl;

    public User(){

    }

    public User(String uid, String name, String email, String password, String profileImageUrl){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
    }

    public User(String number, String friendNumber){
        this.number = number;
        this.friendNumber = friendNumber;
    }
}

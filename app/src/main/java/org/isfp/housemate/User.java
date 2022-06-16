package org.isfp.housemate;

public class User {

    public String id;
    public String name;
    public String email;
    public String number;
    public String friend_number;

    public User(){

    }

    public User(String id, String email, String name){
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User(String number, String friend_number){
        this.number = number;
        this.friend_number = friend_number;
    }
}

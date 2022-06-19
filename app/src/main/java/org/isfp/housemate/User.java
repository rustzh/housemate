package org.isfp.housemate;

import java.io.Serializable;

public class User implements Serializable {

    public String uid;
    public String name;
    public String email;
    public String password;
    public String number;
    public String friendNumber;
    public String profileImageURL;
    public String connectState;
    public String dataRoomNumber;


    public User(){

    }

    public User(String uid, String name, String email, String password, String profileImageURL){
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImageURL = profileImageURL;
    }

    public User(String number, String friendNumber, String dataRoomNumber){
        this.number = number;
        this.friendNumber = friendNumber;
        this.dataRoomNumber = dataRoomNumber;
    }
}

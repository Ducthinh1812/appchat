package com.example.appchat;

public class messent {
    int idmes;
    String name;
    String friend;
    String mesegent;

    public messent() {
    }

    public messent(int idmes, String name, String friend, String mesegent) {
        this.idmes = idmes;
        this.name = name;
        this.friend = friend;
        this.mesegent = mesegent;
    }

    public int getIdmes() {
        return idmes;
    }

    public void setIdmes(int idmes) {
        this.idmes = idmes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getMesegent() {
        return mesegent;
    }

    public void setMesegent(String mesegent) {
        this.mesegent = mesegent;
    }
}

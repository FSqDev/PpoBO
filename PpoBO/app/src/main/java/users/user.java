package users;

import java.util.ArrayList;

public class User {
    // === Instance Variables ===
    private String name;
    private String phoneNum;
    private String email;
    private String userType;
    private ArrayList<User> people;

    public User(String name, String phoneNum, String email, String userType){
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.userType = userType;
    }
    // === Methods ===
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType(){
        return userType;
    }

    public void addPerson(User person){
        people.add(person);
    }

    public void removePerson(User person){
        people.remove(person);
    }

    public ArrayList<User> getPeople(){
        return people;
    }
}

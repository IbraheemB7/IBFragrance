package IBFragrance.Badran.ibfragrance.data;

public class MyUser {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String id;

// Constructor with all fields
public MyUser(String firstName, String lastName, String email, String password, String phone, String id) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.id = id;

}

// Constructor with no arguments
public MyUser() {
}

// Getters and Setters
public String getFirstName() {
    return firstName;
}

public void setFirstName(String firstName) {
    this.firstName = firstName;
}

public String getLastName() {
    return lastName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

public String getPhone() {
    return phone;
}

public void setPhone(String phone) {
    this.phone = phone;
}

public String getID() {
    return this.id;
}
public void setID(String id) {
    this.id = id;
}



// toString method
@Override
public String toString() {
    return "MyUser{" +
            "firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            '}';
}




}

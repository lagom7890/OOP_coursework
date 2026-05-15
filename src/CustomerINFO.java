public class CustomerINFO {
    private final long phoneNumber;
    private final String email;

    private final String gender;
    private final String name;
    private final int age;
    private final String address;
    private final int id;

    private final String notes;

    public CustomerINFO(String gender, String name, int age, int id, long phoneNumber,
                        String email, String address, String notes) {
        this.gender = gender;
        this.name = name;
        this.age = age;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.notes = notes;
    }

    public String getGender(){
        return gender;
    }
    public String getName() {
        return name;
    }
    public int getAge(){
        return age;
    }
    public int getId() {
        return id;
    }
    public long getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public String getNotes() {
        return notes;
    }
}

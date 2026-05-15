import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerManagement extends Subject {
    private final List<CustomerINFO> customers;

    public CustomerManagement() {
        this.customers = new ArrayList<>();
    }
    public void addCustomer(CustomerINFO customer) {
        this.customers.add(customer);
        notifyObservers("New customer added: " + customer.getName());
    }

    public List<CustomerINFO> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public List<CustomerINFO> searchByName(String name) {
        return customers.stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    public CustomerINFO searchById(int id) {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
    public List<CustomerINFO> searchByContact(String query) {
        return customers.stream()
                .filter(c -> c.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                        String.valueOf(c.getPhoneNumber()).contains(query))
                .collect(Collectors.toList());
    }

    public List<CustomerINFO> filterByGender(String gender) {
        return customers.stream()
                .filter(c -> c.getGender().equalsIgnoreCase(gender))
                .collect(Collectors.toList());
    }
    public List<CustomerINFO> filterByAgeRange(int minAge, int maxAge) {
        return customers.stream()
                .filter(c -> c.getAge() >= minAge && c.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

}
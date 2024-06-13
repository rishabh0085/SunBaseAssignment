package SunBaseDrive4.demo.Service;

import SunBaseDrive4.demo.Entities.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private SunBaseDrive4.demo.Repository.customerRepository customerRepository;

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setFirstName(customerDetails.getFirstName());
            customer.setLastName(customerDetails.getLastName());
            customer.setStreet(customerDetails.getStreet());
            customer.setAddress(customerDetails.getAddress());
            customer.setCity(customerDetails.getCity());
            customer.setState(customerDetails.getState());
            customer.setEmail(customerDetails.getEmail());
            customer.setPhone(customerDetails.getPhone());
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}

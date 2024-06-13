package SunBaseDrive4.demo.Controller;

import SunBaseDrive4.demo.DTOS.AuthenticationRequest;
import SunBaseDrive4.demo.DTOS.AuthenticationResponse;
import SunBaseDrive4.demo.DTOS.RemoteCustomer;
import SunBaseDrive4.demo.Entities.Customer;
import SunBaseDrive4.demo.Repository.customerRepository;
import SunBaseDrive4.demo.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/{id}")
    public Optional<Customer> getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @GetMapping
    public Page<Customer> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        return customerService.updateCustomer(id, customerDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @Autowired
    private customerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${remote.api.auth.url}")
    private String authUrl;

    @Value("${remote.api.customer.url}")
    private String customerUrl;

    @Value("${remote.api.login_id}")
    private String loginId;

    @Value("${remote.api.password}")
    private String password;

    @GetMapping("/sync")
    public void syncCustomers() {
        String token = getRemoteApiToken();
        List<Customer> remoteCustomers = fetchRemoteCustomers(token);
        for (Customer remoteCustomer : remoteCustomers) {
            Optional<Customer> existingCustomer = customerRepository.findById(remoteCustomer.getId());
            if (existingCustomer.isPresent()) {
                Customer existing = existingCustomer.get();
                existing.setFirstName(remoteCustomer.getFirstName());
                existing.setLastName(remoteCustomer.getLastName());
                existing.setStreet(remoteCustomer.getStreet());
                existing.setAddress(remoteCustomer.getAddress());
                existing.setCity(remoteCustomer.getCity());
                existing.setState(remoteCustomer.getState());
                existing.setEmail(remoteCustomer.getEmail());
                existing.setPhone(remoteCustomer.getPhone());
                customerRepository.save(existing);
            } else {
                customerRepository.save(remoteCustomer);
            }
        }
    }

    private String getRemoteApiToken() {
        AuthenticationRequest authRequest = new AuthenticationRequest();
        authRequest.setLogin_id(loginId);
        authRequest.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthenticationRequest> request = new HttpEntity<>(authRequest, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(authUrl, request, AuthenticationResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getToken();
        } else {
            throw new RuntimeException("Failed to get token from remote API");
        }
    }

    private List<Customer> fetchRemoteCustomers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<RemoteCustomer[]> response = restTemplate.exchange(
                customerUrl + "?cmd=get_customer_list",
                HttpMethod.GET,
                request,
                RemoteCustomer[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            RemoteCustomer[] remoteCustomers = response.getBody();
            List<Customer> customers = new ArrayList<>();
            for (RemoteCustomer rc : remoteCustomers) {
                Customer customer = new Customer();
                try {
                    customer.setId(Long.parseLong(rc.getUuid())); // Convert UUID to Long
                } catch (NumberFormatException e) {
                    // Handle the case where UUID cannot be converted to Long
                    System.err.println("Invalid UUID format for customer ID: " + rc.getUuid());
                    continue; // Skip this customer if ID is not convertible
                }
                customer.setFirstName(rc.getFirst_name());
                customer.setLastName(rc.getLast_name());
                customer.setStreet(rc.getStreet());
                customer.setAddress(rc.getAddress());
                customer.setCity(rc.getCity());
                customer.setState(rc.getState());
                customer.setEmail(rc.getEmail());
                customer.setPhone(rc.getPhone());
                customers.add(customer);
            }
            return customers;
        } else {
            throw new RuntimeException("Failed to fetch customers from remote API");
        }
    }

}

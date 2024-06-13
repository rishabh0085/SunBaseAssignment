package SunBaseDrive4.demo.Repository;

import SunBaseDrive4.demo.Entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerRepository extends JpaRepository<Customer, Long> {
}

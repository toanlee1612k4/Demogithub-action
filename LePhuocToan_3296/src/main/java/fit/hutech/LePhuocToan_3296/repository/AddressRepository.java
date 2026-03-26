package fit.hutech.LePhuocToan_3296.repository;

import fit.hutech.LePhuocToan_3296.entity.Address;
import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
    
    Optional<Address> findByUserAndIsDefaultTrue(User user);
}
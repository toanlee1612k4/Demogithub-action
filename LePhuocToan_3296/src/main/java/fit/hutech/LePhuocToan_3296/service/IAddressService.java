package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.Address;
import fit.hutech.LePhuocToan_3296.entity.User;
import java.util.List;

public interface IAddressService {
    List<Address> getAddressesByUser(User user);
    Address getDefaultAddress(User user);
    Address saveAddress(Address address);
    void deleteAddress(Long id);
    void setDefaultAddress(Long addressId, User user);
}
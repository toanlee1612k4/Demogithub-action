package fit.hutech.LePhuocToan_3296.service;

import fit.hutech.LePhuocToan_3296.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User findByUsername(String username);
    User saveUser(User user);
    void updateProfile(User user);
    void changePassword(User user, String oldPassword, String newPassword);
    void toggleUserStatus(Long userId);
    Page<User> getAllUsers(Pageable pageable);
}
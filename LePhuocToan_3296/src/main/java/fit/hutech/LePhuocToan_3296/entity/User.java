package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String fullname;
    
    @Column(length = 50)
    private String provider = "LOCAL"; 
    
    @Column(length = 50)
    private String role = "USER";
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    // Quan hệ Nhiều-Nhiều với Role (bảng riêng)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
    
    // Quan hệ 1-N với Orders
    @OneToMany(mappedBy = "user")
    private List<Orders> orders;
    
    // Quan hệ 1-N với Cart
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts;
    
    // Quan hệ 1-N với Address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
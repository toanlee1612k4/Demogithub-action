package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_line")
    private String addressLine;
    
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
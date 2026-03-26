package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"category", "bookImages", "orderDetails"})
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String description;
    
    @Column(name = "release_date")
    private LocalDateTime releaseDate;
    
    private Integer pages;
    
    @Column(nullable = false)
    private Double price;
    
    private String image; // Ảnh chính
    
    private Integer quantity; // Tồn kho
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false; // Soft Delete
    
    @Version // Optimistic Locking
    private Integer version = 0;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "updated_by")
    private String updatedBy;

    // Quan hệ N-1 với Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Quan hệ 1-N với BookImage
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImage> bookImages;
    
    // Quan hệ 1-N với OrderDetail
    @OneToMany(mappedBy = "book")
    private List<OrderDetail> orderDetails;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
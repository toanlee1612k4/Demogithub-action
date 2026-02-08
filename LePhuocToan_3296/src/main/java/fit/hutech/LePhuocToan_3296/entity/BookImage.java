package fit.hutech.LePhuocToan_3296.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "book_image")
public class BookImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
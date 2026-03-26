package fit.hutech.LePhuocToan_3296.config;

import fit.hutech.LePhuocToan_3296.entity.*;
import fit.hutech.LePhuocToan_3296.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        createRoles();
        createAdminUser();
        createCategories();
        createBooks();
        log.info("Data initialization completed!");
    }

    private void createRoles() {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            adminRole.setDescription("Quản trị viên hệ thống");
            roleRepository.save(adminRole);
            log.info("Created ROLE_ADMIN");
        }
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            userRole.setDescription("Người dùng thông thường");
            roleRepository.save(userRole);
            log.info("Created ROLE_USER");
        }
    }

    private void createAdminUser() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();
        
        java.util.Optional<User> existing = userRepository.findByUsername("admin");
        if (existing.isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@jbookstore.com");
            admin.setFullname("Administrator");
            admin.setPhoneNumber("0901234567");
            admin.setProvider("LOCAL");
            admin.setRole("ADMIN");
            admin.setIsActive(true);
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            log.info("Admin account created: admin");
        } else {
            // Ensure existing admin has correct password and roles
            User admin = existing.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setIsActive(true);
            admin.setRole("ADMIN");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
            admin.setRoles(roles);
            userRepository.save(admin);
            log.info("Admin account updated: admin");
        }
        
        // Create second admin account
        java.util.Optional<User> existing2 = userRepository.findByUsername("admin2");
        if (existing2.isEmpty()) {
            User admin2 = new User();
            admin2.setUsername("admin2");
            admin2.setPassword(passwordEncoder.encode("admin456"));
            admin2.setEmail("admin2@jbookstore.com");
            admin2.setFullname("Quản trị viên 2");
            admin2.setPhoneNumber("0912345678");
            admin2.setProvider("LOCAL");
            admin2.setRole("ADMIN");
            admin2.setIsActive(true);
            Set<Role> roles2 = new HashSet<>();
            roles2.add(adminRole);
            roles2.add(userRole);
            admin2.setRoles(roles2);
            userRepository.save(admin2);
            log.info("Admin2 account created: admin2");
        } else {
            User admin2 = existing2.get();
            admin2.setPassword(passwordEncoder.encode("admin456"));
            admin2.setIsActive(true);
            admin2.setRole("ADMIN");
            Set<Role> roles2 = new HashSet<>();
            roles2.add(adminRole);
            roles2.add(userRole);
            admin2.setRoles(roles2);
            userRepository.save(admin2);
            log.info("Admin2 account updated: admin2");
        }
    }

    private void createCategories() {
        String[] names = {"Văn học", "Khoa học", "Công nghệ", "Kinh tế", "Thiếu nhi", "Tâm lý - Kỹ năng sống", "Lịch sử", "Ngoại ngữ"};
        for (String name : names) {
            if (!categoryRepository.existsByName(name)) {
                Category cat = new Category();
                cat.setName(name);
                categoryRepository.save(cat);
                log.info("Created category: {}", name);
            }
        }
    }

    private void createBooks() {
        if (bookRepository.count() > 0) {
            log.info("Books already exist, skipping...");
            return;
        }

        Category vanHoc = categoryRepository.findByName("Văn học").orElse(null);
        Category khoaHoc = categoryRepository.findByName("Khoa học").orElse(null);
        Category congNghe = categoryRepository.findByName("Công nghệ").orElse(null);
        Category kinhTe = categoryRepository.findByName("Kinh tế").orElse(null);
        Category thieuNhi = categoryRepository.findByName("Thiếu nhi").orElse(null);
        Category tamLy = categoryRepository.findByName("Tâm lý - Kỹ năng sống").orElse(null);
        Category lichSu = categoryRepository.findByName("Lịch sử").orElse(null);
        Category ngoaiNgu = categoryRepository.findByName("Ngoại ngữ").orElse(null);

        // Văn học
        createBook("Nhà Giả Kim", "Paulo Coelho",
                "Tiểu thuyết được đọc nhiều nhất thế giới, kể về hành trình theo đuổi giấc mơ của chàng chăn cừu Santiago.",
                79000.0, 150, vanHoc,
                "https://salt.tikicdn.com/cache/w1200/ts/product/45/3b/fc/aa81d0a534b45706ae1eee1e344e80d9.jpg", 320);

        createBook("Đắc Nhân Tâm", "Dale Carnegie",
                "Cuốn sách kinh điển về nghệ thuật giao tiếp và ứng xử giúp bạn thành công trong cuộc sống.",
                68000.0, 200, tamLy,
                "https://salt.tikicdn.com/cache/w1200/ts/product/df/7d/da/d340edda2b0eacb7ddc47537cddb5e08.jpg", 320);

        createBook("Sapiens: Lược sử loài người", "Yuval Noah Harari",
                "Hành trình 70.000 năm phát triển của loài người từ thời kỳ đồ đá đến thế kỷ 21.",
                189000.0, 80, khoaHoc,
                "https://salt.tikicdn.com/cache/w1200/ts/product/0d/41/98/c0e853b0af0c6c7f0fae55638e0592e6.jpg", 560);

        createBook("Clean Code", "Robert C. Martin",
                "Hướng dẫn viết code sạch, dễ đọc và bảo trì - sách kinh điển cho lập trình viên.",
                350000.0, 50, congNghe,
                "https://salt.tikicdn.com/cache/w1200/ts/product/88/10/5a/2dab4840b3c995f3fa6ea4e0ad3c5eb4.jpg", 464);

        createBook("Dế Mèn Phiêu Lưu Ký", "Tô Hoài",
                "Tác phẩm kinh điển của văn học thiếu nhi Việt Nam kể về cuộc phiêu lưu của chú Dế Mèn.",
                45000.0, 300, thieuNhi,
                "https://salt.tikicdn.com/cache/w1200/ts/product/28/15/e3/ced30adfe9d51537699950d218670476.jpg", 180);

        createBook("Tuổi trẻ đáng giá bao nhiêu", "Rosie Nguyễn",
                "Cuốn sách truyền cảm hứng cho giới trẻ Việt Nam về việc tận dụng tuổi trẻ.",
                76000.0, 120, tamLy,
                "https://salt.tikicdn.com/cache/w1200/ts/product/88/2e/01/b4e441e56044bdc96115a5e4f684a085.jpg", 280);

        createBook("Lịch sử Việt Nam bằng tranh", "Trần Bạch Đằng",
                "Bộ sách tranh minh họa lịch sử Việt Nam sinh động từ thời dựng nước đến hiện đại.",
                95000.0, 90, lichSu,
                "https://salt.tikicdn.com/cache/w1200/ts/product/c4/13/7f/e9c8fe79cf2e8tried847ac0ecf8e9e8.jpg", 200);

        createBook("English Grammar in Use", "Raymond Murphy",
                "Sách ngữ pháp tiếng Anh bán chạy nhất thế giới dành cho trình độ trung cấp.",
                225000.0, 70, ngoaiNgu,
                "https://salt.tikicdn.com/cache/w1200/ts/product/70/fe/cd/d5808c6ede94af40cc501e4f7e2f03ee.jpg", 380);

        createBook("Nghĩ giàu làm giàu", "Napoleon Hill",
                "13 nguyên tắc vàng để đạt được thành công tài chính và sự giàu có.",
                89000.0, 160, kinhTe,
                "https://salt.tikicdn.com/cache/w1200/ts/product/97/c0/24/7bf95b23601a28a1feed41b575ce9c55.jpg", 350);

        createBook("Hoàng tử bé", "Antoine de Saint-Exupéry",
                "Câu chuyện về tình bạn, tình yêu và ý nghĩa cuộc sống qua lời kể của hoàng tử nhỏ.",
                55000.0, 250, vanHoc,
                "https://salt.tikicdn.com/cache/w1200/ts/product/ff/4d/a3/0c9e09e8f44d73a89db8f9aaeefc2c2c.jpg", 120);

        createBook("Tư duy nhanh và chậm", "Daniel Kahneman",
                "Cuốn sách đoạt giải Nobel về hai hệ thống tư duy chi phối mọi quyết định của con người.",
                199000.0, 60, tamLy,
                "https://salt.tikicdn.com/cache/w1200/ts/product/80/28/4d/f3401a1e635b73e4c44455bcfad5e3f7.jpg", 620);

        createBook("Atomic Habits", "James Clear",
                "Phương pháp xây dựng thói quen tốt và loại bỏ thói quen xấu từ những thay đổi nhỏ.",
                149000.0, 100, tamLy,
                "https://salt.tikicdn.com/cache/w1200/ts/product/53/02/c5/4d0e25f1dc3943f3c8417db9f5b8e6b5.jpg", 330);

        createBook("Java: The Complete Reference", "Herbert Schildt",
                "Tài liệu tham khảo toàn diện về lập trình Java từ cơ bản đến nâng cao.",
                420000.0, 40, congNghe,
                "https://salt.tikicdn.com/cache/w1200/ts/product/24/37/e5/3ed7003e6e9bb43b32bfda741cab9236.jpg", 1280);

        createBook("Chiến tranh và Hòa bình", "Leo Tolstoy",
                "Tiểu thuyết sử thi vĩ đại nhất của văn học Nga và thế giới về thời Napoleon.",
                165000.0, 55, vanHoc,
                "https://salt.tikicdn.com/cache/w1200/ts/product/eb/47/00/9e2ee82b2ad0ff8d996a42f9bdd60a3e.jpg", 1500);

        createBook("Sức mạnh của thói quen", "Charles Duhigg",
                "Hiểu cách thói quen hoạt động để thay đổi cuộc sống cá nhân và doanh nghiệp.",
                119000.0, 85, tamLy,
                "https://salt.tikicdn.com/cache/w1200/ts/product/3f/4c/13/6d3c3da5aa2c4d9ddf0a4e58ed488dd6.jpg", 400);

        createBook("Kinh tế học hài hước", "Steven D. Levitt",
                "Giải thích những hiện tượng đời thường bằng kinh tế học theo cách thú vị nhất.",
                108000.0, 75, kinhTe,
                "https://salt.tikicdn.com/cache/w1200/ts/product/63/e0/d3/d8e62e3b8e6ba1c7e01b7f71b8cbb2af.jpg", 330);

        log.info("Created 16 sample books with images");
    }

    private void createBook(String title, String author, String description,
                            Double price, Integer quantity, Category category,
                            String imageUrl, Integer pages) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setDescription(description);
        book.setPrice(price);
        book.setQuantity(quantity);
        book.setCategory(category);
        book.setImage(imageUrl);
        book.setPages(pages);
        book.setIsDeleted(false);
        book.setReleaseDate(LocalDateTime.now().minusDays((long)(Math.random() * 365)));
        bookRepository.save(book);
    }
}

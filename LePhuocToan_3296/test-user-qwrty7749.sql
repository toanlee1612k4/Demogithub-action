-- ============================================
-- TEST USER: qwrty7749
-- ============================================

USE ebookstore;

-- 1. KIỂM TRA USER CỤ THỂ
SELECT '========== THÔNG TIN USER qwrty7749 ==========' AS '';
SELECT 
    id,
    username,
    CONCAT('''', username, '''') as username_with_quotes,
    LENGTH(username) as username_length,
    email,
    fullname,
    provider,
    role,
    is_active,
    LENGTH(password) as password_length,
    SUBSTRING(password, 1, 20) as password_start,
    created_at,
    last_login,
    phone_number
FROM user
WHERE username = 'qwrty7749';

-- 2. KIỂM TRA ROLES CỦA USER NÀY
SELECT '========== ROLES CỦA qwrty7749 ==========' AS '';
SELECT 
    u.username,
    r.id as role_id,
    r.name as role_name,
    r.description as role_desc
FROM user u
LEFT JOIN user_role ur ON u.id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.id
WHERE u.username = 'qwrty7749';

-- 3. KIỂM TRA XEM CÓ USER NÀO GIỐNG NHAU KHÔNG (dấu cách, ký tự đặc biệt)
SELECT '========== TÌM USER TƯƠNG TỰ qwrty7749 ==========' AS '';
SELECT 
    id,
    username,
    CONCAT('''', username, '''') as with_quotes,
    LENGTH(username) as len,
    HEX(username) as hex_value
FROM user
WHERE username LIKE '%qwrty7749%' 
   OR username LIKE '%qwrty%';

-- 4. KIỂM TRA BẢNG ROLE
SELECT '========== TẤT CẢ ROLES ==========' AS '';
SELECT * FROM role;

-- ============================================
-- SỬA LỖI (nếu cần)
-- ============================================

-- A. NẾU USER KHÔNG CÓ ROLE
-- Uncomment dòng dưới để gán ROLE_USER
-- INSERT IGNORE INTO user_role (user_id, role_id)
-- SELECT u.id, r.id FROM user u, role r 
-- WHERE u.username = 'qwrty7749' AND r.name = 'ROLE_USER';

-- B. NẾU ROLE_USER CHƯA TỒN TẠI
-- Uncomment dòng dưới để tạo ROLE_USER
-- INSERT IGNORE INTO role (name, description, created_at) 
-- VALUES ('ROLE_USER', 'Người dùng thông thường', NOW());

-- C. NẾU is_active = 0 hoặc NULL
-- Uncomment dòng dưới để kích hoạt
-- UPDATE user SET is_active = 1 WHERE username = 'qwrty7749';

-- D. NẾU USERNAME CÓ DẤU CÁCH THỪA
-- Uncomment dòng dưới để xóa dấu cách
-- UPDATE user SET username = TRIM(username) WHERE username LIKE '% %';

-- ============================================
-- TEST PASSWORD (thay YOUR_RAW_PASSWORD)
-- ============================================

-- Để test password, dùng endpoint: http://localhost:8080/test-password
-- Username: qwrty7749
-- Password: (password bạn dùng khi đăng ký)

SELECT '========== HOÀN TẤT ==========' AS '';
SELECT 'Kiểm tra kết quả ở trên để tìm vấn đề' AS message;

-- ============================================
-- KIỂM TRA DATABASE CHO VẤN ĐỀ ĐĂNG NHẬP
-- ============================================

USE ebookstore;

-- 1. KIỂM TRA TẤT CẢ USER
SELECT '========== DANH SÁCH USER ==========' AS '';
SELECT 
    id,
    username,
    email,
    fullname,
    role,
    is_active,
    LENGTH(password) as password_length,
    CASE 
        WHEN LENGTH(password) = 60 THEN '✅ BCrypt (OK)'
        WHEN LENGTH(password) < 20 THEN '❌ Plain text!'
        ELSE '⚠️ Unknown format'
    END as password_status,
    created_at
FROM user
ORDER BY id;

-- 2. KIỂM TRA ROLE
SELECT '========== BẢNG ROLE ==========' AS '';
SELECT * FROM role;

-- 3. KIỂM TRA QUAN HỆ USER-ROLE
SELECT '========== USER-ROLE MAPPING ==========' AS '';
SELECT 
    u.id as user_id,
    u.username,
    u.role as role_string,
    r.name as role_from_table
FROM user u
LEFT JOIN user_role ur ON u.id = ur.user_id
LEFT JOIN role r ON ur.role_id = r.id
ORDER BY u.id;

-- 4. TÌM USER KHÔNG CÓ ROLE
SELECT '========== USER THIẾU ROLE ==========' AS '';
SELECT 
    u.id,
    u.username,
    u.role as role_string,
    'No role in user_role table!' as issue
FROM user u
WHERE NOT EXISTS (
    SELECT 1 FROM user_role ur WHERE ur.user_id = u.id
);

-- 5. TÌM USER KHÔNG ACTIVE
SELECT '========== USER BỊ VÔ HIỆU HÓA ==========' AS '';
SELECT 
    id,
    username,
    email,
    is_active
FROM user
WHERE is_active = 0 OR is_active IS NULL;

-- ============================================
-- CÁCH SỬA CÁC VẤN ĐỀ THƯỜNG GẶP
-- ============================================

-- A. TẠO ROLE NẾU THIẾU
INSERT IGNORE INTO role (name, description, created_at) 
VALUES 
    ('ROLE_USER', 'Người dùng thông thường', NOW()),
    ('ROLE_ADMIN', 'Quản trị viên', NOW());

-- B. GÁN ROLE CHO USER THIẾU ROLE (chạy sau khi có role)
-- Uncomment dòng dưới và thay YOUR_USERNAME
-- INSERT INTO user_role (user_id, role_id)
-- SELECT u.id, r.id FROM user u, role r 
-- WHERE u.username = 'YOUR_USERNAME' AND r.name = 'ROLE_USER';

-- C. KÍCH HOẠT USER BỊ VÔ HIỆU HÓA
-- Uncomment dòng dưới và thay YOUR_USERNAME
-- UPDATE user SET is_active = 1 WHERE username = 'YOUR_USERNAME';

-- D. KIỂM TRA MỘT USER CỤ THỂ (thay YOUR_USERNAME)
-- SELECT 
--     u.username,
--     u.email,
--     u.is_active,
--     LENGTH(u.password) as pwd_len,
--     u.role as role_string,
--     GROUP_CONCAT(r.name) as roles_from_table
-- FROM user u
-- LEFT JOIN user_role ur ON u.id = ur.user_id
-- LEFT JOIN role r ON ur.role_id = r.id
-- WHERE u.username = 'YOUR_USERNAME'
-- GROUP BY u.id;

SELECT '========== HOÀN TẤT ==========' AS '';
SELECT 'Nếu tìm thấy vấn đề, hãy uncomment và chạy phần SỬA ở trên' AS message;

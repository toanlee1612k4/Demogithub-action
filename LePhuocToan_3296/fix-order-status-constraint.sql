-- ============================================
-- SỬA CONSTRAINT CHO TRẠNG THÁI ĐÔN HÀNG
-- Thay DELIVERED thành COMPLETED
-- ============================================

USE ebookstore;

-- 1. Xem constraint hiện tại
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS
WHERE TABLE_NAME = 'orders' AND CONSTRAINT_NAME LIKE '%status%';

-- 2. Cập nhật dữ liệu cũ từ DELIVERED sang COMPLETED (nếu có)
UPDATE orders 
SET status = 'COMPLETED' 
WHERE status = 'DELIVERED';

-- Kiểm tra xem có bao nhiêu bản ghi được cập nhật
SELECT 'Số đơn hàng đã được cập nhật:' AS message, COUNT(*) AS count 
FROM orders 
WHERE status = 'COMPLETED';

-- 3. Xóa constraint cũ
ALTER TABLE orders 
DROP CHECK chk_orders_status_new;

-- 4. Tạo lại constraint mới với COMPLETED thay cho DELIVERED
ALTER TABLE orders
ADD CONSTRAINT chk_orders_status_new 
CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPING', 'COMPLETED', 'CANCELLED'));

-- 5. Kiểm tra constraint mới đã được tạo
SELECT 
    CONSTRAINT_NAME,
    CHECK_CLAUSE
FROM INFORMATION_SCHEMA.CHECK_CONSTRAINTS
WHERE TABLE_NAME = 'orders' AND CONSTRAINT_NAME = 'chk_orders_status_new';

SELECT '✅ HOÀN TẤT! Constraint đã được cập nhật' AS result;
SELECT 'Bây giờ có thể đổi status sang COMPLETED' AS note;

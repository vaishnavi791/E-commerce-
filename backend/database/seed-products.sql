USE ecommerce;

SET @add_subcategory = IF(
    (SELECT COUNT(*) FROM information_schema.columns
     WHERE table_schema = DATABASE()
       AND table_name = 'products'
       AND column_name = 'subcategory') = 0,
    'ALTER TABLE products ADD COLUMN subcategory VARCHAR(255)',
    'SELECT 1'
);
PREPARE add_subcategory_statement FROM @add_subcategory;
EXECUTE add_subcategory_statement;
DEALLOCATE PREPARE add_subcategory_statement;

SET @add_brand = IF(
    (SELECT COUNT(*) FROM information_schema.columns
     WHERE table_schema = DATABASE()
       AND table_name = 'products'
       AND column_name = 'brand') = 0,
    'ALTER TABLE products ADD COLUMN brand VARCHAR(255)',
    'SELECT 1'
);
PREPARE add_brand_statement FROM @add_brand;
EXECUTE add_brand_statement;
DEALLOCATE PREPARE add_brand_statement;

SET @add_image_url = IF(
    (SELECT COUNT(*) FROM information_schema.columns
     WHERE table_schema = DATABASE()
       AND table_name = 'products'
       AND column_name = 'image_url') = 0,
    'ALTER TABLE products ADD COLUMN image_url VARCHAR(1000)',
    'SELECT 1'
);
PREPARE add_image_url_statement FROM @add_image_url;
EXECUTE add_image_url_statement;
DEALLOCATE PREPARE add_image_url_statement;

DELETE FROM products
WHERE p_name IN (
    'Linen Wrap Dress', 'Ribbed Everyday Top', 'Wide Leg Trousers',
    'Leather Court Heels', 'City Leather Sneakers', 'Strappy Flat Sandals',
    'Structured Shoulder Bag', 'Gold Hoop Earrings', 'Classic Cat Eye Sunglasses',
    'Soft Matte Lip Color', 'Daily Hydration Serum', 'Eau de Parfum No. 04'
);

INSERT INTO products (p_name, price, quantity, category, subcategory, brand, image_url)
VALUES
    ('Linen Wrap Dress', 7499.00, 18, 'Clothing', 'Dresses', 'Everlane', 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?auto=format&fit=crop&w=900&q=85'),
    ('Ribbed Everyday Top', 2699.00, 24, 'Clothing', 'Tops', 'COS', 'https://images.unsplash.com/photo-1564257631407-4deb1f99d992?auto=format&fit=crop&w=900&q=85'),
    ('Wide Leg Trousers', 5499.00, 15, 'Clothing', 'Trousers', 'Arket', 'https://images.unsplash.com/photo-1506629905607-d9c297d3f72e?auto=format&fit=crop&w=900&q=85'),
    ('Leather Court Heels', 8999.00, 9, 'Footwear', 'Heels', 'Vagabond', 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?auto=format&fit=crop&w=900&q=85'),
    ('City Leather Sneakers', 7799.00, 12, 'Footwear', 'Sneakers', 'Veja', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=85'),
    ('Strappy Flat Sandals', 4299.00, 20, 'Footwear', 'Sandals', 'Alohas', 'https://images.unsplash.com/photo-1562273138-f46be4ebdf33?auto=format&fit=crop&w=900&q=85'),
    ('Structured Shoulder Bag', 9999.00, 8, 'Accessories', 'Handbags', 'Polene', 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?auto=format&fit=crop&w=900&q=85'),
    ('Gold Hoop Earrings', 3699.00, 30, 'Accessories', 'Jewellery', 'Missoma', 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=900&q=85'),
    ('Classic Cat Eye Sunglasses', 5999.00, 16, 'Accessories', 'Sunglasses', 'Le Specs', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=900&q=85'),
    ('Soft Matte Lip Color', 1999.00, 26, 'Beauty', 'Makeup', 'Rare Beauty', 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=900&q=85'),
    ('Daily Hydration Serum', 2999.00, 21, 'Beauty', 'Skincare', 'The Ordinary', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=900&q=85'),
    ('Eau de Parfum No. 04', 6799.00, 10, 'Beauty', 'Fragrance', 'Byredo', 'https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&w=900&q=85');

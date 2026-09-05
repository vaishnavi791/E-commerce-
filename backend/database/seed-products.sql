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

SET @add_description = IF(
        (SELECT COUNT(*) FROM information_schema.columns
         WHERE table_schema = DATABASE()
             AND table_name = 'products'
             AND column_name = 'description') = 0,
        'ALTER TABLE products ADD COLUMN description VARCHAR(1000)',
        'SELECT 1'
);
PREPARE add_description_statement FROM @add_description;
EXECUTE add_description_statement;
DEALLOCATE PREPARE add_description_statement;

-- Insert only missing names so this script can be run repeatedly without
-- removing, rewriting, or duplicating products already in MySQL.
INSERT INTO products (p_name, price, quantity, category, subcategory, brand, image_url, description)
SELECT p_name, price, quantity, category, subcategory, brand, image_url, description
FROM (
    SELECT 'Linen Wrap Dress' p_name, 7499.00 price, 18 quantity, 'Clothing' category, 'Dresses' subcategory, 'Everlane' brand, 'https://images.unsplash.com/photo-1595777457583-95e059d581b8?auto=format&fit=crop&w=900&q=85' image_url, 'Lightweight linen wrap dress with an easy waist tie for warm days.' description
    UNION ALL SELECT 'Ribbed Everyday Top', 2699.00, 24, 'Clothing', 'Tops', 'COS', 'https://images.unsplash.com/photo-1564257631407-4deb1f99d992?auto=format&fit=crop&w=900&q=85', 'Soft ribbed everyday top with a clean fitted silhouette.'
    UNION ALL SELECT 'Wide Leg Trousers', 5499.00, 15, 'Clothing', 'Trousers', 'Arket', 'https://images.unsplash.com/photo-1506629905607-d9c297d3f72e?auto=format&fit=crop&w=900&q=85', 'Relaxed wide leg trousers with a polished drape and versatile finish.'
    UNION ALL SELECT 'Leather Court Heels', 8999.00, 9, 'Footwear', 'Heels', 'Vagabond', 'https://images.unsplash.com/photo-1543163521-1bf539c55dd2?auto=format&fit=crop&w=900&q=85', 'Classic leather court heels designed for workdays and evenings.'
    UNION ALL SELECT 'City Leather Sneakers', 7799.00, 12, 'Footwear', 'Sneakers', 'Veja', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=85', 'Low profile leather sneakers for comfortable everyday city wear.'
    UNION ALL SELECT 'Strappy Flat Sandals', 4299.00, 20, 'Footwear', 'Sandals', 'Alohas', 'https://images.unsplash.com/photo-1562273138-f46be4ebdf33?auto=format&fit=crop&w=900&q=85', 'Minimal strappy flat sandals with a comfortable summer shape.'
    UNION ALL SELECT 'Structured Shoulder Bag', 9999.00, 8, 'Accessories', 'Handbags', 'Polene', 'https://images.unsplash.com/photo-1584917865442-de89df76afd3?auto=format&fit=crop&w=900&q=85', 'Structured shoulder bag with a refined shape for everyday essentials.'
    UNION ALL SELECT 'Gold Hoop Earrings', 3699.00, 30, 'Accessories', 'Jewelry', 'Missoma', 'https://images.unsplash.com/photo-1535632066927-ab7c9ab60908?auto=format&fit=crop&w=900&q=85', 'Polished gold hoop earrings that add a simple statement to any look.'
    UNION ALL SELECT 'Classic Cat Eye Sunglasses', 5999.00, 16, 'Accessories', 'Sunglasses', 'Le Specs', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=900&q=85', 'Classic cat eye sunglasses with an elegant everyday frame.'
    UNION ALL SELECT 'Soft Matte Lip Color', 1999.00, 26, 'Beauty', 'Makeup', 'Rare Beauty', 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=900&q=85', 'Buildable soft matte lip color with comfortable lasting coverage.'
    UNION ALL SELECT 'Daily Hydration Serum', 2999.00, 21, 'Beauty', 'Skincare', 'The Ordinary', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=900&q=85', 'Lightweight daily hydration serum for a fresh dewy finish.'
    UNION ALL SELECT 'Eau de Parfum No. 04', 6799.00, 10, 'Beauty', 'Fragrance', 'Byredo', 'https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&w=900&q=85', 'Modern floral eau de parfum with a clean, memorable scent.'

    /* Expanded products are loaded by ProductCatalogInitializer instead.
    UNION ALL SELECT 'Midnight Satin Slip Dress', 8999.00, 14, 'Clothing', 'Dresses', 'Reformation', 'https://images.unsplash.com/photo-1539008835657-9e8e9680c956?auto=format&fit=crop&w=900&q=85', 'Bias-cut satin slip dress with a draped neckline for evening occasions.'
    UNION ALL SELECT 'Botanical Cotton Midi Dress', 6899.00, 19, 'Clothing', 'Dresses', 'Sézane', 'https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?auto=format&fit=crop&w=900&q=85', 'Printed cotton midi dress with a fitted bodice and softly gathered skirt.'
    UNION ALL SELECT 'Tailored Belted Shirt Dress', 8299.00, 11, 'Clothing', 'Dresses', 'Massimo Dutti', 'https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=900&q=85', 'Crisp belted shirt dress that moves easily from office hours to dinner.'
    UNION ALL SELECT 'Cotton Poplin Puff Sleeve Blouse', 4599.00, 23, 'Clothing', 'Tops', '& Other Stories', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=900&q=85', 'Breathable cotton poplin blouse with a square neckline and soft puff sleeves.'
    UNION ALL SELECT 'Fine Knit Polo Top', 3999.00, 20, 'Clothing', 'Tops', 'Mango', 'https://images.unsplash.com/photo-1485968579580-b6d095142e6e?auto=format&fit=crop&w=900&q=85', 'Fine-gauge knit polo top with a neat collar for polished everyday layering.'
    UNION ALL SELECT 'Silk Cowl Neck Camisole', 5199.00, 16, 'Clothing', 'Tops', 'Reiss', 'https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=900&q=85', 'Fluid silk camisole with a softly draped cowl neckline.'
    UNION ALL SELECT 'Breton Stripe Jersey Tee', 2499.00, 32, 'Clothing', 'Tops', 'Saint James', 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=85', 'Classic striped cotton jersey tee with a relaxed, easy-to-style fit.'
    UNION ALL SELECT 'Pleated A-Line Midi Skirt', 5799.00, 17, 'Clothing', 'Skirts', 'COS', 'https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?auto=format&fit=crop&w=900&q=85', 'Crisp pleated midi skirt with an A-line shape and comfortable waistband.'
    UNION ALL SELECT 'Relaxed Linen Blazer', 9499.00, 13, 'Clothing', 'Outerwear', 'Arket', 'https://images.unsplash.com/photo-1591047139829-d91aecb6caea?auto=format&fit=crop&w=900&q=85', 'Relaxed linen-blend blazer designed for warm-weather tailoring.'
    UNION ALL SELECT 'High Rise Straight Jeans', 6499.00, 22, 'Clothing', 'Denim', 'Levi''s', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=900&q=85', 'High-rise straight-leg jeans in soft structured denim for everyday wear.'

    -- Accessories: handbag, jewelry, and watch clusters complement one another.
    UNION ALL SELECT 'Soft Leather Work Tote', 12499.00, 10, 'Accessories', 'Handbags', 'Cuyana', 'https://images.unsplash.com/photo-1559563458-527698bf5295?auto=format&fit=crop&w=900&q=85', 'Roomy leather tote with a structured base for a laptop and daily essentials.'
    UNION ALL SELECT 'Crescent Crossbody Bag', 7999.00, 18, 'Accessories', 'Handbags', 'Coach', 'https://images.unsplash.com/photo-1594223274512-ad4803739b7c?auto=format&fit=crop&w=900&q=85', 'Compact crescent crossbody bag with an adjustable strap and secure zip closure.'
    UNION ALL SELECT 'Mini Top Handle Bag', 10999.00, 12, 'Accessories', 'Handbags', 'Furla', 'https://images.unsplash.com/photo-1548036328-c9fa89d128fa?auto=format&fit=crop&w=900&q=85', 'Mini top-handle bag with a removable shoulder strap for versatile styling.'
    UNION ALL SELECT 'Pearl Drop Earrings', 4299.00, 25, 'Accessories', 'Jewelry', 'Mejuri', 'https://images.unsplash.com/photo-1617038220319-276d3cfab638?auto=format&fit=crop&w=900&q=85', 'Delicate pearl drop earrings set in gold vermeil for an elegant finish.'
    UNION ALL SELECT 'Layered Pendant Necklace', 5499.00, 21, 'Accessories', 'Jewelry', 'Missoma', 'https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?auto=format&fit=crop&w=900&q=85', 'Two-layer gold-tone pendant necklace made for everyday stacking.'
    UNION ALL SELECT 'Sculptural Gold Cuff', 4799.00, 15, 'Accessories', 'Jewelry', 'Ana Luisa', 'https://images.unsplash.com/photo-1611652022419-a9419f74343d?auto=format&fit=crop&w=900&q=85', 'Polished sculptural cuff bracelet that adds a modern statement to simple looks.'
    UNION ALL SELECT 'Square Dial Leather Watch', 13999.00, 9, 'Accessories', 'Watches', 'Daniel Wellington', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=900&q=85', 'Minimal square-dial watch with a warm brown leather strap.'
    UNION ALL SELECT 'Two Tone Bracelet Watch', 16499.00, 7, 'Accessories', 'Watches', 'Michael Kors', 'https://images.unsplash.com/photo-1523170335258-f5ed11844a49?auto=format&fit=crop&w=900&q=85', 'Two-tone stainless steel bracelet watch with a refined everyday dial.'
    UNION ALL SELECT 'Rose Gold Mesh Watch', 11999.00, 11, 'Accessories', 'Skagen', 'https://images.unsplash.com/photo-1508057198894-247b23fe5ade?auto=format&fit=crop&w=900&q=85', 'Slim rose-gold mesh watch designed for lightweight all-day wear.'
    UNION ALL SELECT 'Printed Silk Square Scarf', 3299.00, 27, 'Accessories', 'Scarves', 'Scarlet & Sam', 'https://images.unsplash.com/photo-1601924928376-07d2c4e1e5c6?auto=format&fit=crop&w=900&q=85', 'Soft printed silk square scarf for the neck, hair, or handbag handle.' */
) AS seed
WHERE NOT EXISTS (
    SELECT 1
    FROM products AS existing_product
    WHERE existing_product.p_name = seed.p_name
);

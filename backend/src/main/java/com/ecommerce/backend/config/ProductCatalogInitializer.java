package com.ecommerce.backend.config;

import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductCatalogInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductCatalogInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        Set<String> existingProductNames = productRepository.findAll().stream()
                .map(Product::getPName)
                .collect(Collectors.toSet());
        catalog().stream()
                .filter(product -> existingProductNames.add(product.getPName()))
                .forEach(productRepository::save);
    }

    private List<Product> catalog() {
        return List.of(
                // Clothing: Dresses
                product("Midnight Satin Slip Dress", 8999, 14, "Clothing", "Dresses", "Reformation", "https://images.unsplash.com/photo-1539008835657-9e8e9680c956?auto=format&fit=crop&w=900&q=85", "Bias-cut satin slip dress with a draped neckline for evening occasions."),
                product("Botanical Cotton Midi Dress", 6899, 19, "Clothing", "Dresses", "Sézane", "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?auto=format&fit=crop&w=900&q=85", "Printed cotton midi dress with a fitted bodice and softly gathered skirt."),
                product("Tailored Belted Shirt Dress", 8299, 11, "Clothing", "Dresses", "Massimo Dutti", "https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=900&q=85", "Crisp belted shirt dress that moves easily from office hours to dinner."),
                product("Ivory Knit Column Dress", 7499, 16, "Clothing", "Dresses", "COS", "https://images.unsplash.com/photo-1566174053879-31528523f8ae?auto=format&fit=crop&w=900&q=85", "Soft rib-knit column dress with a flattering close fit and side slit."),
                product("Rose Garden Wrap Midi Dress", 7799, 18, "Clothing", "Dresses", "Nobody's Child", "https://images.unsplash.com/photo-1469334031218-e382a71b716b?auto=format&fit=crop&w=900&q=85", "Floral wrap midi dress with a defined waist and flutter sleeves."),
                // Clothing: Tops
                product("Cotton Poplin Puff Sleeve Blouse", 4599, 23, "Clothing", "Tops", "& Other Stories", "https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=900&q=85", "Breathable cotton poplin blouse with a square neckline and soft puff sleeves."),
                product("Fine Knit Polo Top", 3999, 20, "Clothing", "Tops", "Mango", "https://images.unsplash.com/photo-1485968579580-b6d095142e6e?auto=format&fit=crop&w=900&q=85", "Fine-gauge knit polo top with a neat collar for polished everyday layering."),
                product("Silk Cowl Neck Camisole", 5199, 16, "Clothing", "Tops", "Reiss", "https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=900&q=85", "Fluid silk camisole with a softly draped cowl neckline."),
                product("Breton Stripe Jersey Tee", 2499, 32, "Clothing", "Tops", "Saint James", "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=900&q=85", "Classic striped cotton jersey tee with a relaxed, easy-to-style fit."),
                product("Merino Mock Neck Top", 4899, 17, "Clothing", "Tops", "Uniqlo", "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?auto=format&fit=crop&w=900&q=85", "Lightweight merino mock-neck top for polished layering in cooler weather."),

                // Accessories: Handbags, Jewelry, Watches
                product("Soft Leather Work Tote", 12499, 10, "Accessories", "Handbags", "Cuyana", "https://images.unsplash.com/photo-1559563458-527698bf5295?auto=format&fit=crop&w=900&q=85", "Roomy leather tote with a structured base for a laptop and daily essentials."),
                product("Crescent Crossbody Bag", 7999, 18, "Accessories", "Handbags", "Coach", "https://images.unsplash.com/photo-1594223274512-ad4803739b7c?auto=format&fit=crop&w=900&q=85", "Compact crescent crossbody bag with an adjustable strap and secure zip closure."),
                product("Mini Top Handle Bag", 10999, 12, "Accessories", "Handbags", "Furla", "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?auto=format&fit=crop&w=900&q=85", "Mini top-handle bag with a removable shoulder strap for versatile styling."),
                product("Woven Summer Shoulder Bag", 6299, 20, "Accessories", "Handbags", "Zara", "https://images.unsplash.com/photo-1590874103328-eac38a683ce7?auto=format&fit=crop&w=900&q=85", "Textured woven shoulder bag with a compact shape for warm-weather outfits."),
                product("Pearl Drop Earrings", 4299, 25, "Accessories", "Jewellery", "Mejuri", "https://images.unsplash.com/photo-1617038220319-276d3cfab638?auto=format&fit=crop&w=900&q=85", "Delicate pearl drop earrings set in gold vermeil for an elegant finish."),
                product("Layered Pendant Necklace", 5499, 21, "Accessories", "Jewellery", "Missoma", "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f?auto=format&fit=crop&w=900&q=85", "Two-layer gold-tone pendant necklace made for everyday stacking."),
                product("Sculptural Gold Cuff", 4799, 15, "Accessories", "Jewellery", "Ana Luisa", "https://images.unsplash.com/photo-1611652022419-a9419f74343d?auto=format&fit=crop&w=900&q=85", "Polished sculptural cuff bracelet that adds a modern statement to simple looks."),
                product("Square Dial Leather Watch", 13999, 9, "Accessories", "Watches", "Daniel Wellington", "https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=900&q=85", "Minimal square-dial watch with a warm brown leather strap."),
                product("Two Tone Bracelet Watch", 16499, 7, "Accessories", "Watches", "Michael Kors", "https://images.unsplash.com/photo-1523170335258-f5ed11844a49?auto=format&fit=crop&w=900&q=85", "Two-tone stainless steel bracelet watch with a refined everyday dial."),
                product("Rose Gold Mesh Watch", 11999, 11, "Accessories", "Watches", "Skagen", "https://images.unsplash.com/photo-1508057198894-247b23fe5ade?auto=format&fit=crop&w=900&q=85", "Slim rose-gold mesh watch designed for lightweight all-day wear."),

                // Footwear
                product("Pointed Slingback Heels", 8499, 12, "Footwear", "Heels", "Alohas", "https://images.unsplash.com/photo-1543163521-1bf539c55dd2?auto=format&fit=crop&w=900&q=85", "Pointed slingback heels with a comfortable mid-height heel for day-to-night wear."),
                product("Suede Block Heel Pumps", 9299, 10, "Footwear", "Heels", "Sam Edelman", "https://images.unsplash.com/photo-1596703263926-eb0762ee17e4?auto=format&fit=crop&w=900&q=85", "Soft suede block-heel pumps designed for stability and a refined finish."),
                product("Retro Court Sneakers", 7299, 22, "Footwear", "Sneakers", "Adidas", "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=900&q=85", "Low-profile retro court sneakers with cushioned comfort for daily wear."),
                product("Canvas Platform Sneakers", 4499, 27, "Footwear", "Sneakers", "Converse", "https://images.unsplash.com/photo-1495555961986-6d4c1ecb7be3?auto=format&fit=crop&w=900&q=85", "Classic canvas platform sneakers with a lightweight rubber sole."),
                product("Braided Leather Sandals", 5399, 18, "Footwear", "Sandals", "Steve Madden", "https://images.unsplash.com/photo-1562273138-f46be4ebdf33?auto=format&fit=crop&w=900&q=85", "Braided leather flat sandals made for effortless summer styling."),
                product("Minimal Slide Sandals", 3799, 24, "Footwear", "Sandals", "Birkenstock", "https://images.unsplash.com/photo-1603487742131-4160ec999306?auto=format&fit=crop&w=900&q=85", "Easy slip-on sandals with a contoured footbed and minimal leather straps."),
                product("Ballet Flats with Bow", 4699, 19, "Footwear", "Flats", "Sam Edelman", "https://images.unsplash.com/photo-1515347619252-60a4bf4fff4f?auto=format&fit=crop&w=900&q=85", "Soft ballet flats with a delicate bow and flexible sole for everyday comfort."),
                product("Leather Loafer Flats", 6899, 14, "Footwear", "Flats", "Vagabond", "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=900&q=85", "Polished leather loafer flats with a cushioned insole for workday wear."),

                // Beauty
                product("Vitamin C Brightening Serum", 3199, 26, "Beauty", "Skincare", "The Ordinary", "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=900&q=85", "Lightweight vitamin C serum that brightens the look of uneven skin tone."),
                product("Ceramide Barrier Cream", 2899, 24, "Beauty", "Skincare", "CeraVe", "https://images.unsplash.com/photo-1556229010-6c3f2c9ca5f8?auto=format&fit=crop&w=900&q=85", "Rich daily moisturizer with ceramides to support a soft, comfortable skin barrier."),
                product("Overnight Renewal Mask", 3699, 15, "Beauty", "Skincare", "Laneige", "https://images.unsplash.com/photo-1571781926291-c477ebfd024b?auto=format&fit=crop&w=900&q=85", "Hydrating overnight mask that leaves skin looking rested and refreshed by morning."),
                product("Soft Focus Liquid Blush", 2399, 29, "Beauty", "Makeup", "Rare Beauty", "https://images.unsplash.com/photo-1583241800698-e8ab01830a4b?auto=format&fit=crop&w=900&q=85", "Buildable liquid blush with a sheer, natural-looking flush of color."),
                product("Satin Finish Lipstick", 2199, 31, "Beauty", "Makeup", "MAC", "https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=900&q=85", "Comfortable satin lipstick with rich color payoff and a smooth finish."),
                product("Lengthening Mascara", 1999, 28, "Beauty", "Makeup", "Maybelline", "https://images.unsplash.com/photo-1631214524020-7e18db9a8f92?auto=format&fit=crop&w=900&q=85", "Defining mascara that lengthens and separates lashes without clumping."),
                product("Peony and Amber Eau de Parfum", 5999, 13, "Beauty", "Fragrance", "Jo Malone", "https://images.unsplash.com/photo-1541643600914-78b084683601?auto=format&fit=crop&w=900&q=85", "Soft floral fragrance with peony petals, warm amber, and a clean musky dry-down."),
                product("Nourishing Hair Oil", 2799, 18, "Beauty", "Haircare", "Moroccanoil", "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=900&q=85", "Lightweight argan hair oil that smooths flyaways and adds healthy-looking shine.")
        );
    }

    private Product product(String name, double price, int quantity, String category,
                            String subcategory, String brand, String imageUrl,
                            String description) {
        Product product = new Product();
        product.setPName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategory(category);
        product.setSubcategory(subcategory);
        product.setBrand(brand);
        product.setImageUrl(imageUrl);
        product.setDescription(description);
        return product;
    }
}

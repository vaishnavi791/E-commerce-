package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.exception.ResourceNotFoundException;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository){
        this.repository = repository;
    }

    public Product addProduct(ProductDTO dto){

        Product product = new Product();

        product.setPName(dto.getPName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());
        product.setSubcategory(dto.getSubcategory());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setDescription(dto.getDescription());

        return repository.save(product);
    }

    public List<Product> getAllProducts(){
        return repository.findAll();

    }

    public Product getProductById(Long id){

        return repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found with id "+id));

    }

    public Product updateProduct(Long id, ProductDTO dto){
        Product product = getProductById(id);

        product.setPName(dto.getPName());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setCategory(dto.getCategory());
        product.setSubcategory(dto.getSubcategory());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        product.setDescription(dto.getDescription());

        return repository.save(product);

    }

    public void deleteProduct(Long id){
        Product product = getProductById(id);
        repository.delete(product);
    }

}
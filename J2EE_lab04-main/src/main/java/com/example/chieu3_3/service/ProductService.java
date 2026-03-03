package com.example.chieu3_3.service;

import com.example.chieu3_3.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class ProductService {
    List<Product> listProduct = new ArrayList<>();
    
    private final ResourceLoader resourceLoader;
    
    public ProductService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<Product> getAll() {
        return listProduct;
    }

    public Product get(int id) {
        return listProduct.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public void add(Product newProduct) {
        int maxId = listProduct.stream().mapToInt(Product::getId).max().orElse(0);
        newProduct.setId(maxId + 1);
        listProduct.add(newProduct);
    }

    public void update(Product editProduct) {
        Product find = get(editProduct.getId());
        if (find != null) {
            find.setPrice(editProduct.getPrice());
            find.setName(editProduct.getName());
            if (editProduct.getImage() != null)
                find.setImage(editProduct.getImage());
        }
    }

    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        String contentType = imageProduct.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Tệp tải lên không phải là hình ảnh!");
        }

        if (!imageProduct.isEmpty()) {
            try {
                // Get the absolute path to the static/images directory
                Resource resource = resourceLoader.getResource("classpath:static/images");
                Path dirImages = Paths.get(resource.getURI()).toAbsolutePath();
                
                if (!Files.exists(dirImages)) {
                    Files.createDirectories(dirImages);
                }

                String newFileName = UUID.randomUUID() + "-" + imageProduct.getOriginalFilename();
                Path pathFileUpload = dirImages.resolve(newFileName);
                Files.copy(imageProduct.getInputStream(), pathFileUpload,
                        StandardCopyOption.REPLACE_EXISTING);
                newProduct.setImage(newFileName);

            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }

    public void delete(int id) {
        listProduct.removeIf(p -> p.getId() == id);
    }
}

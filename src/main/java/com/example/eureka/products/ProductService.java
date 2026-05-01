package com.example.eureka.products;

import com.example.eureka.products.dto.ProductSearch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductService {
    Long resolveProductId(String rawName);
    List<ProductSearch> searchByName(String name);
}

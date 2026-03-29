package com.example.eureka.products;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("""
        SELECT id, canonical_name
        FROM products
        WHERE canonical_name = #{name}
        LIMIT 1
    """)
    Product findByCanonicalName(@Param("name") String name);

    @Select("<script>" +
            "SELECT * FROM products WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<Product> findByIds(@Param("ids") List<Long> ids);

    @Select("""
        SELECT id, canonical_name,
               similarity(canonical_name, #{name}) AS sim
        FROM products
        WHERE similarity(canonical_name, #{name}) > #{threshold}
        ORDER BY sim DESC
        LIMIT 1
    """)
    Product findMostSimilar(@Param("name") String name,
                            @Param("threshold") double threshold);


    @Insert("INSERT INTO products (canonical_name) VALUES (#{canonicalName})")
    void insert(Product product);

    @Select("SELECT CURRVAL('products_id_seq')")
    Long getLastInsertId();
}
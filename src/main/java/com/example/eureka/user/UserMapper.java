package com.example.eureka.user;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
        @Select("""
        SELECT id,
               username,
               password AS password,
               role_id AS role_id
        FROM users
        WHERE username = #{username}
    """)
        User findByUsername(String username);

        @Select("""
        SELECT id,
               username,
               password AS password,
               role_id AS role_id
        FROM users
        WHERE id = #{id}
    """)
        User findById(Long id);

        @Insert("""
        INSERT INTO users(username, password, role_id)
        VALUES(#{username}, #{password}, #{role_id})
    """)
        @Options(useGeneratedKeys = true, keyProperty = "id")
        void insert(User user);
}

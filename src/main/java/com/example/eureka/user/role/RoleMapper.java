package com.example.eureka.user.role;

import org.apache.ibatis.annotations.*;

@Mapper
public interface RoleMapper {
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "roleType", column = "name",
                    javaType = RoleType.class,
                    typeHandler = org.apache.ibatis.type.EnumTypeHandler.class)
    })
    @Select("SELECT * FROM roles WHERE id = #{id}")
    Role findById(@Param("id") Long id);

}

package com.example.eureka.userActivity;

import com.example.eureka.userActivity.dto.UserActivitySummary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserActivityMapper {

    @Insert("""
        INSERT INTO user_activity_log (user_id, company_id, action)
        VALUES (#{userId}, #{companyId}, #{action})
    """)
    void log(@Param("userId") Long userId,
             @Param("companyId") Long companyId,
             @Param("action") String action);

    @Select("""
    SELECT
        u.id AS userId,
        u.username AS username,
        COUNT(*) AS totalActions,
        COUNT(CASE WHEN action = 'ORDER_CREATED' THEN 1 END) AS orderCount,
        MAX(l.created_at) AS lastSeen
    FROM user_activity_log l
    JOIN users u ON l.user_id = u.id
    WHERE l.company_id = #{companyId}
    GROUP BY u.id, u.username
    ORDER BY totalActions DESC
""")
    @Results({
            @Result(property = "userId", column = "userId"),
            @Result(property = "username", column = "username"),
            @Result(property = "totalActions", column = "totalActions"),
            @Result(property = "orderCount", column = "orderCount"),
            @Result(property = "lastSeen", column = "lastSeen")
    })
    List<UserActivitySummary> getSummaryByCompany(@Param("companyId") Long companyId);
}
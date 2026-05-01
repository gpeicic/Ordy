package com.example.eureka.userActivity;

import com.example.eureka.userActivity.dto.UserActivitySummary;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
@Tag(name = "Activity", description = "Korisnicka aktivnost")
public class UserActivityController {

    private final UserActivityMapper userActivityMapper;

    public UserActivityController(UserActivityMapper userActivityMapper) {
        this.userActivityMapper = userActivityMapper;
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserActivitySummary>> getByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(userActivityMapper.getSummaryByCompany(companyId));
    }
}
package com.tracker.nutrition.controller;

import com.tracker.nutrition.dto.AnalyticsReport;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.UserRepository;
import com.tracker.nutrition.service.AnalyticsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;

    public AnalyticsController(AnalyticsService analyticsService, UserRepository userRepository) {
        this.analyticsService = analyticsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/weekly")
    public AnalyticsReport getWeeklyAnalytics(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return analyticsService.getWeeklyAnalytics(user);
    }
}

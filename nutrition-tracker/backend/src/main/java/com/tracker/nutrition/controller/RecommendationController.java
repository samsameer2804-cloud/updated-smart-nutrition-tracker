package com.tracker.nutrition.controller;

import com.tracker.nutrition.dto.Recommendation;
import com.tracker.nutrition.model.User;
import com.tracker.nutrition.repository.UserRepository;
import com.tracker.nutrition.service.RecommendationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);
    private final RecommendationService service;
    private final UserRepository userRepository;

    public RecommendationController(RecommendationService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Recommendation> getRecommendations(Authentication authentication) {
        log.info("Fetching smart recommendations for user: {}", authentication.getName());
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();
        return service.getRecommendations(user);
    }
}

package org.trustworthyreviews.service.impl;

import org.springframework.stereotype.Service;
import org.trustworthyreviews.service.ReviewSortingService;

import java.util.UUID;

@Service
public class JaccardDistanceProvider implements ReviewSortingService.JaccardDistanceProvider {

    @Override
    public double similarity(UUID userA, UUID userB) {
        // TEMPORARY STUB
        return 0.0;
    }
}

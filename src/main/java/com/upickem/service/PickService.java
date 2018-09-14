package com.upickem.service;

import com.upickem.model.Pick;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PickService {

    List<Pick> scoreReadyUnscoredPicks();
}

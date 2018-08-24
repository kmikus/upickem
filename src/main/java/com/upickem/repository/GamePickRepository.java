package com.upickem.repository;

import com.upickem.model.GamePick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GamePickRepository extends JpaRepository<GamePick, Long> {
}

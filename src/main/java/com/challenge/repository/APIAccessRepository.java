package com.challenge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.challenge.model.APIAccess;

/**
 * Repository class to save and retrieve API Key access related data
 */
@Repository
public interface APIAccessRepository extends JpaRepository<APIAccess, Long> {
	Optional<APIAccess> findByApiKey(String apiKey);
}

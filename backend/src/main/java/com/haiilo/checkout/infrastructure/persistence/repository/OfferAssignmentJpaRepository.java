package com.haiilo.checkout.infrastructure.persistence.repository;

import com.haiilo.checkout.infrastructure.persistence.entity.OfferAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferAssignmentJpaRepository extends JpaRepository<OfferAssignmentEntity, Long> {

    List<OfferAssignmentEntity> findByProduct_IdOrderByPriorityAsc(String productId);
}
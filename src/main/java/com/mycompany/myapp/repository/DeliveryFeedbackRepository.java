package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.DeliveryFeedback;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the DeliveryFeedback entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeliveryFeedbackRepository extends ReactiveCrudRepository<DeliveryFeedback, Long>, DeliveryFeedbackRepositoryInternal {
    Flux<DeliveryFeedback> findAllBy(Pageable pageable);

    @Query("SELECT * FROM delivery_feedback entity WHERE entity.feedback_response_id = :id")
    Flux<DeliveryFeedback> findByFeedbackResponse(Long id);

    @Query("SELECT * FROM delivery_feedback entity WHERE entity.feedback_response_id IS NULL")
    Flux<DeliveryFeedback> findAllWhereFeedbackResponseIsNull();

    @Override
    <S extends DeliveryFeedback> Mono<S> save(S entity);

    @Override
    Flux<DeliveryFeedback> findAll();

    @Override
    Mono<DeliveryFeedback> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DeliveryFeedbackRepositoryInternal {
    <S extends DeliveryFeedback> Mono<S> save(S entity);

    Flux<DeliveryFeedback> findAllBy(Pageable pageable);

    Flux<DeliveryFeedback> findAll();

    Mono<DeliveryFeedback> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<DeliveryFeedback> findAllBy(Pageable pageable, Criteria criteria);

}

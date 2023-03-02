package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FeedbackResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FeedbackResponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackResponseRepository extends ReactiveCrudRepository<FeedbackResponse, Long>, FeedbackResponseRepositoryInternal {
    Flux<FeedbackResponse> findAllBy(Pageable pageable);

    @Query("SELECT * FROM feedback_response entity WHERE entity.id not in (select delivery_feedback_id from delivery_feedback)")
    Flux<FeedbackResponse> findAllWhereDeliveryFeedbackIsNull();

    @Override
    <S extends FeedbackResponse> Mono<S> save(S entity);

    @Override
    Flux<FeedbackResponse> findAll();

    @Override
    Mono<FeedbackResponse> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeedbackResponseRepositoryInternal {
    <S extends FeedbackResponse> Mono<S> save(S entity);

    Flux<FeedbackResponse> findAllBy(Pageable pageable);

    Flux<FeedbackResponse> findAll();

    Mono<FeedbackResponse> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FeedbackResponse> findAllBy(Pageable pageable, Criteria criteria);

}

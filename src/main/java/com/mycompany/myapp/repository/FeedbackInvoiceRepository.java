package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.FeedbackInvoice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FeedbackInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeedbackInvoiceRepository extends ReactiveCrudRepository<FeedbackInvoice, Long>, FeedbackInvoiceRepositoryInternal {
    Flux<FeedbackInvoice> findAllBy(Pageable pageable);

    @Override
    Mono<FeedbackInvoice> findOneWithEagerRelationships(Long id);

    @Override
    Flux<FeedbackInvoice> findAllWithEagerRelationships();

    @Override
    Flux<FeedbackInvoice> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM feedback_invoice entity WHERE entity.delivery_feedback_id = :id")
    Flux<FeedbackInvoice> findByDeliveryFeedback(Long id);

    @Query("SELECT * FROM feedback_invoice entity WHERE entity.delivery_feedback_id IS NULL")
    Flux<FeedbackInvoice> findAllWhereDeliveryFeedbackIsNull();

    @Override
    <S extends FeedbackInvoice> Mono<S> save(S entity);

    @Override
    Flux<FeedbackInvoice> findAll();

    @Override
    Mono<FeedbackInvoice> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FeedbackInvoiceRepositoryInternal {
    <S extends FeedbackInvoice> Mono<S> save(S entity);

    Flux<FeedbackInvoice> findAllBy(Pageable pageable);

    Flux<FeedbackInvoice> findAll();

    Mono<FeedbackInvoice> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FeedbackInvoice> findAllBy(Pageable pageable, Criteria criteria);

    Mono<FeedbackInvoice> findOneWithEagerRelationships(Long id);

    Flux<FeedbackInvoice> findAllWithEagerRelationships();

    Flux<FeedbackInvoice> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}

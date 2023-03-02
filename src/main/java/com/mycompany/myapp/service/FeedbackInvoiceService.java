package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FeedbackInvoice;
import com.mycompany.myapp.repository.FeedbackInvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link FeedbackInvoice}.
 */
@Service
@Transactional
public class FeedbackInvoiceService {

    private final Logger log = LoggerFactory.getLogger(FeedbackInvoiceService.class);

    private final FeedbackInvoiceRepository feedbackInvoiceRepository;

    public FeedbackInvoiceService(FeedbackInvoiceRepository feedbackInvoiceRepository) {
        this.feedbackInvoiceRepository = feedbackInvoiceRepository;
    }

    /**
     * Save a feedbackInvoice.
     *
     * @param feedbackInvoice the entity to save.
     * @return the persisted entity.
     */
    public Mono<FeedbackInvoice> save(FeedbackInvoice feedbackInvoice) {
        log.debug("Request to save FeedbackInvoice : {}", feedbackInvoice);
        return feedbackInvoiceRepository.save(feedbackInvoice);
    }

    /**
     * Update a feedbackInvoice.
     *
     * @param feedbackInvoice the entity to save.
     * @return the persisted entity.
     */
    public Mono<FeedbackInvoice> update(FeedbackInvoice feedbackInvoice) {
        log.debug("Request to update FeedbackInvoice : {}", feedbackInvoice);
        return feedbackInvoiceRepository.save(feedbackInvoice);
    }

    /**
     * Partially update a feedbackInvoice.
     *
     * @param feedbackInvoice the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FeedbackInvoice> partialUpdate(FeedbackInvoice feedbackInvoice) {
        log.debug("Request to partially update FeedbackInvoice : {}", feedbackInvoice);

        return feedbackInvoiceRepository
            .findById(feedbackInvoice.getId())
            .map(existingFeedbackInvoice -> {
                if (feedbackInvoice.getInvoiceNumber() != null) {
                    existingFeedbackInvoice.setInvoiceNumber(feedbackInvoice.getInvoiceNumber());
                }

                return existingFeedbackInvoice;
            })
            .flatMap(feedbackInvoiceRepository::save);
    }

    /**
     * Get all the feedbackInvoices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FeedbackInvoice> findAll(Pageable pageable) {
        log.debug("Request to get all FeedbackInvoices");
        return feedbackInvoiceRepository.findAllBy(pageable);
    }

    /**
     * Get all the feedbackInvoices with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Flux<FeedbackInvoice> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackInvoiceRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Returns the number of feedbackInvoices available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return feedbackInvoiceRepository.count();
    }

    /**
     * Get one feedbackInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FeedbackInvoice> findOne(Long id) {
        log.debug("Request to get FeedbackInvoice : {}", id);
        return feedbackInvoiceRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the feedbackInvoice by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FeedbackInvoice : {}", id);
        return feedbackInvoiceRepository.deleteById(id);
    }
}

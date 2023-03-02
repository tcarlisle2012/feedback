package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.FeedbackResponse;
import com.mycompany.myapp.repository.FeedbackResponseRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link FeedbackResponse}.
 */
@Service
@Transactional
public class FeedbackResponseService {

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseService.class);

    private final FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackResponseService(FeedbackResponseRepository feedbackResponseRepository) {
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    /**
     * Save a feedbackResponse.
     *
     * @param feedbackResponse the entity to save.
     * @return the persisted entity.
     */
    public Mono<FeedbackResponse> save(FeedbackResponse feedbackResponse) {
        log.debug("Request to save FeedbackResponse : {}", feedbackResponse);
        return feedbackResponseRepository.save(feedbackResponse);
    }

    /**
     * Update a feedbackResponse.
     *
     * @param feedbackResponse the entity to save.
     * @return the persisted entity.
     */
    public Mono<FeedbackResponse> update(FeedbackResponse feedbackResponse) {
        log.debug("Request to update FeedbackResponse : {}", feedbackResponse);
        return feedbackResponseRepository.save(feedbackResponse);
    }

    /**
     * Partially update a feedbackResponse.
     *
     * @param feedbackResponse the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<FeedbackResponse> partialUpdate(FeedbackResponse feedbackResponse) {
        log.debug("Request to partially update FeedbackResponse : {}", feedbackResponse);

        return feedbackResponseRepository
            .findById(feedbackResponse.getId())
            .map(existingFeedbackResponse -> {
                if (feedbackResponse.getMinRating() != null) {
                    existingFeedbackResponse.setMinRating(feedbackResponse.getMinRating());
                }
                if (feedbackResponse.getMaxRating() != null) {
                    existingFeedbackResponse.setMaxRating(feedbackResponse.getMaxRating());
                }
                if (feedbackResponse.getRating() != null) {
                    existingFeedbackResponse.setRating(feedbackResponse.getRating());
                }
                if (feedbackResponse.getTags() != null) {
                    existingFeedbackResponse.setTags(feedbackResponse.getTags());
                }
                if (feedbackResponse.getPrompt() != null) {
                    existingFeedbackResponse.setPrompt(feedbackResponse.getPrompt());
                }
                if (feedbackResponse.getCampaign() != null) {
                    existingFeedbackResponse.setCampaign(feedbackResponse.getCampaign());
                }
                if (feedbackResponse.getComment() != null) {
                    existingFeedbackResponse.setComment(feedbackResponse.getComment());
                }
                if (feedbackResponse.getCustomerNumber() != null) {
                    existingFeedbackResponse.setCustomerNumber(feedbackResponse.getCustomerNumber());
                }
                if (feedbackResponse.getSalesOrganization() != null) {
                    existingFeedbackResponse.setSalesOrganization(feedbackResponse.getSalesOrganization());
                }
                if (feedbackResponse.getDistributionChannel() != null) {
                    existingFeedbackResponse.setDistributionChannel(feedbackResponse.getDistributionChannel());
                }
                if (feedbackResponse.getDivision() != null) {
                    existingFeedbackResponse.setDivision(feedbackResponse.getDivision());
                }

                return existingFeedbackResponse;
            })
            .flatMap(feedbackResponseRepository::save);
    }

    /**
     * Get all the feedbackResponses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FeedbackResponse> findAll(Pageable pageable) {
        log.debug("Request to get all FeedbackResponses");
        return feedbackResponseRepository.findAllBy(pageable);
    }

    /**
     *  Get all the feedbackResponses where DeliveryFeedback is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<FeedbackResponse> findAllWhereDeliveryFeedbackIsNull() {
        log.debug("Request to get all feedbackResponses where DeliveryFeedback is null");
        return feedbackResponseRepository.findAllWhereDeliveryFeedbackIsNull();
    }

    /**
     * Returns the number of feedbackResponses available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return feedbackResponseRepository.count();
    }

    /**
     * Get one feedbackResponse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<FeedbackResponse> findOne(Long id) {
        log.debug("Request to get FeedbackResponse : {}", id);
        return feedbackResponseRepository.findById(id);
    }

    /**
     * Delete the feedbackResponse by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete FeedbackResponse : {}", id);
        return feedbackResponseRepository.deleteById(id);
    }
}

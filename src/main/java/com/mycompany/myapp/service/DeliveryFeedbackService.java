package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.DeliveryFeedback;
import com.mycompany.myapp.repository.DeliveryFeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DeliveryFeedback}.
 */
@Service
@Transactional
public class DeliveryFeedbackService {

    private final Logger log = LoggerFactory.getLogger(DeliveryFeedbackService.class);

    private final DeliveryFeedbackRepository deliveryFeedbackRepository;

    public DeliveryFeedbackService(DeliveryFeedbackRepository deliveryFeedbackRepository) {
        this.deliveryFeedbackRepository = deliveryFeedbackRepository;
    }

    /**
     * Save a deliveryFeedback.
     *
     * @param deliveryFeedback the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryFeedback> save(DeliveryFeedback deliveryFeedback) {
        log.debug("Request to save DeliveryFeedback : {}", deliveryFeedback);
        return deliveryFeedbackRepository.save(deliveryFeedback);
    }

    /**
     * Update a deliveryFeedback.
     *
     * @param deliveryFeedback the entity to save.
     * @return the persisted entity.
     */
    public Mono<DeliveryFeedback> update(DeliveryFeedback deliveryFeedback) {
        log.debug("Request to update DeliveryFeedback : {}", deliveryFeedback);
        return deliveryFeedbackRepository.save(deliveryFeedback);
    }

    /**
     * Partially update a deliveryFeedback.
     *
     * @param deliveryFeedback the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<DeliveryFeedback> partialUpdate(DeliveryFeedback deliveryFeedback) {
        log.debug("Request to partially update DeliveryFeedback : {}", deliveryFeedback);

        return deliveryFeedbackRepository
            .findById(deliveryFeedback.getId())
            .map(existingDeliveryFeedback -> {
                if (deliveryFeedback.getContactName() != null) {
                    existingDeliveryFeedback.setContactName(deliveryFeedback.getContactName());
                }
                if (deliveryFeedback.getContactEmail() != null) {
                    existingDeliveryFeedback.setContactEmail(deliveryFeedback.getContactEmail());
                }
                if (deliveryFeedback.getDriverEmployeeNumber() != null) {
                    existingDeliveryFeedback.setDriverEmployeeNumber(deliveryFeedback.getDriverEmployeeNumber());
                }

                return existingDeliveryFeedback;
            })
            .flatMap(deliveryFeedbackRepository::save);
    }

    /**
     * Get all the deliveryFeedbacks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<DeliveryFeedback> findAll(Pageable pageable) {
        log.debug("Request to get all DeliveryFeedbacks");
        return deliveryFeedbackRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of deliveryFeedbacks available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return deliveryFeedbackRepository.count();
    }

    /**
     * Get one deliveryFeedback by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<DeliveryFeedback> findOne(Long id) {
        log.debug("Request to get DeliveryFeedback : {}", id);
        return deliveryFeedbackRepository.findById(id);
    }

    /**
     * Delete the deliveryFeedback by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete DeliveryFeedback : {}", id);
        return deliveryFeedbackRepository.deleteById(id);
    }
}

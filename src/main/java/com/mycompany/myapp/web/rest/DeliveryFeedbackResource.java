package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DeliveryFeedback;
import com.mycompany.myapp.repository.DeliveryFeedbackRepository;
import com.mycompany.myapp.service.DeliveryFeedbackService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.DeliveryFeedback}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryFeedbackResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryFeedbackResource.class);

    private static final String ENTITY_NAME = "deliveryFeedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryFeedbackService deliveryFeedbackService;

    private final DeliveryFeedbackRepository deliveryFeedbackRepository;

    public DeliveryFeedbackResource(
        DeliveryFeedbackService deliveryFeedbackService,
        DeliveryFeedbackRepository deliveryFeedbackRepository
    ) {
        this.deliveryFeedbackService = deliveryFeedbackService;
        this.deliveryFeedbackRepository = deliveryFeedbackRepository;
    }

    /**
     * {@code POST  /delivery-feedbacks} : Create a new deliveryFeedback.
     *
     * @param deliveryFeedback the deliveryFeedback to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryFeedback, or with status {@code 400 (Bad Request)} if the deliveryFeedback has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-feedbacks")
    public Mono<ResponseEntity<DeliveryFeedback>> createDeliveryFeedback(@RequestBody DeliveryFeedback deliveryFeedback)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryFeedback : {}", deliveryFeedback);
        if (deliveryFeedback.getId() != null) {
            throw new BadRequestAlertException("A new deliveryFeedback cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return deliveryFeedbackService
            .save(deliveryFeedback)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/delivery-feedbacks/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /delivery-feedbacks/:id} : Updates an existing deliveryFeedback.
     *
     * @param id the id of the deliveryFeedback to save.
     * @param deliveryFeedback the deliveryFeedback to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryFeedback,
     * or with status {@code 400 (Bad Request)} if the deliveryFeedback is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryFeedback couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-feedbacks/{id}")
    public Mono<ResponseEntity<DeliveryFeedback>> updateDeliveryFeedback(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryFeedback deliveryFeedback
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryFeedback : {}, {}", id, deliveryFeedback);
        if (deliveryFeedback.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryFeedback.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deliveryFeedbackRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deliveryFeedbackService
                    .update(deliveryFeedback)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /delivery-feedbacks/:id} : Partial updates given fields of an existing deliveryFeedback, field will ignore if it is null
     *
     * @param id the id of the deliveryFeedback to save.
     * @param deliveryFeedback the deliveryFeedback to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryFeedback,
     * or with status {@code 400 (Bad Request)} if the deliveryFeedback is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryFeedback is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryFeedback couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-feedbacks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DeliveryFeedback>> partialUpdateDeliveryFeedback(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DeliveryFeedback deliveryFeedback
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryFeedback partially : {}, {}", id, deliveryFeedback);
        if (deliveryFeedback.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryFeedback.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return deliveryFeedbackRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DeliveryFeedback> result = deliveryFeedbackService.partialUpdate(deliveryFeedback);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /delivery-feedbacks} : get all the deliveryFeedbacks.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryFeedbacks in body.
     */
    @GetMapping("/delivery-feedbacks")
    public Mono<ResponseEntity<List<DeliveryFeedback>>> getAllDeliveryFeedbacks(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of DeliveryFeedbacks");
        return deliveryFeedbackService
            .countAll()
            .zipWith(deliveryFeedbackService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /delivery-feedbacks/:id} : get the "id" deliveryFeedback.
     *
     * @param id the id of the deliveryFeedback to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryFeedback, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-feedbacks/{id}")
    public Mono<ResponseEntity<DeliveryFeedback>> getDeliveryFeedback(@PathVariable Long id) {
        log.debug("REST request to get DeliveryFeedback : {}", id);
        Mono<DeliveryFeedback> deliveryFeedback = deliveryFeedbackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryFeedback);
    }

    /**
     * {@code DELETE  /delivery-feedbacks/:id} : delete the "id" deliveryFeedback.
     *
     * @param id the id of the deliveryFeedback to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-feedbacks/{id}")
    public Mono<ResponseEntity<Void>> deleteDeliveryFeedback(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryFeedback : {}", id);
        return deliveryFeedbackService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}

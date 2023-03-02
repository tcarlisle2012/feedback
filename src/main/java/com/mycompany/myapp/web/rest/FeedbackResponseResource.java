package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FeedbackResponse;
import com.mycompany.myapp.repository.FeedbackResponseRepository;
import com.mycompany.myapp.service.FeedbackResponseService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FeedbackResponse}.
 */
@RestController
@RequestMapping("/api")
public class FeedbackResponseResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackResponseResource.class);

    private static final String ENTITY_NAME = "feedbackResponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackResponseService feedbackResponseService;

    private final FeedbackResponseRepository feedbackResponseRepository;

    public FeedbackResponseResource(
        FeedbackResponseService feedbackResponseService,
        FeedbackResponseRepository feedbackResponseRepository
    ) {
        this.feedbackResponseService = feedbackResponseService;
        this.feedbackResponseRepository = feedbackResponseRepository;
    }

    /**
     * {@code POST  /feedback-responses} : Create a new feedbackResponse.
     *
     * @param feedbackResponse the feedbackResponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackResponse, or with status {@code 400 (Bad Request)} if the feedbackResponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-responses")
    public Mono<ResponseEntity<FeedbackResponse>> createFeedbackResponse(@Valid @RequestBody FeedbackResponse feedbackResponse)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackResponse : {}", feedbackResponse);
        if (feedbackResponse.getId() != null) {
            throw new BadRequestAlertException("A new feedbackResponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return feedbackResponseService
            .save(feedbackResponse)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/feedback-responses/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /feedback-responses/:id} : Updates an existing feedbackResponse.
     *
     * @param id the id of the feedbackResponse to save.
     * @param feedbackResponse the feedbackResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponse,
     * or with status {@code 400 (Bad Request)} if the feedbackResponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-responses/{id}")
    public Mono<ResponseEntity<FeedbackResponse>> updateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeedbackResponse feedbackResponse
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackResponse : {}, {}", id, feedbackResponse);
        if (feedbackResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackResponseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return feedbackResponseService
                    .update(feedbackResponse)
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
     * {@code PATCH  /feedback-responses/:id} : Partial updates given fields of an existing feedbackResponse, field will ignore if it is null
     *
     * @param id the id of the feedbackResponse to save.
     * @param feedbackResponse the feedbackResponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponse,
     * or with status {@code 400 (Bad Request)} if the feedbackResponse is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackResponse is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-responses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FeedbackResponse>> partialUpdateFeedbackResponse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackResponse feedbackResponse
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackResponse partially : {}, {}", id, feedbackResponse);
        if (feedbackResponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponse.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackResponseRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FeedbackResponse> result = feedbackResponseService.partialUpdate(feedbackResponse);

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
     * {@code GET  /feedback-responses} : get all the feedbackResponses.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackResponses in body.
     */
    @GetMapping("/feedback-responses")
    public Mono<ResponseEntity<List<FeedbackResponse>>> getAllFeedbackResponses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false) String filter
    ) {
        if ("deliveryfeedback-is-null".equals(filter)) {
            log.debug("REST request to get all FeedbackResponses where deliveryFeedback is null");
            return feedbackResponseService.findAllWhereDeliveryFeedbackIsNull().collectList().map(ResponseEntity::ok);
        }
        log.debug("REST request to get a page of FeedbackResponses");
        return feedbackResponseService
            .countAll()
            .zipWith(feedbackResponseService.findAll(pageable).collectList())
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
     * {@code GET  /feedback-responses/:id} : get the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackResponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-responses/{id}")
    public Mono<ResponseEntity<FeedbackResponse>> getFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to get FeedbackResponse : {}", id);
        Mono<FeedbackResponse> feedbackResponse = feedbackResponseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackResponse);
    }

    /**
     * {@code DELETE  /feedback-responses/:id} : delete the "id" feedbackResponse.
     *
     * @param id the id of the feedbackResponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-responses/{id}")
    public Mono<ResponseEntity<Void>> deleteFeedbackResponse(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackResponse : {}", id);
        return feedbackResponseService
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

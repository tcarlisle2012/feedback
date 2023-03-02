package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.FeedbackInvoice;
import com.mycompany.myapp.repository.FeedbackInvoiceRepository;
import com.mycompany.myapp.service.FeedbackInvoiceService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.FeedbackInvoice}.
 */
@RestController
@RequestMapping("/api")
public class FeedbackInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(FeedbackInvoiceResource.class);

    private static final String ENTITY_NAME = "feedbackInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackInvoiceService feedbackInvoiceService;

    private final FeedbackInvoiceRepository feedbackInvoiceRepository;

    public FeedbackInvoiceResource(FeedbackInvoiceService feedbackInvoiceService, FeedbackInvoiceRepository feedbackInvoiceRepository) {
        this.feedbackInvoiceService = feedbackInvoiceService;
        this.feedbackInvoiceRepository = feedbackInvoiceRepository;
    }

    /**
     * {@code POST  /feedback-invoices} : Create a new feedbackInvoice.
     *
     * @param feedbackInvoice the feedbackInvoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackInvoice, or with status {@code 400 (Bad Request)} if the feedbackInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/feedback-invoices")
    public Mono<ResponseEntity<FeedbackInvoice>> createFeedbackInvoice(@RequestBody FeedbackInvoice feedbackInvoice)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackInvoice : {}", feedbackInvoice);
        if (feedbackInvoice.getId() != null) {
            throw new BadRequestAlertException("A new feedbackInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return feedbackInvoiceService
            .save(feedbackInvoice)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/feedback-invoices/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /feedback-invoices/:id} : Updates an existing feedbackInvoice.
     *
     * @param id the id of the feedbackInvoice to save.
     * @param feedbackInvoice the feedbackInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackInvoice,
     * or with status {@code 400 (Bad Request)} if the feedbackInvoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/feedback-invoices/{id}")
    public Mono<ResponseEntity<FeedbackInvoice>> updateFeedbackInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackInvoice feedbackInvoice
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackInvoice : {}, {}", id, feedbackInvoice);
        if (feedbackInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackInvoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackInvoiceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return feedbackInvoiceService
                    .update(feedbackInvoice)
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
     * {@code PATCH  /feedback-invoices/:id} : Partial updates given fields of an existing feedbackInvoice, field will ignore if it is null
     *
     * @param id the id of the feedbackInvoice to save.
     * @param feedbackInvoice the feedbackInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackInvoice,
     * or with status {@code 400 (Bad Request)} if the feedbackInvoice is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackInvoice is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/feedback-invoices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FeedbackInvoice>> partialUpdateFeedbackInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackInvoice feedbackInvoice
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackInvoice partially : {}, {}", id, feedbackInvoice);
        if (feedbackInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackInvoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return feedbackInvoiceRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FeedbackInvoice> result = feedbackInvoiceService.partialUpdate(feedbackInvoice);

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
     * {@code GET  /feedback-invoices} : get all the feedbackInvoices.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackInvoices in body.
     */
    @GetMapping("/feedback-invoices")
    public Mono<ResponseEntity<List<FeedbackInvoice>>> getAllFeedbackInvoices(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FeedbackInvoices");
        return feedbackInvoiceService
            .countAll()
            .zipWith(feedbackInvoiceService.findAll(pageable).collectList())
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
     * {@code GET  /feedback-invoices/:id} : get the "id" feedbackInvoice.
     *
     * @param id the id of the feedbackInvoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackInvoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/feedback-invoices/{id}")
    public Mono<ResponseEntity<FeedbackInvoice>> getFeedbackInvoice(@PathVariable Long id) {
        log.debug("REST request to get FeedbackInvoice : {}", id);
        Mono<FeedbackInvoice> feedbackInvoice = feedbackInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackInvoice);
    }

    /**
     * {@code DELETE  /feedback-invoices/:id} : delete the "id" feedbackInvoice.
     *
     * @param id the id of the feedbackInvoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/feedback-invoices/{id}")
    public Mono<ResponseEntity<Void>> deleteFeedbackInvoice(@PathVariable Long id) {
        log.debug("REST request to delete FeedbackInvoice : {}", id);
        return feedbackInvoiceService
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

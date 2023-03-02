package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FeedbackInvoice;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.FeedbackInvoiceRepository;
import com.mycompany.myapp.service.FeedbackInvoiceService;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link FeedbackInvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeedbackInvoiceResourceIT {

    private static final String DEFAULT_INVOICE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedback-invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackInvoiceRepository feedbackInvoiceRepository;

    @Mock
    private FeedbackInvoiceRepository feedbackInvoiceRepositoryMock;

    @Mock
    private FeedbackInvoiceService feedbackInvoiceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FeedbackInvoice feedbackInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackInvoice createEntity(EntityManager em) {
        FeedbackInvoice feedbackInvoice = new FeedbackInvoice().invoiceNumber(DEFAULT_INVOICE_NUMBER);
        return feedbackInvoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackInvoice createUpdatedEntity(EntityManager em) {
        FeedbackInvoice feedbackInvoice = new FeedbackInvoice().invoiceNumber(UPDATED_INVOICE_NUMBER);
        return feedbackInvoice;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FeedbackInvoice.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        feedbackInvoice = createEntity(em);
    }

    @Test
    void createFeedbackInvoice() throws Exception {
        int databaseSizeBeforeCreate = feedbackInvoiceRepository.findAll().collectList().block().size();
        // Create the FeedbackInvoice
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackInvoice testFeedbackInvoice = feedbackInvoiceList.get(feedbackInvoiceList.size() - 1);
        assertThat(testFeedbackInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    }

    @Test
    void createFeedbackInvoiceWithExistingId() throws Exception {
        // Create the FeedbackInvoice with an existing ID
        feedbackInvoice.setId(1L);

        int databaseSizeBeforeCreate = feedbackInvoiceRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllFeedbackInvoices() {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        // Get all the feedbackInvoiceList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(feedbackInvoice.getId().intValue()))
            .jsonPath("$.[*].invoiceNumber")
            .value(hasItem(DEFAULT_INVOICE_NUMBER));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackInvoicesWithEagerRelationshipsIsEnabled() {
        when(feedbackInvoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(feedbackInvoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackInvoicesWithEagerRelationshipsIsNotEnabled() {
        when(feedbackInvoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(feedbackInvoiceRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getFeedbackInvoice() {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        // Get the feedbackInvoice
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feedbackInvoice.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feedbackInvoice.getId().intValue()))
            .jsonPath("$.invoiceNumber")
            .value(is(DEFAULT_INVOICE_NUMBER));
    }

    @Test
    void getNonExistingFeedbackInvoice() {
        // Get the feedbackInvoice
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeedbackInvoice() throws Exception {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();

        // Update the feedbackInvoice
        FeedbackInvoice updatedFeedbackInvoice = feedbackInvoiceRepository.findById(feedbackInvoice.getId()).block();
        updatedFeedbackInvoice.invoiceNumber(UPDATED_INVOICE_NUMBER);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFeedbackInvoice.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFeedbackInvoice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
        FeedbackInvoice testFeedbackInvoice = feedbackInvoiceList.get(feedbackInvoiceList.size() - 1);
        assertThat(testFeedbackInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    }

    @Test
    void putNonExistingFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackInvoice.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeedbackInvoiceWithPatch() throws Exception {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();

        // Update the feedbackInvoice using partial update
        FeedbackInvoice partialUpdatedFeedbackInvoice = new FeedbackInvoice();
        partialUpdatedFeedbackInvoice.setId(feedbackInvoice.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackInvoice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackInvoice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
        FeedbackInvoice testFeedbackInvoice = feedbackInvoiceList.get(feedbackInvoiceList.size() - 1);
        assertThat(testFeedbackInvoice.getInvoiceNumber()).isEqualTo(DEFAULT_INVOICE_NUMBER);
    }

    @Test
    void fullUpdateFeedbackInvoiceWithPatch() throws Exception {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();

        // Update the feedbackInvoice using partial update
        FeedbackInvoice partialUpdatedFeedbackInvoice = new FeedbackInvoice();
        partialUpdatedFeedbackInvoice.setId(feedbackInvoice.getId());

        partialUpdatedFeedbackInvoice.invoiceNumber(UPDATED_INVOICE_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackInvoice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackInvoice))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
        FeedbackInvoice testFeedbackInvoice = feedbackInvoiceList.get(feedbackInvoiceList.size() - 1);
        assertThat(testFeedbackInvoice.getInvoiceNumber()).isEqualTo(UPDATED_INVOICE_NUMBER);
    }

    @Test
    void patchNonExistingFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feedbackInvoice.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeedbackInvoice() throws Exception {
        int databaseSizeBeforeUpdate = feedbackInvoiceRepository.findAll().collectList().block().size();
        feedbackInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackInvoice))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackInvoice in the database
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeedbackInvoice() {
        // Initialize the database
        feedbackInvoiceRepository.save(feedbackInvoice).block();

        int databaseSizeBeforeDelete = feedbackInvoiceRepository.findAll().collectList().block().size();

        // Delete the feedbackInvoice
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feedbackInvoice.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FeedbackInvoice> feedbackInvoiceList = feedbackInvoiceRepository.findAll().collectList().block();
        assertThat(feedbackInvoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

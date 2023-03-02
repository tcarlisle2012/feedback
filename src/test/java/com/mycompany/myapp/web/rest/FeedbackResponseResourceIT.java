package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FeedbackResponse;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.FeedbackResponseRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FeedbackResponseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FeedbackResponseResourceIT {

    private static final Long DEFAULT_MIN_RATING = 1L;
    private static final Long UPDATED_MIN_RATING = 2L;

    private static final Long DEFAULT_MAX_RATING = 1L;
    private static final Long UPDATED_MAX_RATING = 2L;

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_PROMPT = "AAAAAAAAAA";
    private static final String UPDATED_PROMPT = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPAIGN = "AAAAAAAAAA";
    private static final String UPDATED_CAMPAIGN = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_SALES_ORGANIZATION = "AAAAAAAAAA";
    private static final String UPDATED_SALES_ORGANIZATION = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRIBUTION_CHANNEL = "AAAAAAAAAA";
    private static final String UPDATED_DISTRIBUTION_CHANNEL = "BBBBBBBBBB";

    private static final String DEFAULT_DIVISION = "AAAAAAAAAA";
    private static final String UPDATED_DIVISION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedback-responses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FeedbackResponse feedbackResponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse()
            .minRating(DEFAULT_MIN_RATING)
            .maxRating(DEFAULT_MAX_RATING)
            .rating(DEFAULT_RATING)
            .tags(DEFAULT_TAGS)
            .prompt(DEFAULT_PROMPT)
            .campaign(DEFAULT_CAMPAIGN)
            .comment(DEFAULT_COMMENT)
            .customerNumber(DEFAULT_CUSTOMER_NUMBER)
            .salesOrganization(DEFAULT_SALES_ORGANIZATION)
            .distributionChannel(DEFAULT_DISTRIBUTION_CHANNEL)
            .division(DEFAULT_DIVISION);
        return feedbackResponse;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponse createUpdatedEntity(EntityManager em) {
        FeedbackResponse feedbackResponse = new FeedbackResponse()
            .minRating(UPDATED_MIN_RATING)
            .maxRating(UPDATED_MAX_RATING)
            .rating(UPDATED_RATING)
            .tags(UPDATED_TAGS)
            .prompt(UPDATED_PROMPT)
            .campaign(UPDATED_CAMPAIGN)
            .comment(UPDATED_COMMENT)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .salesOrganization(UPDATED_SALES_ORGANIZATION)
            .distributionChannel(UPDATED_DISTRIBUTION_CHANNEL)
            .division(UPDATED_DIVISION);
        return feedbackResponse;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FeedbackResponse.class).block();
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
        feedbackResponse = createEntity(em);
    }

    @Test
    void createFeedbackResponse() throws Exception {
        int databaseSizeBeforeCreate = feedbackResponseRepository.findAll().collectList().block().size();
        // Create the FeedbackResponse
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeCreate + 1);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getMinRating()).isEqualTo(DEFAULT_MIN_RATING);
        assertThat(testFeedbackResponse.getMaxRating()).isEqualTo(DEFAULT_MAX_RATING);
        assertThat(testFeedbackResponse.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testFeedbackResponse.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testFeedbackResponse.getPrompt()).isEqualTo(DEFAULT_PROMPT);
        assertThat(testFeedbackResponse.getCampaign()).isEqualTo(DEFAULT_CAMPAIGN);
        assertThat(testFeedbackResponse.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testFeedbackResponse.getCustomerNumber()).isEqualTo(DEFAULT_CUSTOMER_NUMBER);
        assertThat(testFeedbackResponse.getSalesOrganization()).isEqualTo(DEFAULT_SALES_ORGANIZATION);
        assertThat(testFeedbackResponse.getDistributionChannel()).isEqualTo(DEFAULT_DISTRIBUTION_CHANNEL);
        assertThat(testFeedbackResponse.getDivision()).isEqualTo(DEFAULT_DIVISION);
    }

    @Test
    void createFeedbackResponseWithExistingId() throws Exception {
        // Create the FeedbackResponse with an existing ID
        feedbackResponse.setId(1L);

        int databaseSizeBeforeCreate = feedbackResponseRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCommentIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackResponseRepository.findAll().collectList().block().size();
        // set the field null
        feedbackResponse.setComment(null);

        // Create the FeedbackResponse, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCustomerNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackResponseRepository.findAll().collectList().block().size();
        // set the field null
        feedbackResponse.setCustomerNumber(null);

        // Create the FeedbackResponse, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSalesOrganizationIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackResponseRepository.findAll().collectList().block().size();
        // set the field null
        feedbackResponse.setSalesOrganization(null);

        // Create the FeedbackResponse, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDistributionChannelIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackResponseRepository.findAll().collectList().block().size();
        // set the field null
        feedbackResponse.setDistributionChannel(null);

        // Create the FeedbackResponse, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDivisionIsRequired() throws Exception {
        int databaseSizeBeforeTest = feedbackResponseRepository.findAll().collectList().block().size();
        // set the field null
        feedbackResponse.setDivision(null);

        // Create the FeedbackResponse, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFeedbackResponses() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        // Get all the feedbackResponseList
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
            .value(hasItem(feedbackResponse.getId().intValue()))
            .jsonPath("$.[*].minRating")
            .value(hasItem(DEFAULT_MIN_RATING.intValue()))
            .jsonPath("$.[*].maxRating")
            .value(hasItem(DEFAULT_MAX_RATING.intValue()))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING.doubleValue()))
            .jsonPath("$.[*].tags")
            .value(hasItem(DEFAULT_TAGS))
            .jsonPath("$.[*].prompt")
            .value(hasItem(DEFAULT_PROMPT))
            .jsonPath("$.[*].campaign")
            .value(hasItem(DEFAULT_CAMPAIGN))
            .jsonPath("$.[*].comment")
            .value(hasItem(DEFAULT_COMMENT))
            .jsonPath("$.[*].customerNumber")
            .value(hasItem(DEFAULT_CUSTOMER_NUMBER))
            .jsonPath("$.[*].salesOrganization")
            .value(hasItem(DEFAULT_SALES_ORGANIZATION))
            .jsonPath("$.[*].distributionChannel")
            .value(hasItem(DEFAULT_DISTRIBUTION_CHANNEL))
            .jsonPath("$.[*].division")
            .value(hasItem(DEFAULT_DIVISION));
    }

    @Test
    void getFeedbackResponse() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        // Get the feedbackResponse
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(feedbackResponse.getId().intValue()))
            .jsonPath("$.minRating")
            .value(is(DEFAULT_MIN_RATING.intValue()))
            .jsonPath("$.maxRating")
            .value(is(DEFAULT_MAX_RATING.intValue()))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING.doubleValue()))
            .jsonPath("$.tags")
            .value(is(DEFAULT_TAGS))
            .jsonPath("$.prompt")
            .value(is(DEFAULT_PROMPT))
            .jsonPath("$.campaign")
            .value(is(DEFAULT_CAMPAIGN))
            .jsonPath("$.comment")
            .value(is(DEFAULT_COMMENT))
            .jsonPath("$.customerNumber")
            .value(is(DEFAULT_CUSTOMER_NUMBER))
            .jsonPath("$.salesOrganization")
            .value(is(DEFAULT_SALES_ORGANIZATION))
            .jsonPath("$.distributionChannel")
            .value(is(DEFAULT_DISTRIBUTION_CHANNEL))
            .jsonPath("$.division")
            .value(is(DEFAULT_DIVISION));
    }

    @Test
    void getNonExistingFeedbackResponse() {
        // Get the feedbackResponse
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFeedbackResponse() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();

        // Update the feedbackResponse
        FeedbackResponse updatedFeedbackResponse = feedbackResponseRepository.findById(feedbackResponse.getId()).block();
        updatedFeedbackResponse
            .minRating(UPDATED_MIN_RATING)
            .maxRating(UPDATED_MAX_RATING)
            .rating(UPDATED_RATING)
            .tags(UPDATED_TAGS)
            .prompt(UPDATED_PROMPT)
            .campaign(UPDATED_CAMPAIGN)
            .comment(UPDATED_COMMENT)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .salesOrganization(UPDATED_SALES_ORGANIZATION)
            .distributionChannel(UPDATED_DISTRIBUTION_CHANNEL)
            .division(UPDATED_DIVISION);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFeedbackResponse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFeedbackResponse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getMinRating()).isEqualTo(UPDATED_MIN_RATING);
        assertThat(testFeedbackResponse.getMaxRating()).isEqualTo(UPDATED_MAX_RATING);
        assertThat(testFeedbackResponse.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testFeedbackResponse.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testFeedbackResponse.getPrompt()).isEqualTo(UPDATED_PROMPT);
        assertThat(testFeedbackResponse.getCampaign()).isEqualTo(UPDATED_CAMPAIGN);
        assertThat(testFeedbackResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFeedbackResponse.getCustomerNumber()).isEqualTo(UPDATED_CUSTOMER_NUMBER);
        assertThat(testFeedbackResponse.getSalesOrganization()).isEqualTo(UPDATED_SALES_ORGANIZATION);
        assertThat(testFeedbackResponse.getDistributionChannel()).isEqualTo(UPDATED_DISTRIBUTION_CHANNEL);
        assertThat(testFeedbackResponse.getDivision()).isEqualTo(UPDATED_DIVISION);
    }

    @Test
    void putNonExistingFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        partialUpdatedFeedbackResponse
            .minRating(UPDATED_MIN_RATING)
            .maxRating(UPDATED_MAX_RATING)
            .tags(UPDATED_TAGS)
            .prompt(UPDATED_PROMPT)
            .comment(UPDATED_COMMENT)
            .distributionChannel(UPDATED_DISTRIBUTION_CHANNEL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackResponse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getMinRating()).isEqualTo(UPDATED_MIN_RATING);
        assertThat(testFeedbackResponse.getMaxRating()).isEqualTo(UPDATED_MAX_RATING);
        assertThat(testFeedbackResponse.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testFeedbackResponse.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testFeedbackResponse.getPrompt()).isEqualTo(UPDATED_PROMPT);
        assertThat(testFeedbackResponse.getCampaign()).isEqualTo(DEFAULT_CAMPAIGN);
        assertThat(testFeedbackResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFeedbackResponse.getCustomerNumber()).isEqualTo(DEFAULT_CUSTOMER_NUMBER);
        assertThat(testFeedbackResponse.getSalesOrganization()).isEqualTo(DEFAULT_SALES_ORGANIZATION);
        assertThat(testFeedbackResponse.getDistributionChannel()).isEqualTo(UPDATED_DISTRIBUTION_CHANNEL);
        assertThat(testFeedbackResponse.getDivision()).isEqualTo(DEFAULT_DIVISION);
    }

    @Test
    void fullUpdateFeedbackResponseWithPatch() throws Exception {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();

        // Update the feedbackResponse using partial update
        FeedbackResponse partialUpdatedFeedbackResponse = new FeedbackResponse();
        partialUpdatedFeedbackResponse.setId(feedbackResponse.getId());

        partialUpdatedFeedbackResponse
            .minRating(UPDATED_MIN_RATING)
            .maxRating(UPDATED_MAX_RATING)
            .rating(UPDATED_RATING)
            .tags(UPDATED_TAGS)
            .prompt(UPDATED_PROMPT)
            .campaign(UPDATED_CAMPAIGN)
            .comment(UPDATED_COMMENT)
            .customerNumber(UPDATED_CUSTOMER_NUMBER)
            .salesOrganization(UPDATED_SALES_ORGANIZATION)
            .distributionChannel(UPDATED_DISTRIBUTION_CHANNEL)
            .division(UPDATED_DIVISION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFeedbackResponse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFeedbackResponse))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
        FeedbackResponse testFeedbackResponse = feedbackResponseList.get(feedbackResponseList.size() - 1);
        assertThat(testFeedbackResponse.getMinRating()).isEqualTo(UPDATED_MIN_RATING);
        assertThat(testFeedbackResponse.getMaxRating()).isEqualTo(UPDATED_MAX_RATING);
        assertThat(testFeedbackResponse.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testFeedbackResponse.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testFeedbackResponse.getPrompt()).isEqualTo(UPDATED_PROMPT);
        assertThat(testFeedbackResponse.getCampaign()).isEqualTo(UPDATED_CAMPAIGN);
        assertThat(testFeedbackResponse.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testFeedbackResponse.getCustomerNumber()).isEqualTo(UPDATED_CUSTOMER_NUMBER);
        assertThat(testFeedbackResponse.getSalesOrganization()).isEqualTo(UPDATED_SALES_ORGANIZATION);
        assertThat(testFeedbackResponse.getDistributionChannel()).isEqualTo(UPDATED_DISTRIBUTION_CHANNEL);
        assertThat(testFeedbackResponse.getDivision()).isEqualTo(UPDATED_DIVISION);
    }

    @Test
    void patchNonExistingFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFeedbackResponse() throws Exception {
        int databaseSizeBeforeUpdate = feedbackResponseRepository.findAll().collectList().block().size();
        feedbackResponse.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(feedbackResponse))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FeedbackResponse in the database
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFeedbackResponse() {
        // Initialize the database
        feedbackResponseRepository.save(feedbackResponse).block();

        int databaseSizeBeforeDelete = feedbackResponseRepository.findAll().collectList().block().size();

        // Delete the feedbackResponse
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, feedbackResponse.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FeedbackResponse> feedbackResponseList = feedbackResponseRepository.findAll().collectList().block();
        assertThat(feedbackResponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

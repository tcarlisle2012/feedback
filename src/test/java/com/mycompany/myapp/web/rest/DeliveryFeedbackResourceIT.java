package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.DeliveryFeedback;
import com.mycompany.myapp.repository.DeliveryFeedbackRepository;
import com.mycompany.myapp.repository.EntityManager;
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
 * Integration tests for the {@link DeliveryFeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeliveryFeedbackResourceIT {

    private static final String DEFAULT_CONTACT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DRIVER_EMPLOYEE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DRIVER_EMPLOYEE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/delivery-feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeliveryFeedbackRepository deliveryFeedbackRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private DeliveryFeedback deliveryFeedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryFeedback createEntity(EntityManager em) {
        DeliveryFeedback deliveryFeedback = new DeliveryFeedback()
            .contactName(DEFAULT_CONTACT_NAME)
            .contactEmail(DEFAULT_CONTACT_EMAIL)
            .driverEmployeeNumber(DEFAULT_DRIVER_EMPLOYEE_NUMBER);
        return deliveryFeedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeliveryFeedback createUpdatedEntity(EntityManager em) {
        DeliveryFeedback deliveryFeedback = new DeliveryFeedback()
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .driverEmployeeNumber(UPDATED_DRIVER_EMPLOYEE_NUMBER);
        return deliveryFeedback;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(DeliveryFeedback.class).block();
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
        deliveryFeedback = createEntity(em);
    }

    @Test
    void createDeliveryFeedback() throws Exception {
        int databaseSizeBeforeCreate = deliveryFeedbackRepository.findAll().collectList().block().size();
        // Create the DeliveryFeedback
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeCreate + 1);
        DeliveryFeedback testDeliveryFeedback = deliveryFeedbackList.get(deliveryFeedbackList.size() - 1);
        assertThat(testDeliveryFeedback.getContactName()).isEqualTo(DEFAULT_CONTACT_NAME);
        assertThat(testDeliveryFeedback.getContactEmail()).isEqualTo(DEFAULT_CONTACT_EMAIL);
        assertThat(testDeliveryFeedback.getDriverEmployeeNumber()).isEqualTo(DEFAULT_DRIVER_EMPLOYEE_NUMBER);
    }

    @Test
    void createDeliveryFeedbackWithExistingId() throws Exception {
        // Create the DeliveryFeedback with an existing ID
        deliveryFeedback.setId(1L);

        int databaseSizeBeforeCreate = deliveryFeedbackRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDeliveryFeedbacks() {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        // Get all the deliveryFeedbackList
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
            .value(hasItem(deliveryFeedback.getId().intValue()))
            .jsonPath("$.[*].contactName")
            .value(hasItem(DEFAULT_CONTACT_NAME))
            .jsonPath("$.[*].contactEmail")
            .value(hasItem(DEFAULT_CONTACT_EMAIL))
            .jsonPath("$.[*].driverEmployeeNumber")
            .value(hasItem(DEFAULT_DRIVER_EMPLOYEE_NUMBER));
    }

    @Test
    void getDeliveryFeedback() {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        // Get the deliveryFeedback
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deliveryFeedback.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deliveryFeedback.getId().intValue()))
            .jsonPath("$.contactName")
            .value(is(DEFAULT_CONTACT_NAME))
            .jsonPath("$.contactEmail")
            .value(is(DEFAULT_CONTACT_EMAIL))
            .jsonPath("$.driverEmployeeNumber")
            .value(is(DEFAULT_DRIVER_EMPLOYEE_NUMBER));
    }

    @Test
    void getNonExistingDeliveryFeedback() {
        // Get the deliveryFeedback
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDeliveryFeedback() throws Exception {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();

        // Update the deliveryFeedback
        DeliveryFeedback updatedDeliveryFeedback = deliveryFeedbackRepository.findById(deliveryFeedback.getId()).block();
        updatedDeliveryFeedback
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .driverEmployeeNumber(UPDATED_DRIVER_EMPLOYEE_NUMBER);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDeliveryFeedback.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedDeliveryFeedback))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
        DeliveryFeedback testDeliveryFeedback = deliveryFeedbackList.get(deliveryFeedbackList.size() - 1);
        assertThat(testDeliveryFeedback.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryFeedback.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDeliveryFeedback.getDriverEmployeeNumber()).isEqualTo(UPDATED_DRIVER_EMPLOYEE_NUMBER);
    }

    @Test
    void putNonExistingDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deliveryFeedback.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeliveryFeedbackWithPatch() throws Exception {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();

        // Update the deliveryFeedback using partial update
        DeliveryFeedback partialUpdatedDeliveryFeedback = new DeliveryFeedback();
        partialUpdatedDeliveryFeedback.setId(deliveryFeedback.getId());

        partialUpdatedDeliveryFeedback.contactName(UPDATED_CONTACT_NAME).contactEmail(UPDATED_CONTACT_EMAIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeliveryFeedback.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryFeedback))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
        DeliveryFeedback testDeliveryFeedback = deliveryFeedbackList.get(deliveryFeedbackList.size() - 1);
        assertThat(testDeliveryFeedback.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryFeedback.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDeliveryFeedback.getDriverEmployeeNumber()).isEqualTo(DEFAULT_DRIVER_EMPLOYEE_NUMBER);
    }

    @Test
    void fullUpdateDeliveryFeedbackWithPatch() throws Exception {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();

        // Update the deliveryFeedback using partial update
        DeliveryFeedback partialUpdatedDeliveryFeedback = new DeliveryFeedback();
        partialUpdatedDeliveryFeedback.setId(deliveryFeedback.getId());

        partialUpdatedDeliveryFeedback
            .contactName(UPDATED_CONTACT_NAME)
            .contactEmail(UPDATED_CONTACT_EMAIL)
            .driverEmployeeNumber(UPDATED_DRIVER_EMPLOYEE_NUMBER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeliveryFeedback.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeliveryFeedback))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
        DeliveryFeedback testDeliveryFeedback = deliveryFeedbackList.get(deliveryFeedbackList.size() - 1);
        assertThat(testDeliveryFeedback.getContactName()).isEqualTo(UPDATED_CONTACT_NAME);
        assertThat(testDeliveryFeedback.getContactEmail()).isEqualTo(UPDATED_CONTACT_EMAIL);
        assertThat(testDeliveryFeedback.getDriverEmployeeNumber()).isEqualTo(UPDATED_DRIVER_EMPLOYEE_NUMBER);
    }

    @Test
    void patchNonExistingDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deliveryFeedback.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeliveryFeedback() throws Exception {
        int databaseSizeBeforeUpdate = deliveryFeedbackRepository.findAll().collectList().block().size();
        deliveryFeedback.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deliveryFeedback))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DeliveryFeedback in the database
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeliveryFeedback() {
        // Initialize the database
        deliveryFeedbackRepository.save(deliveryFeedback).block();

        int databaseSizeBeforeDelete = deliveryFeedbackRepository.findAll().collectList().block().size();

        // Delete the deliveryFeedback
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deliveryFeedback.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DeliveryFeedback> deliveryFeedbackList = deliveryFeedbackRepository.findAll().collectList().block();
        assertThat(deliveryFeedbackList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

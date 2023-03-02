package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryFeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryFeedback.class);
        DeliveryFeedback deliveryFeedback1 = new DeliveryFeedback();
        deliveryFeedback1.setId(1L);
        DeliveryFeedback deliveryFeedback2 = new DeliveryFeedback();
        deliveryFeedback2.setId(deliveryFeedback1.getId());
        assertThat(deliveryFeedback1).isEqualTo(deliveryFeedback2);
        deliveryFeedback2.setId(2L);
        assertThat(deliveryFeedback1).isNotEqualTo(deliveryFeedback2);
        deliveryFeedback1.setId(null);
        assertThat(deliveryFeedback1).isNotEqualTo(deliveryFeedback2);
    }
}

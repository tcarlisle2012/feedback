package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackInvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackInvoice.class);
        FeedbackInvoice feedbackInvoice1 = new FeedbackInvoice();
        feedbackInvoice1.setId(1L);
        FeedbackInvoice feedbackInvoice2 = new FeedbackInvoice();
        feedbackInvoice2.setId(feedbackInvoice1.getId());
        assertThat(feedbackInvoice1).isEqualTo(feedbackInvoice2);
        feedbackInvoice2.setId(2L);
        assertThat(feedbackInvoice1).isNotEqualTo(feedbackInvoice2);
        feedbackInvoice1.setId(null);
        assertThat(feedbackInvoice1).isNotEqualTo(feedbackInvoice2);
    }
}

package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FeedbackInvoice.
 */
@Table("feedback_invoice")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("invoice_number")
    private String invoiceNumber;

    @Transient
    @JsonIgnoreProperties(value = { "feedbackResponse", "feedbackInvoices" }, allowSetters = true)
    private DeliveryFeedback deliveryFeedback;

    @Column("delivery_feedback_id")
    private Long deliveryFeedbackId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackInvoice id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public FeedbackInvoice invoiceNumber(String invoiceNumber) {
        this.setInvoiceNumber(invoiceNumber);
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public DeliveryFeedback getDeliveryFeedback() {
        return this.deliveryFeedback;
    }

    public void setDeliveryFeedback(DeliveryFeedback deliveryFeedback) {
        this.deliveryFeedback = deliveryFeedback;
        this.deliveryFeedbackId = deliveryFeedback != null ? deliveryFeedback.getId() : null;
    }

    public FeedbackInvoice deliveryFeedback(DeliveryFeedback deliveryFeedback) {
        this.setDeliveryFeedback(deliveryFeedback);
        return this;
    }

    public Long getDeliveryFeedbackId() {
        return this.deliveryFeedbackId;
    }

    public void setDeliveryFeedbackId(Long deliveryFeedback) {
        this.deliveryFeedbackId = deliveryFeedback;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackInvoice)) {
            return false;
        }
        return id != null && id.equals(((FeedbackInvoice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackInvoice{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            "}";
    }
}

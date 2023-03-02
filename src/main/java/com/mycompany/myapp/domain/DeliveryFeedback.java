package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A DeliveryFeedback.
 */
@Table("delivery_feedback")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeliveryFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("contact_name")
    private String contactName;

    @Column("contact_email")
    private String contactEmail;

    @Column("driver_employee_number")
    private String driverEmployeeNumber;

    @Transient
    private FeedbackResponse feedbackResponse;

    @Transient
    @JsonIgnoreProperties(value = { "deliveryFeedback" }, allowSetters = true)
    private Set<FeedbackInvoice> feedbackInvoices = new HashSet<>();

    @Column("feedback_response_id")
    private Long feedbackResponseId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DeliveryFeedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContactName() {
        return this.contactName;
    }

    public DeliveryFeedback contactName(String contactName) {
        this.setContactName(contactName);
        return this;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return this.contactEmail;
    }

    public DeliveryFeedback contactEmail(String contactEmail) {
        this.setContactEmail(contactEmail);
        return this;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getDriverEmployeeNumber() {
        return this.driverEmployeeNumber;
    }

    public DeliveryFeedback driverEmployeeNumber(String driverEmployeeNumber) {
        this.setDriverEmployeeNumber(driverEmployeeNumber);
        return this;
    }

    public void setDriverEmployeeNumber(String driverEmployeeNumber) {
        this.driverEmployeeNumber = driverEmployeeNumber;
    }

    public FeedbackResponse getFeedbackResponse() {
        return this.feedbackResponse;
    }

    public void setFeedbackResponse(FeedbackResponse feedbackResponse) {
        this.feedbackResponse = feedbackResponse;
        this.feedbackResponseId = feedbackResponse != null ? feedbackResponse.getId() : null;
    }

    public DeliveryFeedback feedbackResponse(FeedbackResponse feedbackResponse) {
        this.setFeedbackResponse(feedbackResponse);
        return this;
    }

    public Set<FeedbackInvoice> getFeedbackInvoices() {
        return this.feedbackInvoices;
    }

    public void setFeedbackInvoices(Set<FeedbackInvoice> feedbackInvoices) {
        if (this.feedbackInvoices != null) {
            this.feedbackInvoices.forEach(i -> i.setDeliveryFeedback(null));
        }
        if (feedbackInvoices != null) {
            feedbackInvoices.forEach(i -> i.setDeliveryFeedback(this));
        }
        this.feedbackInvoices = feedbackInvoices;
    }

    public DeliveryFeedback feedbackInvoices(Set<FeedbackInvoice> feedbackInvoices) {
        this.setFeedbackInvoices(feedbackInvoices);
        return this;
    }

    public DeliveryFeedback addFeedbackInvoice(FeedbackInvoice feedbackInvoice) {
        this.feedbackInvoices.add(feedbackInvoice);
        feedbackInvoice.setDeliveryFeedback(this);
        return this;
    }

    public DeliveryFeedback removeFeedbackInvoice(FeedbackInvoice feedbackInvoice) {
        this.feedbackInvoices.remove(feedbackInvoice);
        feedbackInvoice.setDeliveryFeedback(null);
        return this;
    }

    public Long getFeedbackResponseId() {
        return this.feedbackResponseId;
    }

    public void setFeedbackResponseId(Long feedbackResponse) {
        this.feedbackResponseId = feedbackResponse;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeliveryFeedback)) {
            return false;
        }
        return id != null && id.equals(((DeliveryFeedback) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeliveryFeedback{" +
            "id=" + getId() +
            ", contactName='" + getContactName() + "'" +
            ", contactEmail='" + getContactEmail() + "'" +
            ", driverEmployeeNumber='" + getDriverEmployeeNumber() + "'" +
            "}";
    }
}

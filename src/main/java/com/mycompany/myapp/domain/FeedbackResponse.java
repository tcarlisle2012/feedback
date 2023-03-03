package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A FeedbackResponse.
 */
@Table("feedback_response")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("min_rating")
    private Long minRating;

    @Column("max_rating")
    private Long maxRating;

    @Column("rating")
    private Double rating;

    @Column("tags")
    private String tags;

    @Size(max = 2000)
    @Column("prompt")
    private String prompt;

    @Size(max = 2000)
    @Column("campaign")
    private String campaign;

    @NotNull(message = "must not be null")
    @Size(max = 500)
    @Column("comment")
    private String comment;

    @NotNull(message = "must not be null")
    @Size(max = 40)
    @Column("customer_number")
    private String customerNumber;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("sales_organization")
    private String salesOrganization;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("distribution_channel")
    private String distributionChannel;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("division")
    private String division;

    @Transient
    private DeliveryFeedback deliveryFeedback;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackResponse id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMinRating() {
        return this.minRating;
    }

    public FeedbackResponse minRating(Long minRating) {
        this.setMinRating(minRating);
        return this;
    }

    public void setMinRating(Long minRating) {
        this.minRating = minRating;
    }

    public Long getMaxRating() {
        return this.maxRating;
    }

    public FeedbackResponse maxRating(Long maxRating) {
        this.setMaxRating(maxRating);
        return this;
    }

    public void setMaxRating(Long maxRating) {
        this.maxRating = maxRating;
    }

    public Double getRating() {
        return this.rating;
    }

    public FeedbackResponse rating(Double rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getTags() {
        return this.tags;
    }

    public FeedbackResponse tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public FeedbackResponse prompt(String prompt) {
        this.setPrompt(prompt);
        return this;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getCampaign() {
        return this.campaign;
    }

    public FeedbackResponse campaign(String campaign) {
        this.setCampaign(campaign);
        return this;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public String getComment() {
        return this.comment;
    }

    public FeedbackResponse comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerNumber() {
        return this.customerNumber;
    }

    public FeedbackResponse customerNumber(String customerNumber) {
        this.setCustomerNumber(customerNumber);
        return this;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getSalesOrganization() {
        return this.salesOrganization;
    }

    public FeedbackResponse salesOrganization(String salesOrganization) {
        this.setSalesOrganization(salesOrganization);
        return this;
    }

    public void setSalesOrganization(String salesOrganization) {
        this.salesOrganization = salesOrganization;
    }

    public String getDistributionChannel() {
        return this.distributionChannel;
    }

    public FeedbackResponse distributionChannel(String distributionChannel) {
        this.setDistributionChannel(distributionChannel);
        return this;
    }

    public void setDistributionChannel(String distributionChannel) {
        this.distributionChannel = distributionChannel;
    }

    public String getDivision() {
        return this.division;
    }

    public FeedbackResponse division(String division) {
        this.setDivision(division);
        return this;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public DeliveryFeedback getDeliveryFeedback() {
        return this.deliveryFeedback;
    }

    public void setDeliveryFeedback(DeliveryFeedback deliveryFeedback) {
        if (this.deliveryFeedback != null) {
            this.deliveryFeedback.setFeedbackResponse(null);
        }
        if (deliveryFeedback != null) {
            deliveryFeedback.setFeedbackResponse(this);
        }
        this.deliveryFeedback = deliveryFeedback;
    }

    public FeedbackResponse deliveryFeedback(DeliveryFeedback deliveryFeedback) {
        this.setDeliveryFeedback(deliveryFeedback);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackResponse)) {
            return false;
        }
        return id != null && id.equals(((FeedbackResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackResponse{" +
            "id=" + getId() +
            ", minRating=" + getMinRating() +
            ", maxRating=" + getMaxRating() +
            ", rating=" + getRating() +
            ", tags='" + getTags() + "'" +
            ", prompt='" + getPrompt() + "'" +
            ", campaign='" + getCampaign() + "'" +
            ", comment='" + getComment() + "'" +
            ", customerNumber='" + getCustomerNumber() + "'" +
            ", salesOrganization='" + getSalesOrganization() + "'" +
            ", distributionChannel='" + getDistributionChannel() + "'" +
            ", division='" + getDivision() + "'" +
            "}";
    }
}

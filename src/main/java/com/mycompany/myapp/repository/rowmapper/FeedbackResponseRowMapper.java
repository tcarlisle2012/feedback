package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.FeedbackResponse;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FeedbackResponse}, with proper type conversions.
 */
@Service
public class FeedbackResponseRowMapper implements BiFunction<Row, String, FeedbackResponse> {

    private final ColumnConverter converter;

    public FeedbackResponseRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FeedbackResponse} stored in the database.
     */
    @Override
    public FeedbackResponse apply(Row row, String prefix) {
        FeedbackResponse entity = new FeedbackResponse();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setMinRating(converter.fromRow(row, prefix + "_min_rating", Long.class));
        entity.setMaxRating(converter.fromRow(row, prefix + "_max_rating", Long.class));
        entity.setRating(converter.fromRow(row, prefix + "_rating", Double.class));
        entity.setTags(converter.fromRow(row, prefix + "_tags", String.class));
        entity.setPrompt(converter.fromRow(row, prefix + "_prompt", String.class));
        entity.setCampaign(converter.fromRow(row, prefix + "_campaign", String.class));
        entity.setComment(converter.fromRow(row, prefix + "_comment", String.class));
        entity.setCustomerNumber(converter.fromRow(row, prefix + "_customer_number", String.class));
        entity.setSalesOrganization(converter.fromRow(row, prefix + "_sales_organization", String.class));
        entity.setDistributionChannel(converter.fromRow(row, prefix + "_distribution_channel", String.class));
        entity.setDivision(converter.fromRow(row, prefix + "_division", String.class));
        return entity;
    }
}

package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.FeedbackInvoice;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link FeedbackInvoice}, with proper type conversions.
 */
@Service
public class FeedbackInvoiceRowMapper implements BiFunction<Row, String, FeedbackInvoice> {

    private final ColumnConverter converter;

    public FeedbackInvoiceRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link FeedbackInvoice} stored in the database.
     */
    @Override
    public FeedbackInvoice apply(Row row, String prefix) {
        FeedbackInvoice entity = new FeedbackInvoice();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setInvoiceNumber(converter.fromRow(row, prefix + "_invoice_number", String.class));
        entity.setDeliveryFeedbackId(converter.fromRow(row, prefix + "_delivery_feedback_id", Long.class));
        return entity;
    }
}

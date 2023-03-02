package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.DeliveryFeedback;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DeliveryFeedback}, with proper type conversions.
 */
@Service
public class DeliveryFeedbackRowMapper implements BiFunction<Row, String, DeliveryFeedback> {

    private final ColumnConverter converter;

    public DeliveryFeedbackRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DeliveryFeedback} stored in the database.
     */
    @Override
    public DeliveryFeedback apply(Row row, String prefix) {
        DeliveryFeedback entity = new DeliveryFeedback();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setContactName(converter.fromRow(row, prefix + "_contact_name", String.class));
        entity.setContactEmail(converter.fromRow(row, prefix + "_contact_email", String.class));
        entity.setDriverEmployeeNumber(converter.fromRow(row, prefix + "_driver_employee_number", String.class));
        entity.setFeedbackResponseId(converter.fromRow(row, prefix + "_feedback_response_id", Long.class));
        return entity;
    }
}

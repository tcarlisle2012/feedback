package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DeliveryFeedbackSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("contact_name", table, columnPrefix + "_contact_name"));
        columns.add(Column.aliased("contact_email", table, columnPrefix + "_contact_email"));
        columns.add(Column.aliased("driver_employee_number", table, columnPrefix + "_driver_employee_number"));

        columns.add(Column.aliased("feedback_response_id", table, columnPrefix + "_feedback_response_id"));
        return columns;
    }
}

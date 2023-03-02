package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class FeedbackResponseSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("min_rating", table, columnPrefix + "_min_rating"));
        columns.add(Column.aliased("max_rating", table, columnPrefix + "_max_rating"));
        columns.add(Column.aliased("rating", table, columnPrefix + "_rating"));
        columns.add(Column.aliased("tags", table, columnPrefix + "_tags"));
        columns.add(Column.aliased("prompt", table, columnPrefix + "_prompt"));
        columns.add(Column.aliased("campaign", table, columnPrefix + "_campaign"));
        columns.add(Column.aliased("comment", table, columnPrefix + "_comment"));
        columns.add(Column.aliased("customer_number", table, columnPrefix + "_customer_number"));
        columns.add(Column.aliased("sales_organization", table, columnPrefix + "_sales_organization"));
        columns.add(Column.aliased("distribution_channel", table, columnPrefix + "_distribution_channel"));
        columns.add(Column.aliased("division", table, columnPrefix + "_division"));

        return columns;
    }
}

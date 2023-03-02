package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.FeedbackInvoice;
import com.mycompany.myapp.repository.rowmapper.DeliveryFeedbackRowMapper;
import com.mycompany.myapp.repository.rowmapper.FeedbackInvoiceRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the FeedbackInvoice entity.
 */
@SuppressWarnings("unused")
class FeedbackInvoiceRepositoryInternalImpl
    extends SimpleR2dbcRepository<FeedbackInvoice, Long>
    implements FeedbackInvoiceRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final DeliveryFeedbackRowMapper deliveryfeedbackMapper;
    private final FeedbackInvoiceRowMapper feedbackinvoiceMapper;

    private static final Table entityTable = Table.aliased("feedback_invoice", EntityManager.ENTITY_ALIAS);
    private static final Table deliveryFeedbackTable = Table.aliased("delivery_feedback", "deliveryFeedback");

    public FeedbackInvoiceRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        DeliveryFeedbackRowMapper deliveryfeedbackMapper,
        FeedbackInvoiceRowMapper feedbackinvoiceMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(FeedbackInvoice.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.deliveryfeedbackMapper = deliveryfeedbackMapper;
        this.feedbackinvoiceMapper = feedbackinvoiceMapper;
    }

    @Override
    public Flux<FeedbackInvoice> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<FeedbackInvoice> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = FeedbackInvoiceSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(DeliveryFeedbackSqlHelper.getColumns(deliveryFeedbackTable, "deliveryFeedback"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(deliveryFeedbackTable)
            .on(Column.create("delivery_feedback_id", entityTable))
            .equals(Column.create("id", deliveryFeedbackTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, FeedbackInvoice.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<FeedbackInvoice> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<FeedbackInvoice> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<FeedbackInvoice> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<FeedbackInvoice> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<FeedbackInvoice> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private FeedbackInvoice process(Row row, RowMetadata metadata) {
        FeedbackInvoice entity = feedbackinvoiceMapper.apply(row, "e");
        entity.setDeliveryFeedback(deliveryfeedbackMapper.apply(row, "deliveryFeedback"));
        return entity;
    }

    @Override
    public <S extends FeedbackInvoice> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

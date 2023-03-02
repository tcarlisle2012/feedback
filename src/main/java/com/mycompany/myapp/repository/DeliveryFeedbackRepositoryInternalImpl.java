package com.mycompany.myapp.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.mycompany.myapp.domain.DeliveryFeedback;
import com.mycompany.myapp.repository.rowmapper.DeliveryFeedbackRowMapper;
import com.mycompany.myapp.repository.rowmapper.FeedbackResponseRowMapper;
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
 * Spring Data R2DBC custom repository implementation for the DeliveryFeedback entity.
 */
@SuppressWarnings("unused")
class DeliveryFeedbackRepositoryInternalImpl
    extends SimpleR2dbcRepository<DeliveryFeedback, Long>
    implements DeliveryFeedbackRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final FeedbackResponseRowMapper feedbackresponseMapper;
    private final DeliveryFeedbackRowMapper deliveryfeedbackMapper;

    private static final Table entityTable = Table.aliased("delivery_feedback", EntityManager.ENTITY_ALIAS);
    private static final Table feedbackResponseTable = Table.aliased("feedback_response", "feedbackResponse");

    public DeliveryFeedbackRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        FeedbackResponseRowMapper feedbackresponseMapper,
        DeliveryFeedbackRowMapper deliveryfeedbackMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(DeliveryFeedback.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.feedbackresponseMapper = feedbackresponseMapper;
        this.deliveryfeedbackMapper = deliveryfeedbackMapper;
    }

    @Override
    public Flux<DeliveryFeedback> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<DeliveryFeedback> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DeliveryFeedbackSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(FeedbackResponseSqlHelper.getColumns(feedbackResponseTable, "feedbackResponse"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(feedbackResponseTable)
            .on(Column.create("feedback_response_id", entityTable))
            .equals(Column.create("id", feedbackResponseTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, DeliveryFeedback.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<DeliveryFeedback> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<DeliveryFeedback> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private DeliveryFeedback process(Row row, RowMetadata metadata) {
        DeliveryFeedback entity = deliveryfeedbackMapper.apply(row, "e");
        entity.setFeedbackResponse(feedbackresponseMapper.apply(row, "feedbackResponse"));
        return entity;
    }

    @Override
    public <S extends DeliveryFeedback> Mono<S> save(S entity) {
        return super.save(entity);
    }
}

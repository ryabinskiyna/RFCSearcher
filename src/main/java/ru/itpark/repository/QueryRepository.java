package ru.itpark.repository;

import ru.itpark.enumeration.QueryStatus;
import ru.itpark.model.QueryModel;

import java.util.List;

public interface QueryRepository {
    void init();

    List<QueryModel> getAll();

    void addQuery(QueryModel item);

    void updateQuery(QueryModel item);
}

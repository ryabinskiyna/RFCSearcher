package ru.itpark.service;

import ru.itpark.model.QueryModel;

import java.util.List;

public interface QueryService {
    void init();

    void destroy();

    List<QueryModel> getAll();

    String search(String query);
}

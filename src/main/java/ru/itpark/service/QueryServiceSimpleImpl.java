package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.enumeration.QueryStatus;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
// Это реализация без многопоточности
public class QueryServiceSimpleImpl implements QueryService {
    private final QueryRepository repository;
    private AtomicBoolean stopEvent;
    private LinkedBlockingQueue<QueryModel> queries;

    public QueryServiceSimpleImpl(QueryRepository repository, String searchPath, String resultsPath) {
        this.repository = repository;
        stopEvent = new AtomicBoolean(false);
        queries = new LinkedBlockingQueue<QueryModel>();

        Thread thread = new Thread(new QueryTask(repository, queries, stopEvent, searchPath, resultsPath));
        thread.start();
    }

    @Override
    public void init() {
        repository.init();
    }

    @Override
    public void destroy() {
        stopEvent.set(true);
    }

    @Override
    public List<QueryModel> getAll() {
        return repository.getAll();
    }

    @Override
    public String search(String query) {
        String id = UUID.randomUUID().toString();
        QueryModel model = new QueryModel();
        model.setId(id);
        model.setQuery(query);
        model.setStatus(QueryStatus.ENQUEUED);
        repository.addQuery(model);
        try {
            queries.put(model);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }
}

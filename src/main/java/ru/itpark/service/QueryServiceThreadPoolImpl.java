package ru.itpark.service;

import lombok.RequiredArgsConstructor;
import ru.itpark.enumeration.QueryStatus;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
// Это одна из реализаций с многопоточностью
public class QueryServiceThreadPoolImpl implements QueryService {
    private final QueryRepository repository;
    private final QuerySearch querySearch;
    private ExecutorService executor = Executors.newFixedThreadPool(10);

    public QueryServiceThreadPoolImpl(QueryRepository repository, String searchPath, String resultsPath) {
        this.repository = repository;
        this.querySearch = new QuerySearch(repository, searchPath, resultsPath);
    }

    @Override
    public void init() {
        repository.init();
    }

    @Override
    public void destroy() {
        executor.shutdownNow();
    }

    @Override
    public List<QueryModel> getAll() {
        return repository.getAll();
    }

    @Override
    public String search(String query) {
        QueryModel item = new QueryModel();
        String id = UUID.randomUUID().toString();
        item.setId(id);
        item.setQuery(query);
        item.setStatus(QueryStatus.ENQUEUED);
        repository.addQuery(item);
        executor.submit(() -> querySearch.search(item));

        return id;
    }
}

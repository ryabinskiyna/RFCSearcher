package ru.itpark.service;

import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueryTask implements Runnable {
    private BlockingQueue<QueryModel> queries;
    private AtomicBoolean stopEvent;
    private QuerySearch querySearch;

    public QueryTask(QueryRepository repository, BlockingQueue<QueryModel> queue, AtomicBoolean stopEvent, String searchPath, String resultsPath) {
        this.queries = queue;
        this.stopEvent = stopEvent;
        querySearch = new QuerySearch(repository, searchPath, resultsPath);
    }

    @Override
    public void run() {

        while (!stopEvent.get()) {
            try {
                QueryModel item = queries.poll(1000, TimeUnit.MILLISECONDS);
                if (item == null)
                    continue;
                querySearch.search(item);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

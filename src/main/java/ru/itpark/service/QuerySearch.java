package ru.itpark.service;

import ru.itpark.enumeration.QueryStatus;
import ru.itpark.model.QueryModel;
import ru.itpark.repository.QueryRepository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuerySearch {
    private final QueryRepository repository;
    private String searchPath;
    private String resultsPath;

    public QuerySearch(QueryRepository repository, String searchPath, String resultsPath) {
        this.repository = repository;
        this.searchPath = searchPath;
        this.resultsPath = resultsPath;
    }

    public void search(QueryModel item) {
        try {
            String query = item.getQuery();
            item.setStatus(QueryStatus.INPROGRESS);
            repository.updateQuery(item);

            File dir = new File(searchPath);
            File[] directoryListing = dir.listFiles();
            File outfile = new File(resultsPath + "\\" + item.getId() + ".txt");

            if (directoryListing != null) {
                for (File file : directoryListing) {
                    List<String> list = new ArrayList<String>();
                    try (Stream<String> lines = Files.lines(file.toPath())) {
                        list = lines.collect(Collectors.toList());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringBuilder result = new StringBuilder();

                    for (String line : list) {
                        if (line.contains(query))
                            result.append("[" + file.getName() + "]:" + line + System.lineSeparator());
                    }

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outfile, true))) {
                        writer.write(result.toString());
                    }
                }
            }
            item.setStatus(QueryStatus.DONE);
            repository.updateQuery(item);
        } catch (Exception e) {
            item.setStatus(QueryStatus.ERROR);
            repository.updateQuery(item);
            e.printStackTrace();
        }
    }
}

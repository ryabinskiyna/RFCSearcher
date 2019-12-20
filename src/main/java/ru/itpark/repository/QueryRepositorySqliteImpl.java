package ru.itpark.repository;

import lombok.RequiredArgsConstructor;
import ru.itpark.enumeration.QueryStatus;
import ru.itpark.model.QueryModel;
import ru.itpark.util.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RequiredArgsConstructor
public class QueryRepositorySqliteImpl implements QueryRepository {
    private final DataSource dataSource;

    @Override
    public void init() {
        JdbcTemplate.executeInit(
                dataSource,
                "CREATE TABLE queries (id TEXT PRIMARY KEY, query TEXT NOT NULL, status TEXT NOT NULL)"
        );
//        try (
//                Connection connection = dataSource.getConnection();
//                Statement statement = connection.createStatement()
//        ) {
//            statement.execute("CREATE TABLE queries (id TEXT PRIMARY KEY, query TEXT NOT NULL, status TEXT NOT NULL)");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public List<QueryModel> getAll() {
        return JdbcTemplate.executeQuery(
                dataSource,
                "SELECT id, query, status FROM queries",
                rs -> new QueryModel(
                        rs.getString("id"),
                        rs.getString("query"),
                        QueryStatus.valueOf(rs.getString("status"))

                )

        );
    }
//        try (
//                Connection connection = dataSource.getConnection();
//                Statement statement = connection.createStatement()
//        ) {
//            ResultSet resultSet = statement.executeQuery("SELECT id, query, status FROM queries");
//
//            LinkedList<QueryModel> res = new LinkedList<QueryModel>();
//            while (resultSet.next()) {
//                QueryModel item = new QueryModel();
//                item.setId(resultSet.getString("id"));
//                item.setQuery(resultSet.getString("query"));
//                try {
//                    item.setStatus(QueryModel.fromString(resultSet.getString("status")));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                res.add(item);
//            }
//            return res;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return Collections.emptyList();
//    }

    @Override
    public void addQuery(QueryModel item) {
        JdbcTemplate.executeCreateQuery(
                dataSource,
                "INSERT INTO queries (id, query, status) VALUES (?,?,?)",
                pstmt -> {
                    pstmt.setString(1, item.getId());
                    pstmt.setString(2, item.getQuery());
                    pstmt.setString(3, String.valueOf(item.getStatus()));
                }
        );
    }
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO queries (id, query, status) VALUES (?,?,?)")
//        ) {
//            pstmt.setString(1, item.getId());
//            pstmt.setString(2, item.getQuery());
//            pstmt.setString(3, QueryModel.toString(item.getStatus()));
//            pstmt.executeUpdate();
//            return true;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    public void updateQuery(QueryModel item) {
        JdbcTemplate.executeCreateQuery(
                dataSource,
                "UPDATE queries SET status = ? WHERE id = ?",
                pstmt -> {
                    pstmt.setString(1, String.valueOf(item.getStatus()));
                    pstmt.setString(2, item.getId());
                }


        );
    }
//        try (
//                Connection connection = dataSource.getConnection();
//                PreparedStatement pstmt = connection.prepareStatement("UPDATE queries SET status = ? WHERE id = ?")
//        ) {
//            pstmt.setString(1, QueryModel.toString(status));
//            pstmt.setString(2, id);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}

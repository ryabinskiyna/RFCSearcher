package ru.itpark.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itpark.enumeration.QueryStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryModel {
    private String id; // UUID
    private String query;
    private QueryStatus status;

//    public static QueryStatus fromString(String stat) throws Exception {
//        if (stat.contentEquals("ENQUEUED"))
//            return QueryStatus.ENQUEUED;
//
//        if (stat.contentEquals("INPROGRESS"))
//            return QueryStatus.INPROGRESS;
//
//        if (stat.contentEquals("DONE"))
//            return QueryStatus.DONE;
//
//        if (stat.contentEquals("ERROR"))
//            return QueryStatus.ERROR;
//
//        throw new Exception("Invalid status");
//    }

//    public static String toString(QueryStatus stat) {
//        if (stat == QueryStatus.ENQUEUED)
//            return "ENQUEUED";
//
//        if (stat == QueryStatus.INPROGRESS)
//            return "INPROGRESS";
//
//        if (stat == QueryStatus.DONE)
//            return "DONE";
//
//        if (stat == QueryStatus.ERROR)
//            return "ERROR";
//
//        return "";
//    }
}

package com.cloud.accelerator.commons;

import com.google.api.gax.paging.Page;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.Logging.EntryListOption;
import com.google.cloud.logging.Logging.ListOption;
import com.google.cloud.logging.Sink;
import com.google.cloud.logging.SinkInfo;
import com.google.cloud.logging.SinkInfo.Destination.DatasetDestination;


public class CloudLoggingUtils {

    static Logging logging ;

    public static Sink createSink(String sinkName, String datasetName) {
        SinkInfo sinkInfo = SinkInfo.of(sinkName, DatasetDestination.of(datasetName));
        Sink sink = logging.create(sinkInfo);
        return sink;
    }

    public static Sink updateSink(String sinkName, String datasetName) {
        // [START logging_update_sink]
        SinkInfo sinkInfo =
                SinkInfo.newBuilder(sinkName, DatasetDestination.of(datasetName))
                        .setVersionFormat(SinkInfo.VersionFormat.V2)
                        .setFilter("severity>=ERROR")
                        .build();
        Sink sink = logging.update(sinkInfo);
        // [END logging_update_sink]
        return sink;
    }


    public static Page<Sink> listSinks() {
        Page<Sink> sinks = logging.listSinks(ListOption.pageSize(100));
        for (Sink sink : sinks.iterateAll()) {
            // do something with the sink
        }
        // [END logging_list_sinks]
        return sinks;
    }


    public static boolean deleteSink(String sinkName) {
        // [START logging_delete_sink]
        boolean deleted = logging.deleteSink(sinkName);
        if (deleted) {
            // the sink was deleted
        } else {
            // the sink was not found
        }
        // [END logging_delete_sink]
        return deleted;
    }


    public static boolean deleteLog(String logName) {
        // [START logging_delete_log]
        boolean deleted = logging.deleteLog(logName);
        if (deleted) {
            // the log was deleted
        } else {
            // the log was not found
        }
        // [END logging_delete_log]
        return deleted;
    }

    /** Example of listing log entries for a specific log. */
    public static Page<LogEntry> listLogEntries(String filter) {
        Page<LogEntry> entries = logging.listLogEntries(EntryListOption.filter(filter));
        for (LogEntry entry : entries.iterateAll()) {
            // do something with the entry
        }
        return entries;
    }


}
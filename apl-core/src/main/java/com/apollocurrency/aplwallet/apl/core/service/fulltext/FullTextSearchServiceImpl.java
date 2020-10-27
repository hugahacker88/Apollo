/*
 *  Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.service.fulltext;

import com.apollocurrency.aplwallet.apl.core.service.appdata.DatabaseManager;
import com.apollocurrency.aplwallet.apl.util.annotation.DatabaseSpecificDml;
import com.apollocurrency.aplwallet.apl.util.annotation.DmlMarker;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Singleton
@DatabaseSpecificDml(DmlMarker.FULL_TEXT_SEARCH)
public class FullTextSearchServiceImpl implements FullTextSearchService {
    private FullTextSearchEngine fullTextSearchEngine;
    private Map<String, String> fullTextSearchIndexedTables;
    private String schemaName;
    private DatabaseManager databaseManager;

    @Inject
    public FullTextSearchServiceImpl(DatabaseManager databaseManager, FullTextSearchEngine fullTextSearchEngine,
                                     @Named(value = "fullTextTables") Map<String, String> fullTextSearchIndexedTables,
                                     @Named(value = "tablesSchema") String schemaName) {
        this.databaseManager = databaseManager;
        this.fullTextSearchEngine = fullTextSearchEngine;
        this.fullTextSearchIndexedTables = fullTextSearchIndexedTables;
        this.schemaName = schemaName;
    }

    /**
     * Drop the fulltext index for a table
     *
     * @param conn   SQL connection
     * @param schema Schema name
     * @param table  Table name
     * @throws SQLException Unable to drop fulltext index
     */
    public void dropIndex(Connection conn, String schema, String table) throws SQLException {
        String upperSchema = schema.toUpperCase();
        String upperTable = table.toUpperCase();
        boolean reindex = false;
        //
        // Drop an existing database trigger
        //
        try (Statement qstmt = conn.createStatement();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = qstmt.executeQuery(String.format(
                "SELECT columns FROM ftl_indexes WHERE `table` = '%s'",
                upperSchema, upperTable))) {
                if (rs.next()) {
//                    stmt.execute("DROP TRIGGER IF EXISTS FTL_" + upperTable);
                    stmt.execute(String.format("DELETE FROM ftl_indexes WHERE `table` = '%s'",
                        upperSchema, upperTable));
                    reindex = true;
                }
            }
        }
        //
        // Rebuild the Lucene index
        //
        if (reindex) {
            reindexAll(conn, schema);
        }
    }

    /**
     * Initialize the fulltext support for a new database
     * <p>
     * This method should be called from AplDbVersion when performing the database version update
     * that enables fulltext search support
     */
//    @PostConstruct
    public void init() {
        boolean isIndexFolderEmpty;
        try {
            isIndexFolderEmpty = fullTextSearchEngine.isIndexFolderEmpty(); // first check if index is deleted manually
            log.debug("init = (isIndexFolderEmpty = {})", isIndexFolderEmpty);
            fullTextSearchEngine.init(); // some files are created by initialization
        } catch (IOException e) {
            throw new RuntimeException("Unable to init fulltext engine", e);
        }
        String fullTextTableName = "ftl_indexes";
        log.debug("fullTextTableName = {}", fullTextTableName);
        try (Connection conn = databaseManager.getDataSource().getConnection();
             Statement stmt = conn.createStatement();
             Statement qstmt = conn.createStatement()) {
            //
            // Check if we have already been initialized.
            //
            boolean alreadyInitialized = true;
            boolean indexesInfoTableExist = false;
            try (ResultSet rs = qstmt.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE TABLE_NAME = 'ftl_indexes'")) {
                while (rs.next()) {
                    indexesInfoTableExist = true;
                    if (!rs.getString(1).startsWith(fullTextTableName)) {
                        alreadyInitialized = false;
                    }
                }
            }
            if (indexesInfoTableExist && alreadyInitialized && !isIndexFolderEmpty) {
                log.info("Fulltext support is already initialized");
                return;
            }
            //
            // We need to delete an existing Lucene index since the V3 file format is not compatible with V5
            //
            fullTextSearchEngine.clearIndex();
            //
            // Drop the H2 Lucene V3 function aliases
            // Mainly for backward compatibility with old databases, which
            // full text search was implemented by using built-in h2 trigger
            // org.h2.fulltext.FullTextLucene.init
            //
//            stmt.execute("DROP ALIAS IF EXISTS FTL_INIT");
//            stmt.execute("DROP ALIAS IF EXISTS FTL_DROP_ALL");
//            stmt.execute("DROP ALIAS IF EXISTS FTL_REINDEX");
//            stmt.execute("DROP ALIAS IF EXISTS FTL_SEARCH_DATA");

            // Drop our fulltext function aliases, we should not depend on stored procedures
            // since it hard wire us with h2
            //
//            stmt.execute("DROP ALIAS IF EXISTS FTL_SEARCH");
//            stmt.execute("DROP ALIAS IF EXISTS FTL_CREATE_INDEX");
//            stmt.execute("DROP ALIAS IF EXISTS FTL_DROP_INDEX");

            log.info("H2 fulltext function aliases dropped");
            //
            // Create our schema and table
            //
//            stmt.execute("CREATE SCHEMA IF NOT EXISTS FTL");
            boolean createResult = stmt.execute("CREATE TABLE IF NOT EXISTS ftl_indexes "
                + "(`schema` VARCHAR(20), `table` VARCHAR(100), columns VARCHAR(200), PRIMARY KEY(`schema`, `table`))");
            log.info("fulltext table is created = '{}'", createResult);
            //
            // Drop existing triggers and create our triggers.  H2 will initialize the trigger
            // when it is created.  H2 has already initialized the existing triggers and they
            // will be closed when dropped.  The H2 Lucene V3 trigger initialization will work with
            // Lucene V5, so we are able to open the database using the Lucene V5 library files.
            //
            Long recordCount = -1L;
            try (ResultSet rs = qstmt.executeQuery("SELECT count(*) as count FROM ftl_indexes")) {
                while (rs.next()) {
                    recordCount = rs.getLong("count");
/*
                    String table = rs.getString("table");
                    String columns = rs.getString("columns");
                    stmt.execute("DROP TRIGGER IF EXISTS FTL_" + table);
                    stmt.execute(String.format("CREATE TRIGGER FTL_%s AFTER INSERT,UPDATE,DELETE ON %s.%s "
                            + "FOR EACH ROW CALL \"%s\"",
                        table, schema, table, fullTextTableName));
*/
                }
            }

            if (recordCount == 0) { // skip if table if filled with initial data
                String tableName = null;
                try {
                    for (String s : fullTextSearchIndexedTables.keySet()) {
                        tableName = s;
                        String indexedColumns = fullTextSearchIndexedTables.get(tableName);
                        stmt.execute(String.format("INSERT INTO ftl_indexes VALUES('%s', '%s', '%s')",
                            this.schemaName.toLowerCase(), tableName.toLowerCase(), indexedColumns.toLowerCase()));
                        reindex(conn, tableName, schemaName);
                        log.info("Lucene search index created for table {}", tableName);
                    }
                } catch (SQLException exc) {
                    log.error("Unable to create Lucene search index for table " + tableName);
                    throw new SQLException("Unable to create Lucene search index for table " + tableName, exc);
                }
            }
            //
            // Rebuild the Lucene index since the Lucene V3 index is not compatible with Lucene V5
            //
//            if (recordCount > 0) { // no sense to reindex if all info is initialized
//                reindexAll(conn);
//            }

//            //
//            // Create our function aliases
//            //
//            stmt.execute("CREATE ALIAS FTL_SEARCH NOBUFFER FOR \"" + FullTextStoredProcedures.class.getName() + ".search\"");
            log.info("Fulltext aliases created");
        } catch (SQLException exc) {
            log.error("Unable to initialize fulltext search support", exc);
            throw new RuntimeException(exc.toString(), exc);
        }
    }

    /**
     * Drop all fulltext indexes
     *
     * @param conn SQL connection
     * @throws SQLException Unable to drop fulltext indexes
     */
    public void dropAll(Connection conn) throws SQLException {
        //
        // Drop existing triggers
        //
        try (Statement stmt = conn.createStatement()/*;
             Statement qstmt = conn.createStatement();
             ResultSet rs = qstmt.executeQuery("SELECT `table` FROM ftl_indexes")*/) {
/*            while (rs.next()) {
                String table = rs.getString(1);
                stmt.execute("TRUNCATE TABLE " + table);
            }*/
            stmt.execute("DROP TABLE ftl_indexes");
        }
        //
        // Delete the Lucene index
        //
        fullTextSearchEngine.clearIndex();
    }

    public ResultSet search(String schema, String table, String queryText, int limit, int offset)
        throws SQLException {
        return fullTextSearchEngine.search(schema, table, queryText, limit, offset);
    }

    @Override
    public void shutdown() {
        try {
            fullTextSearchEngine.shutdown();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void reindex(Connection conn, String tableName, String schemaName) throws SQLException {
        //
        // Build the SELECT statement for just the indexed columns
        //
        TableData tableData = DbUtils.getTableData(conn, tableName, schemaName);
        if (tableData.getDbIdColumnPosition() == -1) {
            log.debug("Table {} has not dbId column", tableName);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT db_id");
        for (int index : tableData.getIndexColumns()) {
            sb.append(", ").append(tableData.getColumnNames().get(index));
        }
        sb.append(" FROM ").append(tableName);
//        Object[] row = new Object[tableData.getColumnNames().size()];

        boolean isIndexAppended = false;
        //
        // Index each row in the table
        //
        FullTextOperationData operationData = new FullTextOperationData(
            FullTextOperationData.OperationType.INSERT_UPDATE, tableName);
        try (Statement qstmt = conn.createStatement();
             ResultSet rs = qstmt.executeQuery(sb.toString())) {
            while (rs.next()) {
                int i = 0;
                Object dbId = rs.getObject(i+1);
                operationData.setTableKey(schemaName + "." + tableName + ";DB_ID;" + dbId);
                i++;
                Iterator it = tableData.getIndexColumns().iterator();
                while (it.hasNext()) {
                    Object indexedColumnValue = rs.getObject(i+1);
                    operationData.addColumnData(indexedColumnValue);
                    it.next(); // move forward
                    i++;
                }
                if (operationData.getColumnsWithData().size() > 0) {
                    log.debug("Index data = {}", operationData);
                    fullTextSearchEngine.indexRow(operationData, tableData);
                    isIndexAppended = true;
                }
            }
        }
        //
        // Commit the index updates
        //
        if (isIndexAppended) {
            fullTextSearchEngine.commitIndex();
        }
    }

    public void reindexAll(Connection conn) throws SQLException {
        reindexAll(conn, schemaName);
    }

    private void reindexAll(Connection conn, String schema) throws SQLException {
        long start = System.currentTimeMillis();
        log.info("Rebuilding Lucene search index");
        try {
            //
            // Delete the current Lucene index
            //
            fullTextSearchEngine.clearIndex();
            //
            // Reindex each table
            //
            for (String tableName : this.fullTextSearchIndexedTables.keySet()) {
                long startTable = System.currentTimeMillis();
                log.debug("Reindexing '{}' starting...", tableName);
                reindex(conn, tableName, schema);
                log.debug("Reindexing '{}' DONE in '{}' ms", tableName, System.currentTimeMillis() - startTable);
            }
        } catch (SQLException exc) {
            throw new SQLException("Unable to rebuild the Lucene index", exc);
        }
        log.info("Rebuilding Lucene search index DONE in '{}' ms", System.currentTimeMillis() - start);
    }


    /**
     * Creates a new index for table to support fulltext search.
     * Note that a schema is always PUBLIC.
     *
     * @param con                   DB connection
     * @param table                 name of table for indexing
     * @param fullTextSearchColumns list of columns for indexing separated by comma
     * @throws SQLException when unable to create index
     */
    @Override
    public final void createSearchIndex(
        final Connection con,
        final String table,
        final String fullTextSearchColumns
    ) throws SQLException {
        if (fullTextSearchColumns != null) {
            log.debug("Creating search index on {} ({})", table, fullTextSearchColumns);
            String table1 = table.toLowerCase();
            String upperSchema = schemaName.toLowerCase();
            String upperTable = table1.toLowerCase();
//            String tableName = upperSchema + "." + upperTable;
            //
            // Drop an existing index and the associated database trigger
            //
            dropIndex(con, schemaName, table1);
            //
            // Update our schema and create a new database trigger.  Note that the trigger
            // will be initialized when it is created.
            //
/*
            try (Statement stmt = con.createStatement()) {
                stmt.execute(String.format("INSERT INTO ftl_indexes (`schema`, `table', columns) VALUES('%s', '%s', '%s')",
                    upperSchema.toLowerCase(), upperTable.toLowerCase(), fullTextSearchColumns.toLowerCase()));
                stmt.execute(String.format("CREATE TRIGGER FTL_%s AFTER INSERT,UPDATE,DELETE ON %s "
                        + "FOR EACH ROW CALL \"%s\"",
                    upperTable, tableName, FullTextTrigger.class.getName()));
            }
*/
            boolean isTableDataExist = false;
            try (Statement stmt = con.createStatement()) {
                ResultSet rs = stmt.executeQuery(String.format("SELECT count(*) as count FROM ftl_indexes WHERE `schema` = '%s' AND `table` = '%s'",
                    upperSchema.toLowerCase(), upperTable.toLowerCase()));
                if (rs.next()) {
                    isTableDataExist = rs.getLong("count") > 0;
                }
            }
            //
            // Index the table
            //
            if (!isTableDataExist) {
                try (Statement stmt = con.createStatement()) {
                    stmt.execute(String.format("INSERT INTO ftl_indexes VALUES('%s', '%s', '%s')",
                        upperSchema.toLowerCase(), upperTable.toLowerCase(), fullTextSearchColumns.toLowerCase()));
                    reindex(con, upperTable, schemaName);
                    log.info("Lucene search index created for table " + upperTable);
                } catch (SQLException exc) {
                    log.error("Unable to create Lucene search index for table " + upperTable);
                    throw new SQLException("Unable to create Lucene search index for table " + upperTable, exc);
                }
            }
        }
    }
}

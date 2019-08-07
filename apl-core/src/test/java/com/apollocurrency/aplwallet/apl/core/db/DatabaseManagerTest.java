/*
 *  Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.apollocurrency.aplwallet.apl.core.db.cdi.transaction.JdbiHandleFactory;
import com.apollocurrency.aplwallet.apl.core.shard.ShardManagement;
import com.apollocurrency.aplwallet.apl.data.DbTestData;
import com.apollocurrency.aplwallet.apl.extension.TemporaryFolderExtension;
import com.apollocurrency.aplwallet.apl.testutil.DbPopulator;
import com.apollocurrency.aplwallet.apl.util.Constants;
import com.apollocurrency.aplwallet.apl.util.injectable.DbProperties;
import com.apollocurrency.aplwallet.apl.util.injectable.PropertiesHolder;
import org.bouncycastle.asn1.esf.CompleteRevocationRefs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


class DatabaseManagerTest {

    private String TEMP_FILE_NAME = "apl-temp-utest-db-name";

    private static PropertiesHolder propertiesHolder = new PropertiesHolder();
    @RegisterExtension
    static TemporaryFolderExtension temporaryFolderExtension = new TemporaryFolderExtension();
    private DbProperties baseDbProperties;
    private DatabaseManagerImpl databaseManager;
    @BeforeEach
    public void setUp() throws IOException {
        Path dbFilePath = temporaryFolderExtension.newFolder().toPath().resolve(Constants.APPLICATION_DIR_NAME);
        baseDbProperties = DbTestData.getDbFileProperties(dbFilePath.toAbsolutePath().toString());
        databaseManager = new DatabaseManagerImpl(baseDbProperties, propertiesHolder, new JdbiHandleFactory());
        DbPopulator dbPopulator = new DbPopulator(databaseManager.getDataSource(), "db/schema.sql", "db/db-manager-data.sql");
        dbPopulator.initDb();
        dbPopulator.populateDb();
        databaseManager.initFullShards(Set.of(2L, 3L));
    }

    @AfterEach
    public void tearDown() {
        databaseManager.shutdown();
    }

    @Test
    void init() {

        assertNotNull(databaseManager.getJdbi());
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    void createAndAddShard() throws Exception {
        assertNotNull(databaseManager.getJdbi());
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource newShardDb = ((ShardManagement)databaseManager).createAndAddShard(1L);
        assertNotNull(newShardDb);
        assertNotNull(newShardDb.getConnection());
    }

    @Test
    void createShardInitTableSchemaVersion() throws Exception {
        assertNotNull(databaseManager.getJdbi());
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource newShardDb = ((ShardManagement)databaseManager).createAndAddShard(1L, new ShardInitTableSchemaVersion());
        assertNotNull(newShardDb);
        Connection newShardDbConnection = newShardDb.getConnection();
        assertNotNull(newShardDbConnection);
        checkTablesCreated(newShardDbConnection);
    }

    private void checkTablesCreated(Connection newShardDbConnection) throws SQLException {
        PreparedStatement sqlStatement = newShardDbConnection.prepareStatement("select * from BLOCK");
        sqlStatement.execute();
        sqlStatement = newShardDbConnection.prepareStatement("select * from TRANSACTION");
        sqlStatement.execute();
    }

    @Test
    void createAndAddShardWithoutId() throws Exception {
        assertNotNull(databaseManager.getJdbi());
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource newShardDb = ((ShardManagement)databaseManager).createAndAddShard(null);
        assertNotNull(newShardDb);
        Connection newShardDbConnection = newShardDb.getConnection();
        assertNotNull(newShardDbConnection);
        checkTablesCreated(newShardDbConnection);
    }

    @Test
    void createShardAddConstraintsSchemaVersion() throws Exception {
        assertNotNull(databaseManager);
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource newShardDb = ((ShardManagement)databaseManager).createAndAddShard(1L, new ShardAddConstraintsSchemaVersion());
        assertNotNull(newShardDb);
        Connection newShardDbConnection = newShardDb.getConnection();
        assertNotNull(newShardDbConnection);
        checkTablesCreated(newShardDbConnection);
    }

    @Test
    void createShardTwoSchemaVersion() throws Exception {
        assertNotNull(databaseManager);
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource newShardDb = ((ShardManagement)databaseManager).createAndAddShard(1L, new ShardInitTableSchemaVersion());
        assertNotNull(newShardDb);
        assertNotNull(newShardDb.getConnection());
        newShardDb = ((ShardManagement)databaseManager).createAndAddShard(1L, new ShardAddConstraintsSchemaVersion());
        assertNotNull(newShardDb);
        Connection newShardDbConnection = newShardDb.getConnection();
        assertNotNull(newShardDbConnection);
        checkTablesCreated(newShardDbConnection);
    }

    @Test
    void createTemporaryDb() throws Exception {
        assertNotNull(databaseManager);
        TransactionalDataSource dataSource = databaseManager.getDataSource();
        assertNotNull(dataSource);
        TransactionalDataSource temporaryDb = ((ShardManagement)databaseManager).createAndAddTemporaryDb(TEMP_FILE_NAME);
        assertNotNull(temporaryDb);
        assertNotNull(temporaryDb.getConnection());
    }

    @Test
    void testFindFullDatasources() {
        List<TransactionalDataSource> fullDatasources = ((ShardManagement) databaseManager).getFullDatasources();
        assertEquals(2, fullDatasources.size());
        assertTrue(fullDatasources.get(0).getUrl().contains("shard-3"), "First datasource should represent full shard with id 3 (sorted by shard id desc)");
        assertTrue(fullDatasources.get(1).getUrl().contains("shard-2"), "Second datasource should represent full shard with id 2 (sorted by shard id desc)");

    }

    @Test
    void testGetOrInitFullShardDataSourceForShardWhichNotExist() {
        TransactionalDataSource dataSource = ((ShardManagement) databaseManager).getOrInitFullShardDataSourceById(0L);

        assertNull(dataSource, "Shard datasource with shardId=0 should not exist");
    }

    @Test
    void testGetOrInitFullShardDataSourceForNotFullShard() {
        TransactionalDataSource dataSource = ((ShardManagement) databaseManager).getOrInitFullShardDataSourceById(1L);

        assertNull(dataSource, "Shard datasource with shardId=1 should not be full, (shard state != 100)");
    }

    @Test
    void testGetOrInitFullShardDataSourceForFullShardId() {
        TransactionalDataSource dataSource = ((ShardManagement) databaseManager).getOrInitFullShardDataSourceById(2L);

        assertNotNull(dataSource, "Shard datasource with shardId=2 should be full, (shard state = 100)");
        assertTrue(dataSource.getUrl().contains("shard-2"), "Datasource should represent full shard with id 2");
    }

    @Test
    void testGetDatasourceWhenDbIsUnavailable() throws ExecutionException, InterruptedException {
        TransactionalDataSource currentDatasource = databaseManager.getDataSource();
        databaseManager.setAvailable(false);
        CompletableFuture<Void> getDatasourceFuture = CompletableFuture.supplyAsync(() -> {
                checkDatasource(databaseManager.getDataSource());
                return null;
        });
        CompletableFuture<Void> setAvailableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
                databaseManager.setAvailable(true);
                return null;
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        setAvailableFuture.get();
        getDatasourceFuture.get();
        assertSame(currentDatasource, databaseManager.getDataSource());
    }
    @Test
    void testDoubleShutdownAndGetDatasource() {
        TransactionalDataSource currentDatasource = databaseManager.getDataSource();
        List<TransactionalDataSource> fullDatasources = ((ShardManagement) databaseManager).getFullDatasources();
        databaseManager.shutdown();
        databaseManager.shutdown();
        assertTrue(currentDatasource.isShutdown());
        for (TransactionalDataSource fullDatasource : fullDatasources) {
            assertTrue(fullDatasource.isShutdown());
        }
        TransactionalDataSource newCurrentDatasource = databaseManager.getDataSource();
        assertNotSame(currentDatasource, newCurrentDatasource);
        checkDatasource(newCurrentDatasource);

        List<TransactionalDataSource> newFullDatasources = ((ShardManagement) databaseManager).getFullDatasources();
        for (int i = 0; i < newFullDatasources.size(); i++) {
            assertNotSame(fullDatasources.get(i), newFullDatasources.get(i));
            checkDatasource(newFullDatasources.get(i));
        }
    }

    @Test
    void testAddNewFullShardId() {
        databaseManager.addFullShard(1L);
        List<TransactionalDataSource> fullDatasources = databaseManager.getFullDatasources();
        assertEquals(3, fullDatasources.size());
        for (int i = 0; i < 3; i++) {
            assertTrue(fullDatasources.get(i).getUrl().contains("shard-" + (3 - i)));
            checkDatasource(fullDatasources.get(i));
        }
        assertSame(fullDatasources.get(2), databaseManager.getShardDataSourceById(1L));
    }

    @Test
    void testCreateShardDataSourceById() {
        TransactionalDataSource datasource = databaseManager.getOrCreateShardDataSourceById(2L);
        checkDatasource(datasource);
        assertTrue(datasource.getUrl().contains("shard-2"));
    }
    @Test
    void testGetExistingShardDataSourceById() {
        List<TransactionalDataSource> fullDatasources = databaseManager.getFullDatasources();
        TransactionalDataSource datasource = databaseManager.getOrCreateShardDataSourceById(2L);
        checkDatasource(datasource);
        assertTrue(datasource.getUrl().contains("shard-2"));
        assertSame(fullDatasources.get(1), datasource);
    }

    @Test
    void testGetExistingShardDataSourceByIdWithVersion() {
        List<TransactionalDataSource> fullDatasources = databaseManager.getFullDatasources();
        TransactionalDataSource datasource = databaseManager.getOrCreateShardDataSourceById(3L, new ShardInitTableSchemaVersion());
        checkDatasource(datasource);
        assertTrue(datasource.getUrl().contains("shard-3"));
        assertSame(fullDatasources.get(0), datasource);
    }

    private void checkDatasource(TransactionalDataSource dataSource) {
        try {
            assertFalse(dataSource.isShutdown());
            ResultSet rs = dataSource.getConnection().createStatement().executeQuery("select 1");
            assertTrue(rs.next());
        }
        catch (SQLException e) {
            throw new RuntimeException(e.toString());
        }
    }

}
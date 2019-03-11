package com.apollocurrency.aplwallet.apl.core.shard;

import java.util.Map;

import com.apollocurrency.aplwallet.apl.core.db.DatabaseManager;
import com.apollocurrency.aplwallet.apl.core.db.DbVersion;

/**
 * Interface + Implementation for transferring data between main database and shard database.
 * Also renaming database files.
 */
public interface DataTransferManagementReceiver {

    String TEMPORARY_MIGRATION_FILE_NAME = "apl-temp-migration";
    String PREVIOUS_MIGRATION_KEY = "SHARD_MIGRATION_STATUS";
    String LAST_MIGRATION_OBJECT_NAME = "LAST_MIGRATION_OBJECT_NAME";

//    Map<String, Long> getTableNameWithCountMap();

    DatabaseManager getDatabaseManager();

    MigrateState getCurrentState();

    MigrateState addOrCreateShard(DatabaseMetaInfo source, DbVersion dbVersion);

//    MigrateState createTempDb(DatabaseMetaInfo source);

//    MigrateState addSnapshotBlock(DatabaseMetaInfo targetDataSource);

    MigrateState moveDataBlockLinkedData(Map<String, Long> tableNameCountMap, DatabaseMetaInfo source, DatabaseMetaInfo target);

    MigrateState moveData(Map<String, Long> tableNameCountMap, DatabaseMetaInfo source, DatabaseMetaInfo target);

    MigrateState renameDataFiles(DatabaseMetaInfo source, DatabaseMetaInfo target);

}

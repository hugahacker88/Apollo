/*
 *  Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.app;

import com.apollocurrency.aplwallet.apl.core.app.observer.events.Async;
import com.apollocurrency.aplwallet.apl.core.app.observer.events.Sync;
import com.apollocurrency.aplwallet.apl.core.config.Property;
import com.apollocurrency.aplwallet.apl.core.db.DatabaseManager;
import com.apollocurrency.aplwallet.apl.core.db.DerivedTablesRegistry;
import com.apollocurrency.aplwallet.apl.core.db.TransactionalDataSource;
import com.apollocurrency.aplwallet.apl.core.db.derived.DerivedTableInterface;
import com.apollocurrency.aplwallet.apl.core.shard.observer.TrimData;
import com.apollocurrency.aplwallet.apl.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import javax.enterprise.event.Event;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TrimService {
    private static final Logger log = LoggerFactory.getLogger(TrimService.class);
    private final int maxRollback;
    private final int trimFrequency;
    private final DatabaseManager dbManager;
    private final DerivedTablesRegistry dbTablesRegistry;
    private final TrimDao trimDao;
    private final GlobalSync globalSync;

    private Event<TrimData> trimEvent;



    @Inject
    public TrimService(DatabaseManager databaseManager,
                       DerivedTablesRegistry derivedDbTablesRegistry,
                       GlobalSync globalSync,
                       Event<TrimData> trimEvent,
                       TrimDao trimDao,
                       @Property(value = "apl.maxRollback", defaultValue = "720") int maxRollback
    ) {
        this.maxRollback = maxRollback;
        this.trimDao = trimDao;
        this.dbManager = Objects.requireNonNull(databaseManager, "Database manager cannot be null");
        this.dbTablesRegistry = Objects.requireNonNull(derivedDbTablesRegistry, "Db tables registry cannot be null");
        this.globalSync = Objects.requireNonNull(globalSync, "Synchronization service cannot be null");
        this.trimFrequency = Constants.DEFAULT_TRIM_FREQUENCY;
        this.trimEvent = Objects.requireNonNull(trimEvent, "Trim event should not be null");
    }

    public int getLastTrimHeight() {
        TrimEntry trimEntry = trimDao.get();
        return trimEntry == null ? 0 : Math.max(trimEntry.getHeight() - maxRollback, 0);
    }


    public void init(int height) {
        TrimEntry trimEntry = trimDao.get();
        int lastTrimHeight = trimEntry == null ? 0 : trimEntry.getHeight();
        log.info("Last trim height was {}", lastTrimHeight);
        if (trimEntry != null) {
            if (!trimEntry.isDone()) {
                log.info("Finish trim at height {}", lastTrimHeight);
                trimDerivedTables(lastTrimHeight, false);
            }
        }
        for (int i = lastTrimHeight + trimFrequency; i <= height; i += trimFrequency) {
            log.debug("Perform trim on height {}", i);
            trimDerivedTables(i, false);
        }
    }



    public void trimDerivedTables(int height, boolean async) {

        TransactionalDataSource dataSource = dbManager.getDataSource();
        boolean inTransaction = dataSource.isInTransaction();
        try {
            if (!inTransaction) {
                dataSource.begin();
            }
            long startTime = System.currentTimeMillis();
            doTrimDerivedTablesOnBlockchainHeight(height, async);
            log.debug("Total trim time: " + (System.currentTimeMillis() - startTime));
            dataSource.commit(!inTransaction);

        }
        catch (Exception e) {
            log.info(e.toString(), e);
            dataSource.rollback(!inTransaction);
            throw e;
        }
    }

    public void doTrimDerivedTablesOnBlockchainHeight(int blockchainHeight, boolean async) {
        int trimHeight = Math.max(blockchainHeight - maxRollback, 0);
        if (trimHeight > 0) {

            TrimEntry trimEntry = new TrimEntry(null, blockchainHeight, false);
            trimDao.clear();
            trimEntry = trimDao.save(trimEntry);
            dbManager.getDataSource().commit(false);

            doTrimDerivedTablesOnHeight(trimHeight, false);
            if (async) {
                trimEvent.select(new AnnotationLiteral<Async>() {}).fire(new TrimData(trimHeight, blockchainHeight));
            } else {
                trimEvent.select(new AnnotationLiteral<Sync>() {}).fire(new TrimData(trimHeight, blockchainHeight));
            }
            trimEntry.setDone(true);
            trimDao.save(trimEntry);

        }
    }
    public void resetTrim() {
        trimDao.clear();
    }

    public void doTrimDerivedTablesOnHeight(int height, boolean oneLock) {
        TransactionalDataSource dataSource = dbManager.getDataSource();
        if (oneLock) {
            globalSync.readLock();
        }
        long onlyTrimTime = 0;
        try {
            for (DerivedTableInterface table : dbTablesRegistry.getDerivedTables()) {
                if (!oneLock) {
                    globalSync.readLock();
                }
                try {
                    long startTime = System.currentTimeMillis();
                    table.trim(height);
                    dataSource.commit(false);
                    onlyTrimTime += (System.currentTimeMillis() - startTime);
                }
                finally {
                    if (!oneLock) {
                        globalSync.readUnlock();
                    }
                }
            }
        }
        finally {
            if (oneLock) {
                globalSync.readUnlock();
            }
        }
        log.debug("Only trim time: " + onlyTrimTime);
    }
}

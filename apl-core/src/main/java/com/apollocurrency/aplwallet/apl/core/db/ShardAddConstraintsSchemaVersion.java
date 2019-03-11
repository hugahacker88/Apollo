/*
 * Copyright © 2013-2016 The Nxt Core Developers.
 * Copyright © 2016-2017 Jelurida IP B.V.
 *
 * See the LICENSE.txt file at the top-level directory of this distribution
 * for licensing information.
 *
 * Unless otherwise agreed in a custom licensing agreement with Jelurida B.V.,
 * no part of the Nxt software, including this file, may be copied, modified,
 * propagated, or distributed except according to the terms contained in the
 * LICENSE.txt file.
 * 
 * Removal or modification of this copyright notice is prohibited.
 *
 */

/*
 * Copyright © 2018 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.db;

import static org.slf4j.LoggerFactory.getLogger;

import javax.sql.DataSource;

import org.slf4j.Logger;

public class ShardAddConstraintsSchemaVersion extends ShardInitTableSchemaVersion {
    private static final Logger log = getLogger(ShardAddConstraintsSchemaVersion.class);

    protected int update(int nextUpdate) {
        nextUpdate = super.update(nextUpdate);
        switch (nextUpdate) {
/*  ---------------------- BLOCK -------------------    */
            case 8:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("ALTER TABLE block ADD CONSTRAINT IF NOT EXISTS chk_timeout CHECK (timeout >= 0)");
            case 9:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("alter table BLOCK add constraint IF NOT EXISTS BLOCK_PK primary key (DB_ID)");
            case 10:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("alter table BLOCK add constraint IF NOT EXISTS BLOCK_ID_IDX unique (ID)");
            case 11:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("alter table BLOCK add constraint IF NOT EXISTS BLOCK_TIMESTAMP_IDX unique (TIMESTAMP)");
            case 12:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("alter table BLOCK add constraint IF NOT EXISTS BLOCK_HEIGHT_IDX unique (HEIGHT)");
            case 13:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("create unique index IF NOT EXISTS BLOCK_DB_ID_UINDEX on BLOCK (DB_ID)");
            case 14:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("create unique index IF NOT EXISTS PRIMARY_KEY_BLOCK_DB_ID_INDEX on BLOCK (DB_ID)");
            case 15:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("create index IF NOT EXISTS BLOCK_GENERATOR_ID_IDX on BLOCK (GENERATOR_ID)");
            case 16:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("CREATE UNIQUE INDEX IF NOT EXISTS block_id_idx ON block (id)");
            case 17:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("CREATE UNIQUE INDEX IF NOT EXISTS block_height_idx ON block (height)");
            case 18:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("CREATE INDEX IF NOT EXISTS block_generator_id_idx ON block (generator_id)");
            case 19:
                log.trace("Starting adding BLOCK constraint = {}", nextUpdate);
                apply("CREATE UNIQUE INDEX IF NOT EXISTS block_timestamp_idx ON block (timestamp DESC)");
/*  ---------------------- TRANSACTION -------------------    */
            case 20:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("alter table TRANSACTION add constraint IF NOT EXISTS TRANSACTION_PK primary key (DB_ID)");
            case 21:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("alter table TRANSACTION add constraint IF NOT EXISTS TRANSACTION_ID_IDX unique (ID)");
            case 22:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("ALTER TABLE TRANSACTION ADD CONSTRAINT IF NOT EXISTS TRANSACTION_TO_BLOCK_FK FOREIGN KEY (block_id) REFERENCES block (id) ON DELETE CASCADE");
            case 23:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("create unique index IF NOT EXISTS TRANSACTION_DB_ID_UINDEX on TRANSACTION (DB_ID)");
            case 24:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("CREATE UNIQUE INDEX IF NOT EXISTS transaction_id_idx ON transaction (id)");
            case 25:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("CREATE INDEX IF NOT EXISTS transaction_sender_id_idx ON transaction (sender_id)");
            case 26:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("CREATE INDEX IF NOT EXISTS transaction_recipient_id_idx ON transaction (recipient_id)");
            case 27:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("CREATE INDEX IF NOT EXISTS transaction_block_timestamp_idx ON transaction (block_timestamp DESC)");
            case 28:
                log.trace("Starting adding TRANSACTION constraint = {}", nextUpdate);
                apply("CREATE INDEX IF NOT EXISTS referenced_transaction_referenced_transaction_id_idx ON referenced_transaction (referenced_transaction_id)");
            case 29:
                return 29;
            default:
                throw new RuntimeException("Shard ADD CONSTRAINTS/INDEXES database is inconsistent with code, at update " + nextUpdate
                        + ", probably trying to run older code on newer database");
        }
    }

    @Override
    void init(DataSource dataSource) {
        super.init(dataSource);
    }

    @Override
    protected void apply(String sql) {
        super.apply(sql);
    }
}

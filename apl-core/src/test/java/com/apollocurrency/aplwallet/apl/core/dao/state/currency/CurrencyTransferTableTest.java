/*
 *  Copyright © 2018-2020 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.dao.state.currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;

import com.apollocurrency.aplwallet.apl.core.app.Blockchain;
import com.apollocurrency.aplwallet.apl.core.app.BlockchainImpl;
import com.apollocurrency.aplwallet.apl.core.app.BlockchainProcessor;
import com.apollocurrency.aplwallet.apl.core.app.BlockchainProcessorImpl;
import com.apollocurrency.aplwallet.apl.core.app.CollectionUtil;
import com.apollocurrency.aplwallet.apl.core.chainid.BlockchainConfig;
import com.apollocurrency.aplwallet.apl.core.db.DatabaseManager;
import com.apollocurrency.aplwallet.apl.core.db.DbIterator;
import com.apollocurrency.aplwallet.apl.core.db.DerivedDbTablesRegistryImpl;
import com.apollocurrency.aplwallet.apl.core.db.DerivedTablesRegistry;
import com.apollocurrency.aplwallet.apl.core.db.fulltext.FullTextConfig;
import com.apollocurrency.aplwallet.apl.core.db.fulltext.FullTextConfigImpl;
import com.apollocurrency.aplwallet.apl.core.entity.state.currency.CurrencyTransfer;
import com.apollocurrency.aplwallet.apl.data.CurrencyTransferTestData;
import com.apollocurrency.aplwallet.apl.data.DbTestData;
import com.apollocurrency.aplwallet.apl.extension.DbExtension;
import com.apollocurrency.aplwallet.apl.testutil.DbUtils;
import com.apollocurrency.aplwallet.apl.util.injectable.PropertiesHolder;
import org.jboss.weld.junit.MockBean;
import org.jboss.weld.junit5.EnableWeld;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldSetup;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@EnableWeld
class CurrencyTransferTableTest {

    @RegisterExtension
    static DbExtension dbExtension = new DbExtension(DbTestData.getInMemDbProps(), "db/currency_transfer_data.sql", "db/schema.sql");

    @Inject
    CurrencyTransferTable table;
    CurrencyTransferTestData td;

    Comparator<CurrencyTransfer> currencyTransferComparator = Comparator
        .comparing(CurrencyTransfer::getDbId)
        .thenComparing(CurrencyTransfer::getId).reversed();

    private Blockchain blockchain = mock(BlockchainImpl.class);
    private BlockchainConfig blockchainConfig = mock(BlockchainConfig.class);
    private BlockchainProcessor blockchainProcessor = mock(BlockchainProcessor.class);

    @WeldSetup
    public WeldInitiator weld = WeldInitiator.from(
        PropertiesHolder.class, CurrencyTransferTable.class
    )
        .addBeans(MockBean.of(dbExtension.getDatabaseManager(), DatabaseManager.class))
        .addBeans(MockBean.of(dbExtension.getDatabaseManager().getJdbi(), Jdbi.class))
        .addBeans(MockBean.of(blockchainConfig, BlockchainConfig.class))
        .addBeans(MockBean.of(blockchain, Blockchain.class, BlockchainImpl.class))
        .addBeans(MockBean.of(blockchainProcessor, BlockchainProcessor.class, BlockchainProcessorImpl.class))
        .addBeans(MockBean.of(mock(FullTextConfig.class), FullTextConfig.class, FullTextConfigImpl.class))
        .addBeans(MockBean.of(mock(DerivedTablesRegistry.class), DerivedTablesRegistry.class, DerivedDbTablesRegistryImpl.class))
        .build();

    @BeforeEach
    void setUp() {
        td = new CurrencyTransferTestData();
    }

    @Test
    void testLoad() {
        CurrencyTransfer transfer = table.get(table.getDbKeyFactory().newKey(td.TRANSFER_0));
        assertNotNull(transfer);
        assertEquals(td.TRANSFER_0, transfer);
    }

    @Test
    void testLoad_returnNull_ifNotExist() {
        CurrencyTransfer transfer = table.get(table.getDbKeyFactory().newKey(td.TRANSFER_NEW));
        assertNull(transfer);
    }

    @Test
    void testSave_insert_new_entity() {//SQL MERGE -> INSERT
        CurrencyTransfer previous = table.get(table.getDbKeyFactory().newKey(td.TRANSFER_NEW));
        assertNull(previous);

        DbUtils.inTransaction(dbExtension, (con) -> table.insert(td.TRANSFER_NEW));
        CurrencyTransfer actual = table.get(table.getDbKeyFactory().newKey(td.TRANSFER_NEW));

        assertNotNull(actual);
        assertTrue(actual.getDbId() != 0);
        assertEquals(td.TRANSFER_NEW.getCurrencyId(), actual.getCurrencyId());
        assertEquals(td.TRANSFER_NEW.getRecipientId(), actual.getRecipientId());
    }

    @Test
    void testSave_update_existing_entity() {//SQL MERGE -> UPDATE
        CurrencyTransfer previous = table.get(table.getDbKeyFactory().newKey(td.TRANSFER_1));
        assertNotNull(previous);

        assertThrows(RuntimeException.class, () -> // not permitted by DB constraints
            DbUtils.inTransaction(dbExtension, (con) -> table.insert(previous))
        );
    }

    @Test
    void getAccountCurrencyTransfers() {
        DbIterator<CurrencyTransfer> dbResult = table.getAccountCurrencyTransfers(-7396849795322372927L, 0, 10);
        List<CurrencyTransfer> result = CollectionUtil.toList(dbResult);
        assertEquals(List.of(td.TRANSFER_2, td.TRANSFER_0), result);
    }

    @Test
    void getAccountCurrencyTransfers_2() {
        DbIterator<CurrencyTransfer> dbResult = table.getAccountCurrencyTransfers(
            -208393164898941117L, -5453448652141572559L, 0, 10);
        List<CurrencyTransfer> result = CollectionUtil.toList(dbResult);
        assertEquals(List.of(td.TRANSFER_1), result);
    }

}
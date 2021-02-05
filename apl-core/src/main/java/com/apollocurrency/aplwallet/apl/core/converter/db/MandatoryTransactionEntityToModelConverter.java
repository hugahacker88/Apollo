/*
 * Copyright (c)  2020-2021. Apollo Foundation.
 */

package com.apollocurrency.aplwallet.apl.core.converter.db;

import com.apollocurrency.aplwallet.apl.core.blockchain.MandatoryTransaction;
import com.apollocurrency.aplwallet.apl.core.entity.blockchain.MandatoryTransactionEntity;
import com.apollocurrency.aplwallet.apl.core.blockchain.Transaction;
import com.apollocurrency.aplwallet.apl.core.blockchain.TransactionBuilderFactory;
import com.apollocurrency.aplwallet.apl.util.api.converter.Converter;
import com.apollocurrency.aplwallet.apl.util.exception.AplException;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author andrew.zinchenko@gmail.com
 */
@Singleton
public class MandatoryTransactionEntityToModelConverter implements Converter<MandatoryTransactionEntity, MandatoryTransaction> {
    private final TransactionBuilderFactory transactionBuilderFactory;

    @Inject
    public MandatoryTransactionEntityToModelConverter(TransactionBuilderFactory transactionBuilderFactory) {
        this.transactionBuilderFactory = transactionBuilderFactory;
    }

    @Override
    public MandatoryTransaction apply(MandatoryTransactionEntity entity) {
        try {
            Transaction tx = transactionBuilderFactory.newTransaction(entity.getTransactionBytes());
            MandatoryTransaction model = new MandatoryTransaction(tx, entity.getRequiredTxHash());
            return model;
        } catch (AplException.NotValidException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
}

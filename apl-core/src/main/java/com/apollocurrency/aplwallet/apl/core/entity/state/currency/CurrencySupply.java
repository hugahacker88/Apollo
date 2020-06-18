/*
 * Copyright © 2018-2020 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.entity.state.currency;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.apollocurrency.aplwallet.apl.core.dao.state.currency.CurrencySupplyTable;
import com.apollocurrency.aplwallet.apl.core.db.DbKey;
import com.apollocurrency.aplwallet.apl.core.db.model.VersionedDeletableEntity;
import com.apollocurrency.aplwallet.apl.core.db.model.VersionedDerivedEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class CurrencySupply extends VersionedDeletableEntity {

    private long currencyId;
    private long currentSupply;
    private long currentReservePerUnitATM;

    public CurrencySupply(Currency currency, int height) {
        super(null, height);
        this.currencyId = currency.getCurrencyId();
        super.setDbKey(CurrencySupplyTable.currencySupplyDbKeyFactory.newKey(this.currencyId));
    }

    /**
     * unit test
     */
    public CurrencySupply(long currencyId, long currentSupply, long currentReservePerUnitATM, int height,
                          boolean latest, boolean deleted) {
        super(null, height);
        this.currencyId = currencyId;
        this.currentSupply = currentSupply;
        this.currentReservePerUnitATM = currentReservePerUnitATM;
        this.setLatest(latest);
        this.setLatest(latest);
    }

    public CurrencySupply(ResultSet rs, DbKey dbKey) throws SQLException {
        super(rs);
        this.currencyId = rs.getLong("id");
        super.setDbKey(dbKey);
        this.currentSupply = rs.getLong("current_supply");
        this.currentReservePerUnitATM =
            rs.getLong("current_reserve_per_unit_atm");
    }
}

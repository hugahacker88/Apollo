package com.apollocurrency.aplwallet.apl.core.rest.converter;

import com.apollocurrency.aplwallet.api.dto.Account2FADTO;
import com.apollocurrency.aplwallet.apl.core.app.Convert2;
import com.apollocurrency.aplwallet.apl.core.http.TwoFactorAuthParameters;

public class Account2FAConverter implements Converter<TwoFactorAuthParameters, Account2FADTO> {
    @Override
    public Account2FADTO apply(TwoFactorAuthParameters model) {
        Account2FADTO dto = new Account2FADTO();
        dto.setStatus(model.getStatus2FA());
        dto.setAccount(Long.toUnsignedString(model.getAccountId()));
        dto.setAccountRS(Convert2.rsAccount(model.getAccountId()));
        return dto;
    }
}

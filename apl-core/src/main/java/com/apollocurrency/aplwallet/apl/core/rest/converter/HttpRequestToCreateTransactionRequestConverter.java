/*
 * Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.rest.converter;

import com.apollocurrency.aplwallet.apl.core.account.model.Account;
import com.apollocurrency.aplwallet.apl.core.account.service.AccountService;
import com.apollocurrency.aplwallet.apl.core.http.ParameterException;
import com.apollocurrency.aplwallet.apl.core.http.HttpParameterParserUtil;
import com.apollocurrency.aplwallet.apl.core.model.CreateTransactionRequest;
import com.apollocurrency.aplwallet.apl.core.transaction.messages.Attachment;
import com.apollocurrency.aplwallet.apl.crypto.Convert;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestToCreateTransactionRequestConverter {


    public static CreateTransactionRequest convert(HttpServletRequest req, Account senderAccount, long recipientId, long amountATM,
                                                   Attachment attachment, boolean broadcast,
                                                   AccountService accountService) throws ParameterException {
        String passphrase = Convert.emptyToNull(HttpParameterParserUtil.getPassphrase(req, false));
        String secretPhrase = HttpParameterParserUtil.getSecretPhrase(req, false);
        Boolean encryptedMessageIsPrunable = Boolean.valueOf(req.getParameter("encryptedMessageIsPrunable"));
        Boolean messageIsPrunable = Boolean.valueOf(req.getParameter("messageIsPrunable"));
        Boolean isPhased = Boolean.valueOf(req.getParameter("phased"));
        Account recipient = null;

        if (recipientId != 0) {
            recipient = accountService.getAccount(recipientId);
        }

        CreateTransactionRequest createTransactionRequest = CreateTransactionRequest.builder()
                .broadcast(!"false".equalsIgnoreCase(req.getParameter("broadcast")) && broadcast && (secretPhrase != null || passphrase != null))
                .secretPhrase(secretPhrase)
                .passphrase(passphrase)
                .deadlineValue(req.getParameter("deadline"))
                .referencedTransactionFullHash(Convert.emptyToNull(req.getParameter("referencedTransactionFullHash")))
                .publicKeyValue(Convert.emptyToNull(req.getParameter("publicKey")))
                .publicKey(HttpParameterParserUtil.getPublicKey(req, senderAccount.getId()))
                .keySeed(HttpParameterParserUtil.getKeySeed(req, senderAccount.getId(), false))

                .amountATM(amountATM)
                .feeATM(HttpParameterParserUtil.getFeeATM(req))
                .senderAccount(senderAccount)
                .recipientId(recipientId)
                .recipientPublicKey(Convert.emptyToNull(req.getParameter("recipientPublicKey")))
                .phased(isPhased)
                .phasing(isPhased ? HttpParameterParserUtil.parsePhasing(req) : null)

                .attachment(attachment)
                .encryptToSelfMessage(HttpParameterParserUtil.getEncryptToSelfMessage(req, senderAccount.getId()))
                .encryptedMessageIsPrunable(encryptedMessageIsPrunable)
                .appendix(HttpParameterParserUtil.getEncryptedMessage(req, recipient, senderAccount.getId(), encryptedMessageIsPrunable))
                .messageIsPrunable(messageIsPrunable)
                .message(HttpParameterParserUtil.getPlainMessage(req, messageIsPrunable))

                .ecBlockHeight(HttpParameterParserUtil.getInt(req, "ecBlockHeight", 0, Integer.MAX_VALUE, false))
                .ecBlockId(Convert.parseUnsignedLong(req.getParameter("ecBlockId")))

                .build();

        return createTransactionRequest;
    }


}

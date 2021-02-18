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

package com.apollocurrency.aplwallet.apl.util.exception;

import com.apollocurrency.aplwallet.apl.util.JSON;
import org.json.simple.JSONStreamAware;

import java.io.IOException;

public abstract class AplException extends Exception {

    private JSONStreamAware jsonResponce;

    protected AplException() {
        super();
    }

    protected AplException(String message) {
        super(message);
    }

    protected AplException(JSONStreamAware json) {
        super(JSON.toString(json));
        this.jsonResponce = json;
    }

    protected AplException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AplException(Throwable cause) {
        super(cause);
    }

    public JSONStreamAware getJsonResponce() {
        return jsonResponce;
    }

    public static abstract class ValidationException extends AplException {

        private ValidationException(String message) {
            super(message);
        }

        private ValidationException(JSONStreamAware json) {
            super(json);
        }

        private ValidationException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class SignatureVerificationException extends ValidationException {

        public SignatureVerificationException(String message) {
            super(message);
        }

        public SignatureVerificationException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class NotCurrentlyValidException extends ValidationException {

        public NotCurrentlyValidException(String message) {
            super(message);
        }

        public NotCurrentlyValidException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class ExistingTransactionException extends NotCurrentlyValidException {

        public ExistingTransactionException(String message) {
            super(message);
        }

    }

    public static final class NotYetEnabledException extends NotCurrentlyValidException {

        public NotYetEnabledException(String message) {
            super(message);
        }

        public NotYetEnabledException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }

    public static final class NotValidException extends ValidationException {

        public NotValidException(String message) {
            super(message);
        }

        public NotValidException(JSONStreamAware json) {
            super(json);
        }

        public NotValidException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class AccountControlException extends NotCurrentlyValidException {

        public AccountControlException(String message) {
            super(message);
        }

        public AccountControlException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static class InsufficientBalanceException extends NotCurrentlyValidException {

        public InsufficientBalanceException(String message) {
            super(message);
        }

        public InsufficientBalanceException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static final class NotYetEncryptedException extends IllegalStateException {

        public NotYetEncryptedException(String message) {
            super(message);
        }

        public NotYetEncryptedException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static final class StopException extends RuntimeException {

        public StopException(String message) {
            super(message);
        }

        public StopException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static final class AplIOException extends IOException {

        public AplIOException(String message) {
            super(message);
        }

        public AplIOException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    public static final class PrivateTransactionAccessDenied extends RuntimeException {
        public PrivateTransactionAccessDenied() {
            super();
        }

        public PrivateTransactionAccessDenied(String message) {
            super(message);
        }

        public PrivateTransactionAccessDenied(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class PrivateLedgerEntryAccessDenied extends RuntimeException {
        public PrivateLedgerEntryAccessDenied() {
            super();
        }

        public PrivateLedgerEntryAccessDenied(String message) {
            super(message);
        }

        public PrivateLedgerEntryAccessDenied(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class ExecutiveProcessException extends Exception {
        public ExecutiveProcessException() {
            super();
        }

        public ExecutiveProcessException(String message) {
            super(message);
        }

        public ExecutiveProcessException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class ThirdServiceIsNotAvailable extends RuntimeException {
        public ThirdServiceIsNotAvailable() {
            super();
        }

        public ThirdServiceIsNotAvailable(String message) {
            super(message);
        }

        public ThirdServiceIsNotAvailable(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static final class DEXProcessingException extends RuntimeException {
        public DEXProcessingException() {
            super();
        }

        public DEXProcessingException(String message) {
            super(message);
        }

        public DEXProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
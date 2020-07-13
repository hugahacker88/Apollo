package com.apollocurrency.aplwallet.apl.core.transaction;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TransactionTypes {
    public static final byte TYPE_PAYMENT = 0;
    public static final byte TYPE_MESSAGING = 1;
    public static final byte TYPE_COLORED_COINS = 2;
    public static final byte TYPE_DIGITAL_GOODS = 3;
    public static final byte TYPE_ACCOUNT_CONTROL = 4;
    public static final byte TYPE_MONETARY_SYSTEM = 5;
    public static final byte TYPE_DATA = 6;
    public static final byte TYPE_SHUFFLING = 7;
    public static final byte TYPE_UPDATE = 8;
    public static final byte TYPE_DEX = 9;

    public static final byte SUBTYPE_PAYMENT_ORDINARY_PAYMENT = 0;
    public static final byte SUBTYPE_PAYMENT_PRIVATE_PAYMENT = 1;

    public static final byte SUBTYPE_MESSAGING_ARBITRARY_MESSAGE = 0;
    public static final byte SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT = 1;
    public static final byte SUBTYPE_MESSAGING_POLL_CREATION = 2;
    public static final byte SUBTYPE_MESSAGING_VOTE_CASTING = 3;
    public static final byte SUBTYPE_MESSAGING_HUB_ANNOUNCEMENT = 4;
    public static final byte SUBTYPE_MESSAGING_ACCOUNT_INFO = 5;
    public static final byte SUBTYPE_MESSAGING_ALIAS_SELL = 6;
    public static final byte SUBTYPE_MESSAGING_ALIAS_BUY = 7;
    public static final byte SUBTYPE_MESSAGING_ALIAS_DELETE = 8;
    public static final byte SUBTYPE_MESSAGING_PHASING_VOTE_CASTING = 9;
    public static final byte SUBTYPE_MESSAGING_ACCOUNT_PROPERTY = 10;
    public static final byte SUBTYPE_MESSAGING_ACCOUNT_PROPERTY_DELETE = 11;

    public static final byte SUBTYPE_COLORED_COINS_ASSET_ISSUANCE = 0;
    public static final byte SUBTYPE_COLORED_COINS_ASSET_TRANSFER = 1;
    public static final byte SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT = 2;
    public static final byte SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT = 3;
    public static final byte SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION = 4;
    public static final byte SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION = 5;
    public static final byte SUBTYPE_COLORED_COINS_DIVIDEND_PAYMENT = 6;
    public static final byte SUBTYPE_COLORED_COINS_ASSET_DELETE = 7;

    public static final byte SUBTYPE_DIGITAL_GOODS_LISTING = 0;
    public static final byte SUBTYPE_DIGITAL_GOODS_DELISTING = 1;
    public static final byte SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE = 2;
    public static final byte SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE = 3;
    public static final byte SUBTYPE_DIGITAL_GOODS_PURCHASE = 4;
    public static final byte SUBTYPE_DIGITAL_GOODS_DELIVERY = 5;
    public static final byte SUBTYPE_DIGITAL_GOODS_FEEDBACK = 6;
    public static final byte SUBTYPE_DIGITAL_GOODS_REFUND = 7;

    public static final byte SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING = 0;
    public static final byte SUBTYPE_ACCOUNT_CONTROL_PHASING_ONLY = 1;

    public static final byte SUBTYPE_DATA_TAGGED_DATA_UPLOAD = 0;
    public static final byte SUBTYPE_DATA_TAGGED_DATA_EXTEND = 1;

    public static final byte SUBTYPE_UPDATE_CRITICAL = 0;
    public static final byte SUBTYPE_UPDATE_IMPORTANT = 1;
    public static final byte SUBTYPE_UPDATE_MINOR = 2;
    public static final byte SUBTYPE_UPDATE_V2 = 3;

    public static final byte SUBTYPE_DEX_ORDER = 0;
    public static final byte SUBTYPE_DEX_ORDER_CANCEL = 1;
    public static final byte SUBTYPE_DEX_CONTRACT = 2;
    public static final byte SUBTYPE_DEX_TRANSFER_MONEY = 3;
    public static final byte SUBTYPE_DEX_CLOSE_ORDER = 4;

    public static final byte SUBTYPE_SHUFFLING_CREATION = 0;
    public static final byte SUBTYPE_SHUFFLING_REGISTRATION = 1;
    public static final byte SUBTYPE_SHUFFLING_PROCESSING = 2;
    public static final byte SUBTYPE_SHUFFLING_RECIPIENTS = 3;
    public static final byte SUBTYPE_SHUFFLING_VERIFICATION = 4;
    public static final byte SUBTYPE_SHUFFLING_CANCELLATION = 5;

    public static final byte SUBTYPE_MONETARY_SYSTEM_CURRENCY_ISSUANCE = 0;
    public static final byte SUBTYPE_MONETARY_SYSTEM_RESERVE_INCREASE = 1;
    public static final byte SUBTYPE_MONETARY_SYSTEM_RESERVE_CLAIM = 2;
    public static final byte SUBTYPE_MONETARY_SYSTEM_CURRENCY_TRANSFER = 3;
    public static final byte SUBTYPE_MONETARY_SYSTEM_PUBLISH_EXCHANGE_OFFER = 4;
    public static final byte SUBTYPE_MONETARY_SYSTEM_EXCHANGE_BUY = 5;
    public static final byte SUBTYPE_MONETARY_SYSTEM_EXCHANGE_SELL = 6;
    public static final byte SUBTYPE_MONETARY_SYSTEM_CURRENCY_MINTING = 7;
    public static final byte SUBTYPE_MONETARY_SYSTEM_CURRENCY_DELETION = 8;

    private static final Map<Integer, TransactionTypeSpec> ALL_TYPES = new HashMap<>();

    @Getter
    public enum TransactionTypeSpec {
        ORDINARY_PAYMENT(TYPE_PAYMENT, SUBTYPE_PAYMENT_ORDINARY_PAYMENT),
        PRIVATE_PAYMENT(TYPE_PAYMENT, SUBTYPE_PAYMENT_PRIVATE_PAYMENT),

        ARBITRARY_MESSAGE(TYPE_MESSAGING, SUBTYPE_MESSAGING_ARBITRARY_MESSAGE),
        ALIAS_ASSIGNMENT(TYPE_MESSAGING, SUBTYPE_MESSAGING_ALIAS_ASSIGNMENT),
        POLL_CREATION(TYPE_MESSAGING, SUBTYPE_MESSAGING_POLL_CREATION),
        VOTE_CASTING(TYPE_MESSAGING, SUBTYPE_MESSAGING_VOTE_CASTING),
        ACCOUNT_INFO(TYPE_MESSAGING, SUBTYPE_MESSAGING_ACCOUNT_INFO),
        ALIAS_SELL(TYPE_MESSAGING, SUBTYPE_MESSAGING_ALIAS_SELL),
        ALIAS_BUY(TYPE_MESSAGING, SUBTYPE_MESSAGING_ALIAS_BUY),
        ALIAS_DELETE(TYPE_MESSAGING, SUBTYPE_MESSAGING_ALIAS_DELETE),
        PHASING_VOTE_CASTING(TYPE_MESSAGING, SUBTYPE_MESSAGING_PHASING_VOTE_CASTING),
        ACCOUNT_PROPERTY(TYPE_MESSAGING, SUBTYPE_MESSAGING_ACCOUNT_PROPERTY),
        ACCOUNT_PROPERTY_DELETE(TYPE_MESSAGING, SUBTYPE_MESSAGING_ACCOUNT_PROPERTY_DELETE),

        EFFECTIVE_BALANCE_LEASING(TYPE_ACCOUNT_CONTROL, SUBTYPE_ACCOUNT_CONTROL_EFFECTIVE_BALANCE_LEASING),
        SET_PHASING_ONLY(TYPE_ACCOUNT_CONTROL, SUBTYPE_ACCOUNT_CONTROL_PHASING_ONLY),

        TAGGED_DATA_UPLOAD(TYPE_DATA, SUBTYPE_DATA_TAGGED_DATA_UPLOAD),
        TAGGED_DATA_EXTEND(TYPE_DATA, SUBTYPE_DATA_TAGGED_DATA_EXTEND),

        SHUFFLING_CREATION(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_CREATION),
        SHUFFLING_REGISTRATION(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_REGISTRATION),
        SHUFFLING_PROCESSING(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_PROCESSING),
        SHUFFLING_RECIPIENTS(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_RECIPIENTS),
        SHUFFLING_VERIFICATION(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_VERIFICATION),
        SHUFFLING_CANCELLATION(TYPE_SHUFFLING, SUBTYPE_SHUFFLING_CANCELLATION),

        MS_CURRENCY_ISSUANCE(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_CURRENCY_ISSUANCE),
        MS_RESERVE_INCREASE(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_RESERVE_INCREASE),
        MS_RESERVE_CLAIM(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_RESERVE_CLAIM),
        MS_CURRENCY_TRANSFER(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_CURRENCY_TRANSFER),
        MS_PUBLISH_EXCHANGE_OFFER(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_PUBLISH_EXCHANGE_OFFER),
        MS_EXCHANGE_BUY(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_EXCHANGE_BUY),
        MS_EXCHANGE_SELL(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_EXCHANGE_SELL),
        MS_CURRENCY_MINTING(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_CURRENCY_MINTING),
        MS_CURRENCY_DELETION(TYPE_MONETARY_SYSTEM, SUBTYPE_MONETARY_SYSTEM_CURRENCY_DELETION),

        CC_ASSET_ISSUANCE(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_ASSET_ISSUANCE),
        CC_ASSET_TRANSFER(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_ASSET_TRANSFER),
        CC_ASK_ORDER_PLACEMENT(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_ASK_ORDER_PLACEMENT),
        CC_BID_ORDER_PLACEMENT(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_BID_ORDER_PLACEMENT),
        CC_ASK_ORDER_CANCELLATION(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_ASK_ORDER_CANCELLATION),
        CC_BID_ORDER_CANCELLATION(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_BID_ORDER_CANCELLATION),
        CC_DIVIDEND_PAYMENT(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_DIVIDEND_PAYMENT),
        CC_ASSET_DELETE(TYPE_COLORED_COINS, SUBTYPE_COLORED_COINS_ASSET_DELETE),

        DGS_LISTING(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_LISTING),
        DGS_DELISTING(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_DELISTING),
        DGS_CHANGE_PRICE(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_PRICE_CHANGE),
        DGS_CHANGE_QUANTITY(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_QUANTITY_CHANGE),
        DGS_PURCHASE(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_PURCHASE),
        DGS_DELIVERY(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_DELIVERY),
        DGS_FEEDBACK(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_FEEDBACK),
        DGS_REFUND(TYPE_DIGITAL_GOODS, SUBTYPE_DIGITAL_GOODS_REFUND),

        CRITICAL_UPDATE(TYPE_UPDATE, SUBTYPE_UPDATE_CRITICAL),
        IMPORTANT_UPDATE(TYPE_UPDATE, SUBTYPE_UPDATE_IMPORTANT),
        MINOR_UPDATE(TYPE_UPDATE, SUBTYPE_UPDATE_MINOR),
        UPDATE_V2(TYPE_UPDATE, SUBTYPE_UPDATE_V2),

        DEX_ORDER(TYPE_DEX, SUBTYPE_DEX_ORDER),
        DEX_CANCEL_ORDER(TYPE_DEX, SUBTYPE_DEX_ORDER_CANCEL),
        DEX_CONTRACT(TYPE_DEX, SUBTYPE_DEX_CONTRACT),
        DEX_TRANSFER_MONEY(TYPE_DEX, SUBTYPE_DEX_TRANSFER_MONEY),
        DEX_CLOSE_ORDER(TYPE_DEX, SUBTYPE_DEX_CLOSE_ORDER),

        ;

        private final byte type;
        private final byte subtype;

        TransactionTypeSpec(int type, int subtype) {
            this.type = (byte) type;
            this.subtype = (byte) subtype;
            ALL_TYPES.put(subtype | type << 8, this);
        }
    }
    public static TransactionTypeSpec findValue(int type, int subtype) {
        TransactionTypeSpec spec = ALL_TYPES.get(subtype | type << 8);
        if (spec == null) {
            throw new IllegalArgumentException("Unable to find spec for type '" + type + "' and subtype '" + subtype + "'");
        }
        return spec;
    }
}

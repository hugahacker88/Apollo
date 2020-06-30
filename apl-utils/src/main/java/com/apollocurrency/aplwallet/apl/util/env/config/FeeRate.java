/*
 * Copyright (c)  2018-2020. Apollo Foundation.
 */

package com.apollocurrency.aplwallet.apl.util.env.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class FeeRate {
    public static final short RATE_DIVIDER = 100;
    public static final short DEFAULT_RATE = 100; // in percent, that equals to multiply by 1
    @Getter
    private final byte type;
    @Getter
    private final byte subType;
    @Getter
    private final short rate;

    public FeeRate(byte type, byte subType) {
        this(type, subType, DEFAULT_RATE);
    }

    @JsonCreator
    public FeeRate(@JsonProperty("type") byte type, @JsonProperty("subType") byte subType, @JsonProperty("rate") short rate) {
        this.type = type;
        this.subType = subType;
        if (rate < 0) {
            throw new IllegalArgumentException("Wrong rate value.");
        }
        this.rate = rate;
    }

    public FeeRate copy() {
        return new FeeRate(type, subType, rate);
    }
}

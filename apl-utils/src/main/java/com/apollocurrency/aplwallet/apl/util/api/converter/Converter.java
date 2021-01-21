/*
 * Copyright (c)  2021-2022 Apollo Foundation.
 */

package com.apollocurrency.aplwallet.apl.util.api.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public interface Converter<S, T> extends Function<S, T> {

    default T convert(S model) {
        T dto = null;
        if (model != null) {
            dto = this.apply(model);
        }
        return dto;
    }

    default List<T> convert(List<S> models) {
        List<T> dtos = new ArrayList<>();
        if (models != null && !models.isEmpty()) {
            dtos = models.stream().map(this).collect(toList());
        }
        return dtos;
    }

}

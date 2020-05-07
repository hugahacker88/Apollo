/*
 * Copyright © 2020 Apollo Foundation
 */
package com.apollocurrency.aplwallet.apl.util.supervisor.msg;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.HashMap;
import java.util.Map;

/**
 * Request (incoming message) in SV channel
 *
 * @author alukin@gmail.com
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SvBusRequest extends SvBusMessage {

    Map<String, String> parameters = new HashMap<>();
}
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
 * Copyright © 2018-2019 Apollo Foundation
 */

package com.apollocurrency.aplwallet.apl.core.http.get;

import com.apollocurrency.aplwallet.apl.core.http.APITag;
import com.apollocurrency.aplwallet.apl.core.http.AbstractAPIRequestHandler;
import com.apollocurrency.aplwallet.apl.core.http.JSONData;
import com.apollocurrency.aplwallet.apl.core.http.ParameterParser;
import com.apollocurrency.aplwallet.apl.core.shuffling.model.ShufflingParticipant;
import com.apollocurrency.aplwallet.apl.core.shuffling.service.ShufflingParticipantService;
import com.apollocurrency.aplwallet.apl.util.AplException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import java.util.List;
import javax.enterprise.inject.Vetoed;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.http.HttpServletRequest;

@Vetoed
public final class GetShufflingParticipants extends AbstractAPIRequestHandler {

    public GetShufflingParticipants() {
        super(new APITag[] {APITag.SHUFFLING}, "shuffling");
    }
    ShufflingParticipantService shufflingParticipantService = CDI.current().select(ShufflingParticipantService.class).get();
    @Override
    public JSONStreamAware processRequest(HttpServletRequest req) throws AplException {
        long shufflingId = ParameterParser.getUnsignedLong(req, "shuffling", true);
        JSONObject response = new JSONObject();
        JSONArray participantsJSONArray = new JSONArray();
        response.put("participants", participantsJSONArray);
        List<ShufflingParticipant> participants = shufflingParticipantService.getParticipants(shufflingId);
        for (ShufflingParticipant participant : participants) {
            participantsJSONArray.add(JSONData.participant(participant));
        }
        return response;
    }

}

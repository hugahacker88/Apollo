/*
 * Copyright © 2020 Apollo Foundation
 */
package com.apollocurrency.apl.id.handler;

import com.apollocurrency.apl.id.cert.CertHelper;
import java.math.BigInteger;

/**
 * Handles operations with X509 certificate and private key of this node 
 * @author alukin@gmail.com
 */
public interface ThisActorIdHandler {
    BigInteger getApolloId();
    CertHelper getCertificate();
    byte[] sign(byte[] message);
    CertHelper generateSelfSignedCert();
}

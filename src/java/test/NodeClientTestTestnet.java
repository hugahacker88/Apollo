package test;


import apl.AccountLedger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static test.TestData.*;
import static test.TestUtil.*;

/**
 * Test scenarios on testnet for {@link NodeClient}
 */
public class NodeClientTestTestnet extends AbstractNodeClientTest {
    public static final String MAIN_RS = "APL-NZKH-MZRE-2CTT-98NPZ";
    //transaction hash from 7446 block 20_000 APL to RS4
    public static final String TRANSACTION_HASH = "0619d7f4e0f8d2dab76f28e320c5ca819b2a08dc2294e53151bf14d318d5cefa";
    public static final Long BLOCK_HEIGHT = 7446L;

    public NodeClientTestTestnet() {
        super(TEST_LOCALHOST, TEST_FILE);
    }

    @Test
    @Override
    public void testGet() throws Exception {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("requestType", "getBlock");
        parameters.put("height", BLOCK_HEIGHT.toString());
        String json = client.getJson(createURI(url), parameters);
        ObjectMapper MAPPER = getMAPPER();
        Block block = MAPPER.readValue(json, Block.class);
        Assert.assertNotNull(block);
        Assert.assertEquals(BLOCK_HEIGHT.longValue(), block.getHeight().longValue());
        Assert.assertEquals(MAIN_RS, block.getGeneratorRS());
        Assert.assertEquals(3, block.getNumberOfTransactions().intValue());
        Assert.assertEquals(nqt(3).longValue(), block.getTotalFeeNQT().longValue());
        Assert.assertEquals(nqt(58_000).longValue(), block.getTotalAmountNQT().longValue());
    }


    @Test
    @Override
    public void testGetBlockchainHeight() throws Exception {
        Long blockchainHeight = client.getBlockchainHeight(url);
        Assert.assertNotNull(blockchainHeight);
        Assert.assertTrue(blockchainHeight >= BLOCK_HEIGHT); //block is already exist
    }

    @Test
    @Override
    public void testGetPeersList() throws Exception {
        List<Peer> peersList = client.getPeersList(url);
        Assert.assertNotNull(peersList);
        Assert.assertFalse(peersList.isEmpty());
        Assert.assertEquals(5, peersList.size());
    }

    @Test
    @Override
    public void testGetBlocksList() throws Exception {
        List<Block> blocksList = client.getBlocksList(url);
        Assert.assertNotNull(blocksList);
        Assert.assertFalse(blocksList.isEmpty());
        Assert.assertEquals(5, blocksList.size());
    }

    @Test
    @Override
    public void testGetBlockTransactionsList() throws Exception {
        List<Transaction> blockTransactionsList = client.getBlockTransactionsList(url, BLOCK_HEIGHT);
        Assert.assertNotNull(blockTransactionsList);
        Assert.assertFalse(blockTransactionsList.isEmpty());
        Assert.assertEquals(3, blockTransactionsList.size());
        blockTransactionsList.forEach(transaction -> {
            Assert.assertEquals(BLOCK_HEIGHT, transaction.getHeight());
            Assert.assertEquals(MAIN_RS, transaction.getSenderRS());
        });
    }

    @Test
    @Override
    public void testGetPeersCount() throws Exception {
        Assert.assertEquals(5, client.getPeersCount(url));
    }


    @Test
    @Override
    public void testGetPeersIPs() throws Exception {
        List<String> peersIPs = client.getPeersIPs(url);
        Assert.assertNotNull(peersIPs);
        Assert.assertFalse(peersIPs.isEmpty());
        peersIPs.forEach(ip -> Assert.assertTrue(IP_PATTERN.matcher(ip).matches()));
        peersIPs.forEach(ip -> {
                    boolean found = false;
                    for (String aip : URLS) {
                        String tmp = aip.substring(aip.indexOf("//") + 2, aip.lastIndexOf(":"));
                        if (tmp.equals(ip)) {
                            found = true;
                        }
                    }
                    if (!found) {
                        Assert.fail("Unknown ip: " + ip);
                    }
                }
        );
    }

    @Test
    @Override
    public void testGetTransaction() throws IOException {
        Transaction transaction = client.getTransaction(url, TRANSACTION_HASH);
        Assert.assertNotNull(transaction);
        Assert.assertEquals(TRANSACTION_HASH, transaction.getFullHash());
        Assert.assertEquals(nqt(1), transaction.getFeeNQT());
        Assert.assertEquals(nqt(20_000), transaction.getAmountNQT());
        Assert.assertEquals(0, transaction.getSubtype().intValue());
        Assert.assertEquals(0, transaction.getType().intValue());
        Assert.assertEquals(MAIN_RS, transaction.getSenderRS());
//        Assert.assertEquals(RS4, transaction.getRecipientRS());
    }

    @Test
    public void testGetAccountLedger() throws Exception {
        String accountRs = getRandomRS(accounts);
        List<LedgerEntry> accountLedger = client.getAccountLedger(url, accountRs, true);
        Assert.assertNotNull(accountLedger);
        Assert.assertFalse(accountLedger.isEmpty());
        accountLedger.forEach(entry -> {
            Transaction transaction = entry.getTransaction();
            if (transaction != null && !transaction.getSenderRS().equalsIgnoreCase(accountRs) && !transaction.getRecipientRS().equalsIgnoreCase(accountRs)) {
                Assert.fail("Not this user ledger!");
            }
        });
    }

    @Test
    public void testGetPrivateAccountLedger() throws Exception {
        String accountRs = getRandomRS(accounts);
        List<LedgerEntry> accountLedger = client.getPrivateAccountLedger(url, accounts.get(accountRs), true);
        Assert.assertNotNull(accountLedger);
        Assert.assertFalse(accountLedger.isEmpty());
        accountLedger.forEach(entry -> {
            Transaction transaction = entry.getTransaction();
            if (transaction != null && !accountRs.equalsIgnoreCase(transaction.getSenderRS()) && !accountRs.equalsIgnoreCase(transaction.getRecipientRS())) {
                Assert.fail("Not this user ledger!");
            }
        });
        List<LedgerEntry> publicLedger = client.getAccountLedger(url, accountRs, true);
        publicLedger.forEach(accountLedger::remove);
        accountLedger.forEach(entry -> {
            if (!entry.getEventType().equals(AccountLedger.LedgerEvent.PRIVATE_PAYMENT)
                    && !entry.getEventType().equals(AccountLedger.LedgerEvent.TRANSACTION_FEE)) {
                Assert.fail("Not only private payment and theirs fee are present in accountLedger. Fail for entry: " + entry);
            }
        });
    }

    @Test
    public void testGetPrivateTransaction() {

//        client.getPrivateTransaction(url, )
    }

    @Test
    public void testGetPrivateBlockTransactions() {

    }

    @Test
    public void testGetBlockWithPrivateTransactions() {

    }

    @Test
    public void testGetBlocksWithPrivateTransactions() {

    }
}




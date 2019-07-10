package com.apollocurrency.aplwallet.apl.exchange.service;

import com.apollocurrency.aplwallet.apl.core.app.KeyStoreService;
import com.apollocurrency.aplwallet.apl.core.model.WalletKeysInfo;
import com.apollocurrency.aplwallet.apl.eth.contracts.DexContract;
import com.apollocurrency.aplwallet.apl.eth.contracts.DexContractImpl;
import com.apollocurrency.aplwallet.apl.eth.model.EthWalletKey;
import com.apollocurrency.aplwallet.apl.eth.service.EthereumWalletService;
import com.apollocurrency.aplwallet.apl.exchange.model.DexCurrencies;
import com.apollocurrency.aplwallet.apl.util.AplException;
import com.apollocurrency.aplwallet.apl.util.Constants;
import com.apollocurrency.aplwallet.apl.util.injectable.PropertiesHolder;
import org.ethereum.util.blockchain.EtherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

@Singleton
public class DexSmartContractService {
    private static final Logger LOG = LoggerFactory.getLogger(DexSmartContractService.class);

    private Web3j web3j;
    private String smartContractAddress;
    private String paxContractAddress;
    private KeyStoreService keyStoreService;
    private DexEthService dexEthService;
    private EthereumWalletService ethereumWalletService;

    @Inject
    public DexSmartContractService(Web3j web3j, PropertiesHolder propertiesHolder, KeyStoreService keyStoreService, DexEthService dexEthService,
                                   EthereumWalletService ethereumWalletService) {
        this.web3j = web3j;
        this.keyStoreService = keyStoreService;
        smartContractAddress = propertiesHolder.getStringProperty("apl.eth.swap.contract.address");
        paxContractAddress = propertiesHolder.getStringProperty("apl.eth.pax.contract.address");
        this.dexEthService = dexEthService;
        this.ethereumWalletService = ethereumWalletService;
    }

    /**
     *  Deposit(freeze) money(eth or pax) on the contract.
     * @param currency Eth or Pax
     * @return String transaction hash.
     */
    public String deposit(String passphrase, Long offerId, long accountId, String fromAddress, BigInteger weiValue, Long gas, DexCurrencies currency) throws ExecutionException, AplException.ExecutiveProcessException {
        WalletKeysInfo keyStore = keyStoreService.getWalletKeysInfo(passphrase, accountId);
        if(keyStore==null){
            throw new AplException.ExecutiveProcessException("User wallet wasn't found.");
        }
        EthWalletKey ethWalletKey = keyStore.getEthWalletForAddress(fromAddress);
        Long gasPrice = gas;

        if(gasPrice == null){
            gasPrice = dexEthService.getEthPriceInfo().getFastSpeedPrice();
        }

       if(gasPrice == null){
           throw new AplException.ThirdServiceIsNotAvailable("Eth Price Info is not available.");
       }
        if(!currency.isEthOrPax()){
            throw new UnsupportedOperationException("This function not supported this currency " + currency.name());
        }

        if(currency.isPax()){
            ethereumWalletService.sendApproveTransaction(ethWalletKey, smartContractAddress, weiValue);
        }

        return deposit(ethWalletKey.getCredentials(), offerId, weiValue, gasPrice, currency.isEth() ? null : paxContractAddress);
    }


        /**
         *  Withdraw money(eth or pax) from the contract.
         * @param currency Eth or Pax
         * @return String transaction hash.
         */
    public String withdraw(String passphrase, long accountId, String fromAddress,  BigInteger orderId, Long gas, DexCurrencies currency) throws AplException.ExecutiveProcessException {
        WalletKeysInfo keyStore = keyStoreService.getWalletKeysInfo(passphrase, accountId);

        if(keyStore==null){
            throw new AplException.ExecutiveProcessException("User wallet wasn't found.");
        }

        EthWalletKey ethWalletKey = keyStore.getEthWalletForAddress(fromAddress);

        if(!currency.isEthOrPax()){
            throw new UnsupportedOperationException("This function not supported this currency " + currency.name());
        }
        Long gasPrice = gas;

        if(gasPrice == null){
            try {
                gasPrice = dexEthService.getEthPriceInfo().getAverageSpeedPrice();
            } catch (ExecutionException e) {
                throw new AplException.ExecutiveProcessException("Third service is not available, try later.");
            }
        }

        return withdraw(ethWalletKey.getCredentials(), orderId, gasPrice, currency.isEth() ? null : paxContractAddress);
    }

    /**
     *  Deposit some eth/erc20.
     * @param orderId  is Long but then will use as unsign value.
     * @param token
     * @return link on tx.
     */
    private String deposit(Credentials credentials, Long orderId, BigInteger weiValue, Long gasPrice, String token){
        BigInteger orderIdUnsign = new BigInteger(Long.toUnsignedString(orderId));
        ContractGasProvider contractGasProvider = new StaticGasProvider(EtherUtil.convert(gasPrice, EtherUtil.Unit.GWEI), Constants.GAS_LIMIT_FOR_ERC20);
        DexContract  dexContract = new DexContractImpl(smartContractAddress, web3j, credentials, contractGasProvider);
        TransactionReceipt transactionReceipt = null;
        try {
            if(token==null) {
                transactionReceipt = dexContract.deposit(orderIdUnsign, weiValue).sendAsync().get();
            } else {
                transactionReceipt = dexContract.deposit(orderIdUnsign, weiValue, token).sendAsync().get();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return transactionReceipt != null ? transactionReceipt.getTransactionHash() : null;
    }

    /**
     *  Deposit some eth/erc20.
     * @param orderId  is Long but then will use as unsign value.
     * @param token
     * @return link on tx.
     */
    private String depositAndInitiate(Credentials credentials, BigInteger orderId, BigInteger weiValue, byte[] secretHash, String recipient, BigInteger refundTimestamp, Long gasPrice,  String token){
        ContractGasProvider contractGasProvider = new StaticGasProvider(EtherUtil.convert(gasPrice, EtherUtil.Unit.GWEI), Constants.GAS_LIMIT_FOR_ERC20);
        DexContract  dexContract = new DexContractImpl(smartContractAddress, web3j, credentials, contractGasProvider);
        TransactionReceipt transactionReceipt = null;
        try {
            if(token==null) {
                transactionReceipt = dexContract.depositAndInitiate(orderId, secretHash, recipient, refundTimestamp, weiValue).sendAsync().get();
            } else {
                transactionReceipt = dexContract.depositAndInitiate(orderId, weiValue, token, secretHash, recipient, refundTimestamp).sendAsync().get();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return transactionReceipt != null ? transactionReceipt.getTransactionHash() : null;
    }

    private String withdraw(Credentials credentials, BigInteger orderId, Long gasPrice, String token){
        ContractGasProvider contractGasProvider = new StaticGasProvider(EtherUtil.convert(gasPrice, EtherUtil.Unit.GWEI), Constants.GAS_LIMIT_FOR_ERC20);
        DexContract  dexContract = new DexContractImpl(smartContractAddress, web3j, credentials, contractGasProvider);
        TransactionReceipt transactionReceipt = null;
        try {
            if(token==null) {
                transactionReceipt = dexContract.withdraw(orderId).sendAsync().get();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return transactionReceipt != null ? transactionReceipt.getTransactionHash() : null;
    }

    /**
     *  Initiate atomic swap.
     * @return link on tx.
     */
    private String initiate(Credentials credentials, BigInteger orderId, byte[] secretHash, String recipient, BigInteger refundTimestamp, Long gasPrice){
        ContractGasProvider contractGasProvider = new StaticGasProvider(EtherUtil.convert(gasPrice, EtherUtil.Unit.GWEI), Constants.GAS_LIMIT_FOR_ERC20);
        DexContract  dexContract = new DexContractImpl(smartContractAddress, web3j, credentials, contractGasProvider);
        TransactionReceipt transactionReceipt = null;
        try {
            transactionReceipt = dexContract.initiate(orderId, secretHash, recipient, refundTimestamp).sendAsync().get();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return transactionReceipt != null ? transactionReceipt.getTransactionHash() : null;
    }

    private BigInteger getDepositedOrderDetails(String address, BigInteger orderId){
        TransactionManager transactionManager = new ClientTransactionManager(web3j, address);
        DexContract  dexContract = new DexContractImpl(smartContractAddress, web3j, transactionManager, null);
        try {
            dexContract.getDepositedOrderDetails(orderId, address).sendAsync().get();
            //TODO Process it
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }



}

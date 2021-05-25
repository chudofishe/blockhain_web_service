package com.diplom.services;

import com.bigchaindb.builders.BigchainDbTransactionBuilder;
import com.bigchaindb.constants.Operations;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;
import com.diplom.dto.AssetForm;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TransactionService {

    @Autowired
    ConnectionService connectionService;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    EncryptionService encryptionService;

    public String sendAsset (AssetForm form, boolean create, String txId) {

        connectionService.setConfig();

        PublicKey publicKey;
        PrivateKey privateKey;

        try{
            publicKey = encryptionService.publicKeyFromString(customUserDetailsService.findByAuthentication().getPubKey());
            privateKey = encryptionService.privateKeyFromString(form.getpKey());
        } catch (InvalidKeySpecException ex) {
            System.out.println("fail");
            return null;
        }



        KeyPair keyPair = new KeyPair(publicKey, privateKey);

        Map<String, String> assetData = Map.of("description", form.getDescription());
        System.out.println("(*) Assets Prepared..");
        MetaData metaData = new MetaData();
        metaData.setMetaData("location", form.getLocation());
        System.out.println("(*) Metadata Prepared..");

        try {
            if (create) {
                return createTransaction(assetData, metaData, keyPair);
            } else {
                return transferTransaction(txId, metaData, keyPair);
            }

        } catch (Exception ex) {
            System.out.println("rip");
        }

        return "transaction failed";
    }

    public void getAsset() {

    }

    private String createTransaction(Map<String, String> assetData, MetaData metaData, KeyPair keys) throws Exception {

        try {
            //build and send CREATE transaction
            Transaction transaction;

            transaction = BigchainDbTransactionBuilder
                    .init()
                    .addAssets(assetData, TreeMap.class)
                    .addMetaData(metaData)
                    .operation(Operations.CREATE)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(connectionService.handleServerResponse());

            System.out.println("(*) CREATE Transaction sent.. - " + transaction.getId());
            return transaction.toString();

        } catch (IOException e) {
            e.printStackTrace();
            //hi
        }

        return "";
    }

    public String transferTransaction(String txId, MetaData metaData, KeyPair keys) throws Exception {

        Map<String, String> assetData = new TreeMap<String, String>();
        assetData.put("id", txId);

        try {


            //which transaction you want to fulfill?
            FulFill fulfill = new FulFill();
            fulfill.setOutputIndex(0);
            fulfill.setTransactionId(txId);


            //build and send TRANSFER transaction
            Transaction transaction = BigchainDbTransactionBuilder
                    .init()
                    .addInput(null, fulfill, (EdDSAPublicKey) keys.getPublic())
                    .addOutput("1", (EdDSAPublicKey) keys.getPublic())
                    .addAssets(txId, String.class)
                    .addMetaData(metaData)
                    .operation(Operations.TRANSFER)
                    .buildAndSign((EdDSAPublicKey) keys.getPublic(), (EdDSAPrivateKey) keys.getPrivate())
                    .sendTransaction(connectionService.handleServerResponse());

            System.out.println("(*) TRANSFER Transaction sent.. - " + transaction.getId());
            return transaction.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }
}

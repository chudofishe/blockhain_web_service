package com.diplom.services;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class EncryptionService {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    public KeyPair buildKeys() {
        net.i2p.crypto.eddsa.KeyPairGenerator edDsaKpg = new net.i2p.crypto.eddsa.KeyPairGenerator();
        KeyPair keyPair = edDsaKpg.generateKeyPair();
        System.out.println("(*) Keys Generated..");
        return keyPair;
    }

    public String publicKeyToString(PublicKey key) {
        byte[] enc_key = key.getEncoded();
        StringBuilder key_builder = new StringBuilder();

        for(byte b : enc_key){
            key_builder.append(String.format("%02x", b));
        }

        return key_builder.toString();
    }

    public String privateKeyToString(PrivateKey key) {
        byte[] enc_key = key.getEncoded();
        StringBuilder key_builder = new StringBuilder();

        for(byte b : enc_key){
            key_builder.append(String.format("%02x", b));
        }

        return key_builder.toString();
    }

    public PublicKey publicKeyFromString(String key) throws InvalidKeySpecException {
        X509EncodedKeySpec encoded = new X509EncodedKeySpec(Utils.hexToBytes(key));
        return new EdDSAPublicKey(encoded);
    }

    public PrivateKey privateKeyFromString(String key) throws InvalidKeySpecException {
        PKCS8EncodedKeySpec encodedP = new PKCS8EncodedKeySpec(Utils.hexToBytes(key));
        return new EdDSAPrivateKey(encodedP);
    }
}

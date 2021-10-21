package io.mosip.kernel.masterdata.test.utils;


import io.mosip.kernel.core.util.CryptoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import tss.tpm.TPMT_PUBLIC;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;


/**
 *
 * @Since 1.1.4
 */
@RunWith(MockitoJUnitRunner.class)
public class MachineUtilTest {

    private static final String ALGORITHM = "RSA";
    private String encodedKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn4A-U6V4SpSeJmjl0xtBDgyFaHn1CvglvnpbczxiDakH6ks8tPvIYT4jDOU-9XaUYKuMFhLxS7G8qwJhv7GKpDQXphSXjgwv_l8A--KV6C1UVaHoAs4XuJPFdXneSd9uMH94GO6lWucyOyfaZLrf5F_--2Rr4ba4rBWw20OrAl1c7FrzjIQjzYXgnBMrvETXptxKKrMELwOOsuyc1Ju4wzPJHYjI0Em4q2BOcQLXqYjhsZhcYeTqBFxXjCOM3WQKLCIsh9RN8Hz-s8yJbQId6MKIS7HQNCTbhbjl1jdfwqRwmBaZz0Gt73I4_8SVCcCQzJWVsakLC1oJAFcmi3l_mQIDAQAB";


    @Test(expected = Exception.class)
    public void testTPMPublicKey() {
        TPMT_PUBLIC tpmPublic = TPMT_PUBLIC.fromTpm(CryptoUtil.decodeBase64(encodedKey));
        Assert.assertNotNull(CryptoUtil.encodeBase64(tpmPublic.toTpm()));
    }

    @Test
    public void testPublicKey() {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(CryptoUtil.decodeBase64(encodedKey));
        PublicKey publicKey = null;
        try {
            KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
            publicKey = kf.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {   }
        Assert.assertNotNull(publicKey);
        Assert.assertNotNull(CryptoUtil.encodeBase64(publicKey.getEncoded()));
    }

}

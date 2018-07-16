
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * RSA加密类
 *
 * @author heiqie
 */
public class RsaEncrypt {

    public static void main(String[] args) {
        try {
            RsaEncrypt encrypt = new RsaEncrypt();
            String encryptText = "just";
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            // 获取公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            System.out.println("publicKey: " + publicKey);
            // 获取私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            System.out.println("privateKey: " + privateKey);

            //获取加密后的数据
            byte[] encryptedDate = encrypt.encrypt(publicKey, encryptText.getBytes());
            //打印加密后base64编码的数据
            System.out.println("encryptedDate:  " + encrypt.bytesToString(encryptedDate));
            //获取解密后的数据
            byte[] decryptedDate = encrypt.decrypt(privateKey, encryptedDate);
            //打印解密后的内容
            System.out.println(new String(decryptedDate));
            //打印解密后base64编码的数据
            System.out.println("decryptedDate:  " + encrypt.bytesToString(decryptedDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * byte数组转为string
     *
     * @return base64 encode 结果
     */
    private String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 加密方法
     *
     * @param publicKey 公钥
     * @param obj       需要加密的内容
     * @return 加密后的数据
     */
    private byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 解密方法
     *
     * @param privateKey 私钥
     * @param obj        需要解密的内容
     * @return 解密后的数据
     */
    private byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
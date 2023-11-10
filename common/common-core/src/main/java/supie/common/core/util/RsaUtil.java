package supie.common.core.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Map;

/**
 * Java RSA 加密工具类。
 *
 * @author rm -rf .bug
 * @date 2020-11-12
 */
@Slf4j
public class RsaUtil {

    /**
     * 密钥长度 于原文长度对应 以及越长速度越慢
     */
    private static final int KEY_SIZE = 1024;
    /**
     * 用于封装随机产生的公钥与私钥
     */
    private static final Map<Integer, String> KEY_MAP = MapUtil.newHashMap();

    /**
     * 随机生成密钥对。
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器
        keyPairGen.initialize(KEY_SIZE, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        // 得到私钥字符串
        String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        // 将公钥和私钥保存到Map
        // 0表示公钥
        KEY_MAP.put(0, publicKeyString);
        // 1表示私钥
        KEY_MAP.put(1, privateKeyString);
    }

    /**
     * RSA公钥加密。
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     */
    public static String encrypt(String str, String publicKey) {
        RSA rsa = new RSA(null, publicKey);
        return Base64.getEncoder().encodeToString(rsa.encrypt(str, KeyType.PublicKey));
    }

    /**
     * RSA私钥解密。
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decrypt(String str, String privateKey) {
        RSA rsa = new RSA(privateKey, null);
        // 64位解码加密后的字符串
        return new String(rsa.decrypt(Base64.getDecoder().decode(str), KeyType.PrivateKey));
    }

    public static void main(String[] args) throws Exception {
//        build();
        keyTest();
    }

    private static void keyTest() {
        String publicA = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCtLEk61jzjB4aH/yY+0WkCmCAPFa0CAX/XP82Knlqw3Cry3gwKIJPLqY8dlD6vRcLt3+nVSlWBOOGGc4TAHyXKO3xi0EMNiZcVDdBK6CyC+QPQ4rT1EBendCUgOWWM3XZ0ibxxpWK00fF1+tu97rw1rf/qy1fvyww9mP+46i8cnQIDAQAB";
        String privateA = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK0sSTrWPOMHhof/Jj7RaQKYIA8VrQIBf9c/zYqeWrDcKvLeDAogk8upjx2UPq9Fwu3f6dVKVYE44YZzhMAfJco7fGLQQw2JlxUN0EroLIL5A9DitPUQF6d0JSA5ZYzddnSJvHGlYrTR8XX6273uvDWt/+rLV+/LDD2Y/7jqLxydAgMBAAECgYBRoXefmY+4ATskblzr7bDPfUBXfANC18wYfkX4mu7rQc2pEIyiVXr13SbObawi2QKrOQqz9UdbE6ITy+5+1e/snW+NV4+6mkrHQIUPFx1FqATWMsRpzAUyOXji4gyOa5Wp8HrcLrsi5/BGUeCKqxPLhLITJVC9zjfnf7HwAlDvrQJBAO/Tfbc1QvYgfjYl67Ts7k0meSN9m3/PYXi1drzra2B2nTXIvPJV9weU3jJpO8Fn5DsytiDy7fjwC72LByCWt2cCQQC42g2iowZIAo4H/8LL0+fb2T9SNVTeqF0QM59JL++3ptaF311pOsB/5W2xmiOc31JyctlmvB+z6nojCm3AeN1bAkEAro2sLrChcCruMdMf36ujNpwOB3rgtwHl28mYBqECok8y1Xapr2hKSEZeAyr8xyQqxbt/PDQUaN+ua+LI6TtJeQJARIxgCBKqbqO75c17gDiZv5ZFfAfY7CTFNg/enrZPD+ynmsdlDXPDFOqQjg7Z6/+IxHZAlm9m1kI6fqoMXxazBwJALUZcb1Fl0CIz2hfTrFqNaSULpcGWCrcMzQILmvpCZm3yJdw0xTE/C8/gEbkdeD4wkTQyiJoTYgm6SjHChagdgw==";

        String text = "RSA加解密测试内容Ab~!@#$";

        System.out.println();
        System.out.println("---------------------------------> 使用公钥A加密，私钥A解密 <---------------------------------");
        String encryptA = encrypt(text, publicA);
        System.out.println("原内容为：" + text);
        System.out.println("加密后的内容为：" + encryptA);
        System.out.println("解密后的内容为：" + decrypt(encryptA, privateA));
        System.out.println();
    }

    private static void build() throws NoSuchAlgorithmException {
        long temp = System.currentTimeMillis();
        // 生成公钥和私钥
        genKeyPair();
        // 加密字符串
        log.info("公钥:" + KEY_MAP.get(0));
        log.info("私钥:" + KEY_MAP.get(1));
        log.info("生成密钥消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
        log.info("生成后的公钥前端使用!");
        log.info("生成后的私钥后台使用!");
        String message = "RSA加解密测试ABCD~!@#$";
        log.info("原文:" + message);
        temp = System.currentTimeMillis();
        String messageEn = encrypt(message, KEY_MAP.get(0));
        log.info("密文:" + messageEn);
        log.info("加密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
        temp = System.currentTimeMillis();
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAImmZGVhXx2x1AKDSQ+/XBeWnAwFqKOGEqR4Waud0ambEbNdNSeYJJDH4ePg5rXi6cMIxrWREwConhczUI4YFufq/cOvrXC1ruUhyoy9j8+YONC2zBjVOXM+CepDSARIRalIImribhnpgbzHUk2NTd0AMD9k0sJkyhmIbQgE2IJZAgMBAAECgYAus3heeRHHRl0S2S4BnMAG9L9OyOC1Ujii+n4Z3rqSm5z+yMHPBCxrvpUiNlywGyOKW0DnuyfCUnMIlg1fmEa0u0GTslMiGFMPTE97YT/pbsbVxCAf/ullQM/MriJfI67GH1ridiQRJM34Eg7ZwRlo0j2dWd8ivpKErTWoKqty/QJBALxEkxMCYj75yejEHapCElcn7AatM6rVJFidyZRvmb4V+bX9UYYlQWZNfMLr8+Jnfj2vaaQ4QBTIpY8Gvd3Q9ocCQQC7K+vX/LFrNx6pNsssjJU8u99M15GCSr8gUA1pDPi6ukk//zL6OwPQ2Vp2jk3JTgmcYKAPU5Fo8M1De/zwohgfAkAEgGM/3hu5Q8G3JhFLg97qZJL5KeUFXalL0SIDwZNcfywEVPVBDtz8dDfadUfUjwLuuKX+/jVSCeYjgFbD2f47AkBYWXAW4MGCgV8osqOO4MElDO1noS2fGddD0mWyG5xgz8P5wGV4tlijwaUw5xUSm5JvzTBga6rspRXO7/zCPVEzAkBTYBlXL2bwMCtH3Mpl0luGRAqshvH2QG7/p7/g2lwyrt9DU917OIxqj22P2IN+G+Pf+LRQupy3wb+7jsDLAF6F";
        log.info("使用的解密私钥为：" + privateKey);
        String messageDe = decrypt(messageEn, privateKey);
        log.info("解密:" + messageDe);
        log.info("解密消耗时间:" + (System.currentTimeMillis() - temp) / 1000.0 + "秒");
    }
}
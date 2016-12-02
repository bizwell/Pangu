package com.joindata.inf.common.util.basic;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.xiaoleilu.hutool.util.StrUtil;

/**
 * 编码处理工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月4日 上午10:45:17
 */
public class CodecUtil
{
    /**
     * 把字符串 BASE64了<br />
     * <i>特性：编码UTF8，URL安全</i>
     * 
     * @param str 要转的字符串
     * @return 转换后的 BASE64 串
     */
    public static final String toBase64(String str)
    {
        if(str == null)
        {
            return null;
        }
        return Base64.encodeBase64URLSafeString(StrUtil.bytes(str, "UTF-8"));
    }

    /**
     * 将 BASE64 反转成明文
     * 
     * @param str 要转的 BASE64 字符串
     * @return 转换后的明文
     */
    public static final String fromBase64(String str)
    {
        if(str == null)
        {
            return null;
        }
        return StrUtil.str(Base64.decodeBase64(str), "UTF-8");
    }

    /**
     * 计算字符串的 MD5 值，大写返回
     * 
     * @param str 字符串
     * @return MD5 值
     */
    public static final String digestMD5(String str)
    {
        if(str == null)
        {
            return null;
        }
        return StringUtil.toUpperCase(DigestUtils.md5Hex(str));
    }

    /**
     * 计算字符串 SHA1 值，大写返回
     * 
     * @param str 字符串
     * @return SHA1 值
     */
    public static final String digestSHA1(String str)
    {
        if(str == null)
        {
            return null;
        }
        return StringUtil.toUpperCase(DigestUtils.sha1Hex(str));
    }

    /**
     * 字节数组 DES 加密 <i>不对外开放</i>
     * 
     * @param data 原文
     * @param key 密钥，必须8位
     * @return 加密后的字节数组
     * 
     * @throws GeneralSecurityException 出现各种加解密错误，抛出该异常
     */
    private static byte[] encryptDES(byte[] data, byte[] key) throws GeneralSecurityException
    {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 字节数组 DES 解密 <i>不对外开放</i>
     * 
     * @param data 密文
     * @param key 密钥，必须8位
     * @return 解密后的字节数组
     * 
     * @throws GeneralSecurityException 出现各种加解密错误，抛出该异常
     */
    private static byte[] decryptDES(byte[] data, byte[] key) throws GeneralSecurityException
    {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = null;
        keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = null;
        cipher = Cipher.getInstance("DES");

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * 字符串 DES 加密
     * 
     * @param sourceStr 原文
     * @param key 密钥，必须8位
     * @return 密文（且经过 BASE64 后的）
     * 
     * @throws GeneralSecurityException 出现各种加解密错误，抛出该异常
     */
    public static final String encryptDES(String sourceStr, String key) throws GeneralSecurityException
    {
        return Base64.encodeBase64URLSafeString(encryptDES(StrUtil.bytes(sourceStr, "UTF-8"), StrUtil.bytes(key, "UTF-8")));
    }

    /**
     * 字符串 DES 解密
     * 
     * @param secretStr 密文
     * @param key 密钥，必须8位
     * @return 原文
     * 
     * @throws GeneralSecurityException 出现各种加解密错误，抛出该异常
     */
    public static final String decryptDES(String secretStr, String key) throws GeneralSecurityException
    {
        return StrUtil.str(decryptDES(Base64.decodeBase64(secretStr), StrUtil.bytes(key, "UTF-8")), "UTF-8");
    }

    public static void main(String[] args) throws GeneralSecurityException
    {
        System.out.println(toBase64("我是你爸爸，I am your daddy!"));
        System.out.println(fromBase64("5oiR5piv5L2g54i454i477yMSSBhbSB5b3VyIGRhZGR5IQ"));
        System.out.println(digestMD5("admin"));
        System.out.println(digestSHA1("admin"));
        System.out.println(encryptDES("I am your daddy，我是你粑粑", "yourdadddddd"));
        System.out.println(decryptDES("R7kc-m60VKYQJyOHOYRozFU0onTiKR5S9dgBpSw2kQoaFhjIpTbZFA", "yourdadd"));
    }
}

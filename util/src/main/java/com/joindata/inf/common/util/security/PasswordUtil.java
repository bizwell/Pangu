package com.joindata.inf.common.util.security;

import java.util.regex.Pattern;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date Feb 15, 2017 4:55:05 PM
 */
public class PasswordUtil
{
    private static Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");

    /**
     * 判断是否 BCRYPT 字符串
     * 
     * @param str 要判断的字符串
     * @return true，如果是
     */
    public synchronized static boolean isBcrypt(String str)
    {
        return BCRYPT_PATTERN.matcher(str).matches();
    }

    /**
     * 通过 BCRYPT 加密密码<br />
     * <i>strength 为 10，salt 随意生成 </i>
     * 
     * @param password 原文
     * @return 密码
     */
    public static synchronized final String toBcrypt(String password)
    {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * 判断密码是否匹配
     * 
     * @param str1 原文
     * @param str2 密文
     * @return true，如果匹配
     */
    public static synchronized final boolean isMatchBcrypt(String str1, String str2)
    {
        return new BCryptPasswordEncoder().matches(str1, str2);
    }

    public static void main(String[] args)
    {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String e1 = encoder.encode("asdf123");
        System.out.println(e1);
        String e2 = encoder.encode(e1);
        System.out.println(e2);
        
        System.out.println(encoder.matches("111111", e1));
        System.out.println(encoder.matches("111111", e2));
        System.err.println("fuk");
        System.out.println(isBcrypt("111111"));
        System.out.println(isBcrypt(e1));
        System.out.println(isBcrypt(e2));

    }
}

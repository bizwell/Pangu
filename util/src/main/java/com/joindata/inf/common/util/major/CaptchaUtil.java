package com.joindata.inf.common.util.major;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.github.napp.kaptcha.Kaptcha;
import com.github.napp.kaptcha.KaptchaConfig;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.tools.RandomUtil;

/**
 * 验证码工具<br />
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月10日 下午6:21:48
 */
// TODO 后续考虑设计为组件或服务，因为验证码一般还与状态 Session 有关
public class CaptchaUtil
{
    /** 字母+数字的字符池，随机时将从中选取字符。这里面去掉了字母和数字之间容易混淆的字符，以免用户骂娘 */
    private static final char[] CHAR_POOL = new char[]{'A', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'a', 'c', 'd', 'e', 'f', 'h', 'j', 'k', 'm', 'n', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', '3', '4', '5', '7'};

    /** 验证码样式设置 */
    private static final KaptchaConfig KAPTCHA_CONFIG = new KaptchaConfig();

    static
    {
        KAPTCHA_CONFIG.setBackgroundColor(Color.white);
        KAPTCHA_CONFIG.setBorderColor(Color.BLACK);
        KAPTCHA_CONFIG.setTextColor(Color.BLUE);
        KAPTCHA_CONFIG.setHeight(45);
        KAPTCHA_CONFIG.setFontSize(32);
        KAPTCHA_CONFIG.setCharacterSpacing(3);
        KAPTCHA_CONFIG.setNoiseColor(Color.BLUE);
        KAPTCHA_CONFIG.setAddNoise(true);
        KAPTCHA_CONFIG.setDrawBorder(false);
    }

    /**
     * 生成随机字母+数字验证码
     * 
     * @param charCount 字符数
     * @return 验证码图像的字节数组
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] makeAlphanumberCaptcha(int charCount) throws IOException
    {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(new Kaptcha(KAPTCHA_CONFIG).createImage(RandomUtil.randomInSpecified(charCount, CHAR_POOL)), "gif", byteArrayOut);

        return byteArrayOut.toByteArray();
    }

    /**
     * 生成随机字母验证码
     * 
     * @param charCount 字符数
     * @return 验证码图像的字节数组
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] makeAlphabeticCaptcha(int charCount) throws IOException
    {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(new Kaptcha(KAPTCHA_CONFIG).createImage(RandomUtil.randomAlphabetic(charCount)), "gif", byteArrayOut);

        return byteArrayOut.toByteArray();
    }

    /**
     * 生成随机数字验证码
     * 
     * @param charCount 字符数
     * @return 验证码图像的字节数组
     * @throws IOException 发生任何 IO 错误，抛出该异常
     */
    public static final byte[] makeNumericCaptcha(int charCount) throws IOException
    {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
        ImageIO.write(new Kaptcha(KAPTCHA_CONFIG).createImage(RandomUtil.randomNumeric(charCount)), "gif", byteArrayOut);

        return byteArrayOut.toByteArray();
    }

    public static void main(String[] args) throws IOException
    {
        {
            FileUtils.writeByteArrayToFile(new File("/temp/test/captcha/makeAlphanumberCaptcha.gif"), makeAlphanumberCaptcha(4));
            System.out.println(FileUtil.getSize("/temp/test/captcha/makeAlphanumberCaptcha.gif"));
        }
        {
            FileUtils.writeByteArrayToFile(new File("/temp/test/captcha/makeAlphabeticCaptcha.gif"), makeAlphabeticCaptcha(10));
            System.out.println(FileUtil.getSize("/temp/test/captcha/makeAlphabeticCaptcha.gif"));
        }
        {
            FileUtils.writeByteArrayToFile(new File("/temp/test/captcha/makeNumericCaptcha.gif"), makeNumericCaptcha(4));
            System.out.println(FileUtil.getSize("/temp/test/captcha/makeNumericCaptcha.gif"));
        }
    }
}

package com.joindata.inf.common.util.basic;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * <h3>单词实用工具</h3><br />
 * <i>把 commons-lang 的 WordUtils 原封不动抄下来翻译了一下，并加了一些东西</i>
 * <p>
 * 处理字符串中包含单词的工具
 * </p>
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月3日 下午1:54:20
 */
public class WordUtil
{
    /**
     * 将文本中每个单词的首字母大写
     * 
     * @param text 要处理的文本
     * @return 处理后的文本
     */
    public static final String capitalize(String text)
    {
        return WordUtils.capitalize(text);
    }

    /**
     * 将文本中指定符号后的单词首字母大写
     * 
     * @param text 要处理的文本
     * @param delimiters 指定作为单词分隔符的字符，可以指定多个
     * @return 处理后的文本
     */
    public static final String capitalize(String text, char... delimiters)
    {
        return WordUtils.capitalize(text, delimiters);
    }

    /**
     * 将文本中每个单词的首字母小写<br />
     * <i>与 capitalize(text) 相反 </i>
     * 
     * @param text 要处理的文本
     * @return 处理后的文本
     */
    public static final String uncapitalize(String text)
    {
        return WordUtils.uncapitalize(text);
    }

    /**
     * 将文本中指定符号后的单词首字母小写 <i>与 capitalize(text, delimiters) 相反 </i>
     * 
     * @param text 要处理的文本
     * @param delimiters 指定作为单词分隔符的字符，可以指定多个
     * @return 处理后的文本
     */
    public static final String uncapitalize(String text, char... delimiters)
    {
        return WordUtils.uncapitalize(text, delimiters);
    }

    /**
     * 将文本中的每个单词都整理成首字母大写的形式<br />
     * <i>即使某个单词原本是全部大写，也会转成首字母大写，其他小写的形式</i>
     * 
     * @param text 要处理的文本
     * @return 处理后的文本
     */
    public static final String capitalizeFully(String text)
    {
        return WordUtils.capitalizeFully(text);
    }

    /**
     * 将文本中指定符号后的每个单词都整理成首字母大写的形式<br />
     * <i>即使某个单词原本是全部大写，也会转成首字母大写，其他小写的形式</i>
     * 
     * @param text 要处理的文本
     * @param delimiters 指定作为单词分隔符的字符，可以指定多个
     * @return 处理后的文本
     */
    public static final String capitalizeFully(String text, char... delimiters)
    {
        return WordUtils.capitalizeFully(text, delimiters);
    }

    /**
     * 将文本中每个单词首字母提取出来组成缩写，不改变原先的大小写
     * 
     * @param text 要处理的文本
     * @return 缩写
     */
    public static final String initials(String text)
    {
        return WordUtils.initials(text);
    }

    /**
     * 将文本中每个单词首字母提取出来组成缩写，并全部大写
     * 
     * @param text 要处理的文本
     * @return 全是大写的缩写
     */
    public static final String initialsAsUpperCase(String text)
    {
        return StringUtils.upperCase(WordUtils.initials(text));
    }

    /**
     * 将文本中每个单词首字母提取出来组成缩写，并指定单词分隔符，不改变原先的大小写<br />
     * <i>如果分隔符为 null，效果和 initials(text) 一样</i><br />
     * <i>如果指定了 0 个分隔符，将返回空字符串</i><br />
     * 
     * @param text 要处理的文本
     * @param delimiters 作为分隔符的字符，可指定多个
     * @return 缩写
     */
    public static final String initials(String text, char... delimiters)
    {
        return WordUtils.initials(text, delimiters);
    }

    /**
     * 将文本中每个单词首字母提取出来组成缩写，并指定单词分隔符，并全部大写<br />
     * <i>如果分隔符为 null，效果和 initials(text) 一样</i><br />
     * <i>如果指定了 0 个分隔符，将返回空字符串</i><br />
     * 
     * @param text 要处理的文本
     * @param delimiters 作为分隔符的字符，可指定多个
     * @return 全是大写的缩写
     */
    public static final String initialsAsUpperCase(String text, char... delimiters)
    {
        return StringUtils.upperCase(WordUtils.initials(text, delimiters));
    }

    public static void main(String[] args)
    {
        System.out.println(capitalize("i am your daddy. the girl who selling matches"));
        System.out.println(capitalize("i am your daddy. the girl who selling matches", 'a'));
        
        System.out.println(capitalizeFully("i am your daddy. the girl who selling matches"));
        System.out.println(capitalizeFully("i am your daddy. the girl who selling matches", 'a'));
        
        System.out.println(uncapitalize("I AM YOUR DADDY. THE GIRL WHO SELLING MATCHES"));
        System.out.println(uncapitalize("I AM YOUR DADDY. THE GIRL WHO SELLING MATCHES", 'A'));
        
        System.out.println(initials("I am your daddy"));
        System.out.println(initials("i am your daddy", 'a'));
        
        System.out.println(initialsAsUpperCase("oh my god"));
        System.out.println(initialsAsUpperCase("oh my god", 'o'));
    }
}

package com.joindata.inf.common.util.major;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.joindata.inf.common.basic.errors.ResourceErrors;
import com.joindata.inf.common.basic.exceptions.ResourceException;
import com.joindata.inf.common.util.basic.FileUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.steadystate.css.parser.CSSOMParser;

/**
 * HTML 工具类
 * 
 * @author <a href="mailto:songxiang@joindata.com">宋翔</a>
 * @date 2015年11月12日 下午3:43:36
 */
public class HtmlUtil
{
    /**
     * 将 HTML 过滤成标准的 XHTML 格式
     * 
     * @param html 源 HTML
     * @return 标准 XHTML 字符串
     */
    public static String toWellFormedXHTML(String html)
    {
        Document htmlDoc = Jsoup.parse(html, "", Parser.xmlParser());

        htmlDoc.outputSettings(htmlDoc.outputSettings().escapeMode(EscapeMode.xhtml));

        html = htmlDoc.toString();

        StringUtil.replaceAll(html, "<!DOCTYPE html>", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        return html;
    }

    /**
     * 获取某个元素的文本内容<br />
     * <i>匹配到多个元素，返回结果将是多个元素文本内容堆积到一起的字符串</i>
     * 
     * @param html HTML 字符串
     * @param cssQuery CSS 选择器，用于定位元素
     * @return 元素的文本内容
     */
    public static String getElementText(String html, String cssQuery)
    {
        return Jsoup.parse(html).select(cssQuery).text();
    }

    /**
     * 获取某个元素指定属性的值<br />
     * <i>如果匹配多个元素，只处理第一个元素的</i>
     * 
     * @param html HTML 字符串
     * @param cssQuery CSS 选择器，用于定位元素
     * @param attrName 属性名
     * @return 属性值
     */
    public static String getAttributeValue(String html, String cssQuery, String attrName)
    {
        return Jsoup.parse(html).select(cssQuery).attr(attrName);
    }

    /**
     * 获取某个元素内的所有 HTML 内容 <i>匹配到多个元素，返回结果将是多个元素 HTML 内容堆积到一起的字符串</i>
     * 
     * @param html HTML 字符串
     * @param cssQuery CSS 选择器，用于定位元素
     * @return 所有子元素的 HTML 内容
     */
    public static String getElementInnerHtml(String html, String cssQuery)
    {
        return Jsoup.parse(html).select(cssQuery).html();
    }

    /**
     * 将 CSS 全部改造成标记内联方式
     * 
     * @param url 要改造的 HTML 地址
     * @return 改造后的 HTML 字符串
     * @throws ResourceException 无法访问 HTML 中依赖资源，将抛出该异常
     */
    public static String inlineStyles(String url) throws ResourceException
    {
        Document document = null;
        try
        {
            document = Jsoup.connect(url).execute().parse();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new ResourceException(ResourceErrors.CANNOT_ACCESS, e.toString());
        }

        CSSOMParser parser = new CSSOMParser();

        StringBuffer styles = new StringBuffer();
        for(Element ele: document.getElementsByTag("style"))
        {
            styles.append(ele.html());
        }

        for(Element ele: document.getElementsByAttributeValue("rel", "stylesheet"))
        {
            try
            {
                styles.append(Jsoup.connect(ele.absUrl("href")).execute().body().toString());
            }
            catch(IllegalArgumentException e)
            {
                throw new ResourceException(ResourceErrors.CANNOT_ACCESS, "无法下载 " + ele.absUrl("href") + "文件");
            }
            catch(IOException e)
            {
                throw new ResourceException(ResourceErrors.CANNOT_ACCESS, ele.absUrl("href") + "。" + e.toString());
            }
        }

        InputSource source = new InputSource(new StringReader(styles.toString()));
        CSSStyleSheet stylesheet = null;
        try
        {
            stylesheet = parser.parseStyleSheet(source, null, null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new ResourceException(e.toString());
        }

        CSSRuleList ruleList = stylesheet.getCssRules();
        Map<Element, Map<String, String>> allElementsStyles = new HashMap<Element, Map<String, String>>();
        for(int ruleIndex = 0; ruleIndex < ruleList.getLength(); ruleIndex++)
        {
            CSSRule item = ruleList.item(ruleIndex);
            if(item instanceof CSSStyleRule)
            {
                CSSStyleRule styleRule = (CSSStyleRule)item;
                String cssSelector = styleRule.getSelectorText();
                if(StringUtil.contains(cssSelector, ":"))
                {
                    continue;
                }
                Elements elements = document.select(cssSelector);
                for(int elementIndex = 0; elementIndex < elements.size(); elementIndex++)
                {
                    Element element = elements.get(elementIndex);
                    Map<String, String> elementStyles = allElementsStyles.get(element);
                    if(elementStyles == null)
                    {
                        elementStyles = new LinkedHashMap<String, String>();
                        allElementsStyles.put(element, elementStyles);
                    }
                    CSSStyleDeclaration style = styleRule.getStyle();
                    for(int propertyIndex = 0; propertyIndex < style.getLength(); propertyIndex++)
                    {
                        String propertyName = style.item(propertyIndex);
                        String propertyValue = style.getPropertyValue(propertyName);
                        elementStyles.put(propertyName, propertyValue);
                    }
                }
            }
        }

        for(Map.Entry<Element, Map<String, String>> elementEntry: allElementsStyles.entrySet())
        {
            Element element = elementEntry.getKey();
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> styleEntry: elementEntry.getValue().entrySet())
            {
                builder.append(styleEntry.getKey()).append(":").append(styleEntry.getValue()).append(";");
            }
            builder.append(element.attr("style"));
            element.attr("style", builder.toString());
        }

        for(Element ele: document.select("style, script"))
        {
            ele.remove();
        }
        for(Element ele: document.select("head :not(title, meta)"))
        {
            ele.remove();
        }

        return document.html();
    }

    /**
     * 将 CSS 全部改造成标记内联方式<br />
     * <b>baseURL 最好不要是空的，因为 HTML 文档中如果包含引入的 CSS 资源，就需要从源地址下载，此时如果 CSS 资源给的是相对路径，将不知道从哪里去下载，可能会报 IO 异常</b><br />
     * <b>这个方法最后会删除掉所有的 style、script、link 标签</b>
     * 
     * @param baseURL HTML 前缀地址，样式文件的命名空间
     * @param html HTML 字符串
     * @return 改造后的 HTML 字符串
     * @throws ResourceException 无法访问 HTML 中依赖资源，抛出该异常
     */
    public static String inlineStyles(String html, String baseURL) throws ResourceException
    {
        Document document = StringUtil.isNullOrEmpty(baseURL) ? Jsoup.parse(html) : Jsoup.parse(html, baseURL);
        CSSOMParser parser = new CSSOMParser();

        StringBuffer styles = new StringBuffer();
        for(Element ele: document.getElementsByTag("style"))
        {
            styles.append(ele.html());
        }

        for(Element ele: document.getElementsByAttributeValue("rel", "stylesheet"))
        {
            try
            {
                styles.append(Jsoup.connect(ele.absUrl("href")).execute().body().toString());
            }
            catch(IllegalArgumentException e)
            {
                if(StringUtil.isNullOrEmpty(baseURL))
                {
                    continue;
                }
                else
                {
                    throw new ResourceException(ResourceErrors.CANNOT_ACCESS, "无法下载 " + ele.absUrl("href") + "文件");
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                throw new ResourceException(ResourceErrors.CANNOT_ACCESS, ele.absUrl("href") + "。" + e.toString());
            }
        }

        InputSource source = new InputSource(new StringReader(styles.toString()));
        CSSStyleSheet stylesheet = null;
        try
        {
            stylesheet = parser.parseStyleSheet(source, null, null);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new ResourceException(e.toString());
        }

        CSSRuleList ruleList = stylesheet.getCssRules();
        Map<Element, Map<String, String>> allElementsStyles = new HashMap<Element, Map<String, String>>();
        for(int ruleIndex = 0; ruleIndex < ruleList.getLength(); ruleIndex++)
        {
            CSSRule item = ruleList.item(ruleIndex);
            if(item instanceof CSSStyleRule)
            {
                CSSStyleRule styleRule = (CSSStyleRule)item;
                String cssSelector = styleRule.getSelectorText();
                if(StringUtil.contains(cssSelector, ":"))
                {
                    continue;
                }
                Elements elements = document.select(cssSelector);
                for(int elementIndex = 0; elementIndex < elements.size(); elementIndex++)
                {
                    Element element = elements.get(elementIndex);
                    Map<String, String> elementStyles = allElementsStyles.get(element);
                    if(elementStyles == null)
                    {
                        elementStyles = new LinkedHashMap<String, String>();
                        allElementsStyles.put(element, elementStyles);
                    }
                    CSSStyleDeclaration style = styleRule.getStyle();
                    for(int propertyIndex = 0; propertyIndex < style.getLength(); propertyIndex++)
                    {
                        String propertyName = style.item(propertyIndex);
                        String propertyValue = style.getPropertyValue(propertyName);
                        elementStyles.put(propertyName, propertyValue);
                    }
                }
            }
        }

        for(Map.Entry<Element, Map<String, String>> elementEntry: allElementsStyles.entrySet())
        {
            Element element = elementEntry.getKey();
            StringBuilder builder = new StringBuilder();
            for(Map.Entry<String, String> styleEntry: elementEntry.getValue().entrySet())
            {
                builder.append(styleEntry.getKey()).append(":").append(styleEntry.getValue()).append(";");
            }
            builder.append(element.attr("style"));
            element.attr("style", builder.toString());
        }

        for(Element ele: document.select("style, script"))
        {
            ele.remove();
        }
        for(Element ele: document.select("head :not(title, meta)"))
        {
            ele.remove();
        }

        return document.html();
    }

    public static void main(String[] args) throws IOException, ResourceException
    {
        System.out.println(toWellFormedXHTML(FileUtil.readFile("/temp/test/html/src-html.html")));

        System.out.println("-----------------------");

        System.out.println(getElementText(FileUtil.readFile("/temp/test/html/src-html.html"), "ul"));

        System.out.println("-----------------------");

        System.out.println(getAttributeValue(FileUtil.readFile("/temp/test/html/src-html.html"), "div", "class"));

        System.out.println("-----------------------");

        System.out.println(getElementInnerHtml(FileUtil.readFile("/temp/test/html/src-html.html"), "ul"));

        System.out.println("-----------------------");

        System.out.println(inlineStyles("http://www.baidu.com"));

        System.out.println("-----------------------");

        System.out.println(inlineStyles(FileUtil.readFile("/temp/test/html/src-html.html"), "http://zhidao.baidu.com/"));
    }
}

package com.joindata.inf.common.support.swagger.web;

import java.net.MalformedURLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.joindata.inf.common.basic.support.BootInfoHolder;

import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import io.github.swagger2markup.utils.URIUtils;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/api-docs")
@ApiIgnore
public class APIDocExporter
{

    @GetMapping("/preview")
    public ModelAndView previewDoc() throws MalformedURLException
    {
        ModelAndView modelAndView = new ModelAndView("markdown2html");
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder().withMarkupLanguage(MarkupLanguage.MARKDOWN).build();
        String markdown = Swagger2MarkupConverter.from(URIUtils.create("http://localhost:" + BootInfoHolder.get("WebPort") + "/v2/api-docs")).withConfig(config).build().toString();
        modelAndView.addObject("markdown", markdown);
        return modelAndView;

    }
}

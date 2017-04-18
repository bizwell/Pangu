package com.joindata.inf.common.support.swagger.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.mbtest.javabank.http.imposters.Imposter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.joindata.inf.common.basic.support.BootInfoHolder;
import com.joindata.inf.common.support.swagger.core.MockAPI;
import com.joindata.inf.common.support.swagger.core.MockServerInfo;
import com.joindata.inf.common.support.swagger.core.MountebankClient;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;
import com.xiaoleilu.hutool.io.IoUtil;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/mockserver")
@ApiIgnore
public class MockServerController
{
    private static final Logger log = Logger.get();

    @Autowired
    private MountebankClient mountebankClient;

    @ResponseBody
    @PostMapping("/mockApi")
    ResponseEntity<Imposter> createMock(@RequestBody MockAPI mockData, HttpServletRequest servletRequest) throws ParseException
    {
        return new ResponseEntity<Imposter>(mountebankClient.createMock(mockData), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping()
    MockServerInfo getMockServerInfo()
    {
        return new MockServerInfo("mockserver", mountebankClient.getMockServerPort());
    }

    @RequestMapping("/export")
    public void exportMock(HttpServletResponse response) throws Exception
    {
        Imposter imposter = mountebankClient.getImposter();

        response.setContentType("text/json");
        String reportName = BootInfoHolder.getAppId() + ":" + mountebankClient.getMockServerPort() + ".ejs";
        response.setHeader("Content-disposition", "attachment;filename=" + reportName);

        if(imposter != null)
        {
            response.getOutputStream().write(JSON.toJSONString(imposter).getBytes("utf-8"));
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public ResponseEntity<String> importMock(@RequestParam("file") MultipartFile file) throws Exception
    {
        log.info("import file: {}", file.getOriginalFilename());
        if(!StringUtil.endsWith(file.getOriginalFilename(), ".ejs"))
        {
            log.warn("只支持文件格式.ejs");
            throw new RuntimeException("只支持文件格式.ejs");
        }
        String imposterStr = IoUtil.read(file.getInputStream(), "utf-8");
        Imposter imposter = JSON.parseObject(imposterStr, Imposter.class);
        mountebankClient.updateImposter(imposter);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

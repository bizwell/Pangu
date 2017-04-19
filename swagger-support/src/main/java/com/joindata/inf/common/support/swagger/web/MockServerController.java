package com.joindata.inf.common.support.swagger.web;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.joindata.inf.common.support.swagger.core.MockAPI;
import com.joindata.inf.common.support.swagger.core.MockServerInfo;
import com.joindata.inf.common.support.swagger.core.MountebankClient;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/mockserver")
@ApiIgnore
public class MockServerController
{
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
}

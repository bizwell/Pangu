package com.joindata.inf.common.support.camunda.properties;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Service
@Scope("singleton")
@DisconfFile(filename = "camunda.properties")
public class CamundaProperties
{
    private String dbUrl;

    private String dbUsername;

    private String dbPassword;

    @DisconfFileItem(name = "db.url", associateField = "dbUrl")
    public String getDbUrl()
    {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }

    @DisconfFileItem(name = "db.username", associateField = "dbUsername")
    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    @DisconfFileItem(name = "db.password", associateField = "dbPassword")
    public String getDbPassword()
    {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

}
package com.senla.training_2019.smolka.web.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"classpath:/config.properties"})
public class FileConfig {

    @Value("${dataBaseCharSet}")
    private String dataBaseCharSet;

    @Value("${dataBaseCharacterEncoding}")
    private String dataBaseCharacterEncoding;

    @Value("${useUnicode}")
    private Boolean useUnicode;

    @Value("${dialectName}")
    private String hibernateDialectName;

    @Value("${showSql}")
    private boolean showSql;

    @Value("${driverClassName}")
    private String driverNameClass;

    @Value("${dataBaseUrl}")
    private String dataBaseUrl;

    @Value("${dataBaseUsername}")
    private String dataBaseUsername;

    @Value("${dataBasePassword}")
    private String dataBasePassword;

    @Value("${timeZone}")
    private String timeZone;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${tokenHeader}")
    private String tokenHeader;

    public String getDataBaseCharSet() {
        return dataBaseCharSet;
    }

    public void setDataBaseCharSet(String dataBaseCharSet) {
        this.dataBaseCharSet = dataBaseCharSet;
    }

    public String getDataBaseCharacterEncoding() {
        return dataBaseCharacterEncoding;
    }

    public void setDataBaseCharacterEncoding(String dataBaseCharacterEncoding) {
        this.dataBaseCharacterEncoding = dataBaseCharacterEncoding;
    }

    public Boolean getUseUnicode() {
        return useUnicode;
    }

    public void setUseUnicode(Boolean useUnicode) {
        this.useUnicode = useUnicode;
    }

    public String getHibernateDialectName() {
        return hibernateDialectName;
    }

    public void setHibernateDialectName(String hibernateDialectName) {
        this.hibernateDialectName = hibernateDialectName;
    }

    public Boolean getShowSql() {
        return showSql;
    }

    public void setShowSql(Boolean showSql) {
        this.showSql = showSql;
    }

    public String getDriverNameClass() {
        return driverNameClass;
    }

    public void setDriverNameClass(String driverNameClass) {
        this.driverNameClass = driverNameClass;
    }

    public String getDataBaseUrl() {
        return dataBaseUrl;
    }

    public void setDataBaseUrl(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
    }

    public String getDataBaseUsername() {
        return dataBaseUsername;
    }

    public void setDataBaseUsername(String dataBaseUsername) {
        this.dataBaseUsername = dataBaseUsername;
    }

    public String getDataBasePassword() {
        return dataBasePassword;
    }

    public void setDataBasePassword(String dataBasePassword) {
        this.dataBasePassword = dataBasePassword;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public void setTokenHeader(String tokenHeader) {
        this.tokenHeader = tokenHeader;
    }
}
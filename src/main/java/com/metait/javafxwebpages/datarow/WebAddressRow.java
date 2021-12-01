package com.metait.javafxwebpages.datarow;

import java.util.Date;
import java.text.SimpleDateFormat;

public class WebAddressRow {
    public WebAddressDataRowCell getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(WebAddressDataRowCell orderNumber) {
        this.orderNumber = orderNumber;
    }

    public WebAddressDataRowCell getStars() {
        return stars;
    }

    public void setStars(WebAddressDataRowCell stars) {
        this.stars = stars;
    }

    public WebAddressDataRowCell getDate() {
        return date;
    }

    public void setDate(WebAddressDataRowCell date) {
        this.date = date;
    }

    public WebAddressDataRowCell getKeyrords() {
        return keyrords;
    }

    public void setKeyrords(WebAddressDataRowCell keyrords) {
        this.keyrords = keyrords;
    }

    public WebAddressDataRowCell getWebadress() {
        return webadress;
    }

    public void setWebadress(WebAddressDataRowCell webadress) {
        this.webadress = webadress;
    }

    private WebAddressDataRowCell orderNumber = null;
    private WebAddressDataRowCell stars = null;
    private WebAddressDataRowCell date = null;
    private WebAddressDataRowCell keyrords = null;
    private WebAddressDataRowCell webadress = null;

    public WebAddressRow(int iOrder, int iStars, String keyWords, String webAddress)
    {
        orderNumber = new WebAddressDataRowCell("" +iOrder, false);
        stars = new WebAddressDataRowCell(getStartsAfter(iStars), true);
        date = new WebAddressDataRowCell(getTodayString(), false);
        keyrords = new WebAddressDataRowCell(keyWords, true);
        webadress = new WebAddressDataRowCell(webAddress, false);
    }

    private String getStartsAfter(int iStars)
    {
        int max = iStars;
        String ret = "";
        for(int i = 0; i < max; i++)
            ret = ret +"*";
        return ret;
    }

    private String getTodayString()
    {
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}

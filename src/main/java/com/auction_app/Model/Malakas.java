package com.auction_app.Model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "malakas")
@XmlAccessorType(XmlAccessType.FIELD)
public class Malakas {
    @XmlElement
    private Double num;
    @XmlElement
    private String str;

    public Malakas() {
    }

    public Malakas(Double x, String s) {
        this.num = x;
        this.str = s;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}

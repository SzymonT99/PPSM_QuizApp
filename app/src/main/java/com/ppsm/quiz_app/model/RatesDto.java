package com.ppsm.quiz_app.model;

public class RatesDto {

    private Integer goodRates;
    private Integer badRates;

    public RatesDto() {
    }

    public RatesDto(Integer goodRates, Integer badRates) {
        this.goodRates = goodRates;
        this.badRates = badRates;
    }

    public Integer getGoodRates() {
        return goodRates;
    }

    public void setGoodRates(Integer goodRates) {
        this.goodRates = goodRates;
    }

    public Integer getBadRates() {
        return badRates;
    }

    public void setBadRates(Integer badRates) {
        this.badRates = badRates;
    }
}

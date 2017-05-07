package com.joseilton.mobilecontroller.app;

import java.util.Date;

public class Periodo {
    private Date begin;
    private Date end;

    public Periodo() {
    }

    public Periodo(Date begin, Date end) {
        this.begin = begin;
        this.end = end;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getBegin() {
        return begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }
}

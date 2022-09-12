package com.universeluvv.ktnuvoting.InfoClass;

public class VoteInfo {
    private String name;
    private String term;
    private int check;
    private int current_cnt;
    private  int total_cnt;

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }


    public VoteInfo() {}

    public VoteInfo(String name, String term, int check){
        this.name =name;
        this.term = term;
        this.check =check;

    }
    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getCurrent_cnt() {
        return current_cnt;
    }

    public void setCurrent_cnt(int current_cnt) {
        this.current_cnt = current_cnt;
    }

    public int getTotal_cnt() {
        return total_cnt;
    }

    public void setTotal_cnt(int total_cnt) {
        this.total_cnt = total_cnt;
    }
}


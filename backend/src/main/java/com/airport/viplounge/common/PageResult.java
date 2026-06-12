package com.airport.viplounge.common;

import java.io.Serializable;
import java.util.List;

public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> list;
    private long total;
    private long current;
    private long size;

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public long getCurrent() { return current; }
    public void setCurrent(long current) { this.current = current; }
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public PageResult() {
    }

    public PageResult(List<T> list, long total, long current, long size) {
        this.list = list;
        this.total = total;
        this.current = current;
        this.size = size;
    }
}

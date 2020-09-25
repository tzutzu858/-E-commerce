package com.company.store.domain;


import java.util.Date;

public class Orders {

    private String id;
    private Date order_date;
    /*訂單狀態 1代表待確認 0代表已確認*/
    private int status = 1;
    private double total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrderDate() {
        return order_date;
    }

    public void setOrderDate(Date orderDate) {
        this.order_date = orderDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

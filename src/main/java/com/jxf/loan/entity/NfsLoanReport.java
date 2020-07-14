package com.jxf.loan.entity;

import java.math.BigDecimal;

public class NfsLoanReport {

    private BigDecimal amount;
    private int quantity;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
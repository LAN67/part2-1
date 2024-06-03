package ru.part2;

import lombok.Getter;

public class StackNode {
    @Getter
    private String name;
    @Getter
    private CurrencySumma.EnumCurrency currency;
    @Getter
    private Object value;

    public StackNode(String name, CurrencySumma.EnumCurrency currency, Object value) {
        this.name = name;
        this.currency = currency;
        this.value = value;
    }


}

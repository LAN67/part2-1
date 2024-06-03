package ru.part2;

import lombok.Getter;
import java.util.Objects;

public class CurrencySumma {
    public enum EnumCurrency {
        RUB,
        USD,
        EUR,
        CNY
    }

    @Getter
    private EnumCurrency currency;

    public CurrencySumma(EnumCurrency currency) {
        this.currency = currency;
    }

    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.currency);
        return hash;
    }

    public boolean equals(Object obj) {
        if (this == obj)  return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CurrencySumma other = (CurrencySumma) obj;
        return Objects.equals(this.currency, other.currency);
    }
}

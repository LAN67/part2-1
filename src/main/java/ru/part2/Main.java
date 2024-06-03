package ru.part2;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Account acc = new Account("Ivan");
        System.out.println(acc);

        acc.addSumma(10, CurrencySumma.EnumCurrency.RUB);
        System.out.println(acc);

        acc.addSumma(20, CurrencySumma.EnumCurrency.RUB);
        System.out.println(acc);

        acc.addSumma(30, CurrencySumma.EnumCurrency.USD);
        System.out.println(acc);

        acc.addSumma(40, CurrencySumma.EnumCurrency.RUB);
        System.out.println(acc);

        acc.addSumma(50, CurrencySumma.EnumCurrency.EUR);
        System.out.println(acc);

        acc.addSumma(60, CurrencySumma.EnumCurrency.CNY);
        System.out.println(acc);

        System.out.println("=== balances");
        Map<CurrencySumma, Integer> balances = new HashMap<>();
        balances = acc.getBalances();
        balances.forEach((x,y) -> System.out.println("[" + y + " " + x.getCurrency() + "]"));
        CurrencySumma cur = new CurrencySumma(CurrencySumma.EnumCurrency.RUB);
        System.out.println("изменение");
        balances.put(cur, 1024);
        System.out.println("balance");
        balances.forEach((x,y) -> System.out.println("[" + y + " " + x.getCurrency() + "]"));
        System.out.println("acc");
        System.out.println(acc);


        System.out.println("=== undo");
        System.out.println(acc);
        acc.setName("Alex");
        acc.setName("Alex2");
        acc.setName("Alex3");
        System.out.println(acc);
        acc.undo();
        System.out.println(acc);
        acc.undo();
        System.out.println(acc);
        acc.undo();
        System.out.println(acc);
        acc.undo();
        System.out.println(acc);

        System.out.println("=== save load");
        Account acc2 = new Account("Sasha");
        acc2.addSumma(100, CurrencySumma.EnumCurrency.RUB);
        acc2.addSumma(200, CurrencySumma.EnumCurrency.USD);
        acc2.addSumma(300, CurrencySumma.EnumCurrency.CNY);
        System.out.println(acc2);

        System.out.println("save Account");
        AccountSave accSave = acc2.save();

        acc2.setName("Vasya");
        acc2.addSumma(333, CurrencySumma.EnumCurrency.CNY);
        acc2.addSumma(400, CurrencySumma.EnumCurrency.EUR);
        System.out.println(acc2);

        System.out.println("load Account");
        acc2.load(accSave);
        System.out.println(acc2);
    }
}

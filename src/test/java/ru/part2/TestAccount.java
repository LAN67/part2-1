package ru.part2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestAccount {
    @Test
    @DisplayName("Корректное имя")
    public void TestNameGood(){
        Account acc1 = new Account("Ivan");
        Assertions.assertEquals("Ivan", acc1.getName());
        acc1.setName("Petr");
        Assertions.assertEquals("Petr", acc1.getName());
    }
    @Test
    @DisplayName("Не корректное имя")
    public void TestNameException(){
        Assertions.assertThrows(AccountException.class, (() -> {Account acc2 = new Account(null);}));
        Assertions.assertThrows(AccountException.class, (() -> {Account acc3 = new Account("");}));

        Account acc4 = new Account("Ivan");
        Assertions.assertThrows(AccountException.class,(() -> acc4.setName(null)));
        Assertions.assertThrows(AccountException.class,(() -> acc4.setName("")));
    }

    @Test
    @DisplayName("Проверка пар: сумма и валюта")
    public void TestBalance(){
        Account acc = new Account("Ivan");
        Assertions.assertThrows(AccountException.class, (() -> acc.addSumma(-1, CurrencySumma.EnumCurrency.RUB)));

        acc.addSumma(10, CurrencySumma.EnumCurrency.RUB);
        Assertions.assertEquals(10, acc.getSumma(CurrencySumma.EnumCurrency.RUB));

        acc.addSumma(20, CurrencySumma.EnumCurrency.RUB);
        Assertions.assertEquals(20, acc.getSumma(CurrencySumma.EnumCurrency.RUB));

        acc.addSumma(30, CurrencySumma.EnumCurrency.CNY);
        Assertions.assertEquals(30, acc.getSumma(CurrencySumma.EnumCurrency.CNY));
    }

    @Test
    @DisplayName("проверка Undo")
    public void TestUndo(){
        Account acc = new Account("Ivan");
        Assertions.assertEquals("Ivan", acc.getName());
        acc.setName("Alex");
        Assertions.assertEquals("Alex", acc.getName());
        acc.undo();
        Assertions.assertEquals("Ivan", acc.getName());

        acc.addSumma(10, CurrencySumma.EnumCurrency.RUB);
        Assertions.assertEquals(10, acc.getSumma(CurrencySumma.EnumCurrency.RUB));
        acc.addSumma(20, CurrencySumma.EnumCurrency.RUB);
        Assertions.assertEquals(20, acc.getSumma(CurrencySumma.EnumCurrency.RUB));
        acc.undo();
        Assertions.assertEquals(10, acc.getSumma(CurrencySumma.EnumCurrency.RUB));
        Assertions.assertEquals(true, acc.undoCheck());
        acc.undo();
        Assertions.assertEquals(false, acc.undoCheck());
        Assertions.assertThrows(AccountException.class, (acc::undo)); // стек undo пустой
        //начальные значения
        Assertions.assertEquals("Ivan", acc.getName());
        Assertions.assertEquals(null, acc.getSumma(CurrencySumma.EnumCurrency.RUB));

        acc.addSumma(1024, CurrencySumma.EnumCurrency.USD);
        Assertions.assertEquals(1024, acc.getSumma(CurrencySumma.EnumCurrency.USD));
        acc.addSumma(20, CurrencySumma.EnumCurrency.RUB);
        Assertions.assertEquals(20, acc.getSumma(CurrencySumma.EnumCurrency.RUB));
        acc.undo();
        Assertions.assertEquals(1024, acc.getSumma(CurrencySumma.EnumCurrency.USD));
    }

    @Test
    @DisplayName("проверка getBalances")
    public void TestGetBalance(){
        Account acc = new Account("Ivan");
        acc.addSumma(2000, CurrencySumma.EnumCurrency.RUB);
        acc.addSumma(3000, CurrencySumma.EnumCurrency.USD);
        acc.addSumma(4000, CurrencySumma.EnumCurrency.CNY);

        Map<CurrencySumma, Integer> balances = new HashMap<>();
        balances = acc.getBalances();
        CurrencySumma cur = new CurrencySumma(CurrencySumma.EnumCurrency.RUB);
        balances.put(cur, 1024);
        Assertions.assertEquals(2000, acc.getSumma(CurrencySumma.EnumCurrency.RUB));
    }

    @Test
    @DisplayName("проверка Save")
    public void TestSave(){
        Account acc = new Account("Ivan");
        acc.addSumma(2000, CurrencySumma.EnumCurrency.RUB);
        acc.addSumma(3000, CurrencySumma.EnumCurrency.USD);
        acc.addSumma(4000, CurrencySumma.EnumCurrency.CNY);

        AccountSave accSave = acc.save();
        List<StackNode> listSN = accSave.getListSave();
        listSN.add(new StackNode("balances", CurrencySumma.EnumCurrency.EUR, 123));


        acc.addSumma(1024, CurrencySumma.EnumCurrency.USD);
        acc.load(accSave);

        Assertions.assertEquals(3000, acc.getSumma(CurrencySumma.EnumCurrency.USD));
        Assertions.assertEquals(null, acc.getSumma(CurrencySumma.EnumCurrency.EUR));
    }
}

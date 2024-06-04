package ru.part2;

import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;


public class Account {
    @Getter
    private String name;

    private Map<CurrencySumma, Integer> balances = new HashMap<>();
    private Stack<StackNode> undoStack = new Stack<>();


    public Account(String name) {
        if (name == null || name.equals("")) throw new AccountException("Ошибка. Не задано имя.");
        this.name = name;
    }

    public Map<CurrencySumma, Integer> getBalances() {
        return new HashMap<>(balances);
    }

    public void addSumma(Integer summa, CurrencySumma.EnumCurrency currency) {
        if (summa < 0)
            throw new AccountException("Ошибка. Передана сумма " + summa + ". Сумма должна быть больше нуля.");
        history("balances", currency);
        CurrencySumma cur = new CurrencySumma(currency);
        balances.put(cur, summa);
    }

    public Integer getSumma(CurrencySumma.EnumCurrency currency) {
        CurrencySumma cur = new CurrencySumma(currency);
        return balances.get(cur);
    }

    public void setName(String name) {
        if (name == null || name.equals("")) throw new AccountException("Ошибка. Не задано имя.");
        history("name", null);
        this.name = name;
    }

    @Override
    public String toString() {
        final String[] str = {""};
        str[0] = "Account{" +
                "name='" + name + '\'' +
                ", balances={";
        balances.forEach((x, y) -> str[0] += ("[" + y + " " + x.getCurrency()) + "]");
        return str[0] + "}}";
    }

    protected void history(String nameField, CurrencySumma.EnumCurrency currency) {
        Class thisClass = this.getClass();
        for (Field f : thisClass.getDeclaredFields()) {
            if (f.getName().equals(nameField)) {
                try {
                    if (f.getName().equals("balances")) {
                        CurrencySumma cur = new CurrencySumma(currency);
                        StackNode sn = new StackNode(nameField, currency, balances.get(cur));
                        undoStack.push(sn);
                    } else {
                        StackNode sn = new StackNode(nameField, null, f.get(this));
                        undoStack.push(sn);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    public void undo() {
        if (!undoCheck()) throw new AccountException("Ошибка. Стек Undo пуст.");
        StackNode sn = undoStack.pop();
        Class thisClass = this.getClass();
        for (Field f : thisClass.getDeclaredFields()) {
            if (f.getName().equals(sn.getName())) {
                if (sn.getName().equals("balances")) {
                    CurrencySumma cur = new CurrencySumma(sn.getCurrency());
                    if ((Integer) sn.getValue() == null) {
                        balances.remove(cur); // первое значение по валюте. Удаляем запись.
                    } else {
                        balances.put(cur, (Integer) sn.getValue());
                    }
                } else {
                    try {
                        f.setAccessible(true);
                        f.set(this, sn.getValue());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
        }
    }

    public boolean undoCheck() {
        return !undoStack.isEmpty();
    }

    public AccountSave save() {
        List<StackNode> listSave = new ArrayList<>();

        Class thisClass = this.getClass();
        for (Field f : thisClass.getDeclaredFields()) {
            if (!f.getName().equals("undoStack")) {
                try {
                    if (f.getName().equals("balances")) {
                        balances.forEach((x, y) -> listSave.add(new StackNode(f.getName(), x.getCurrency(), y)));
                    } else {
                        listSave.add(new StackNode(f.getName(), null, f.get(this)));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new AccountSave(listSave);
    }

    public void load(AccountSave listSave) {
        balances.clear();
        for (StackNode sn : listSave.getListSave()) {
            if (sn.getName().equals("balances")) {
                CurrencySumma cur = new CurrencySumma(sn.getCurrency());
                balances.put(cur, (Integer) sn.getValue());
            } else {
                Class thisClass = this.getClass();
                for (Field f : thisClass.getDeclaredFields()) {
                    if (f.getName().equals(sn.getName())) {
                        try {
                            f.setAccessible(true);
                            f.set(this, sn.getValue());
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}

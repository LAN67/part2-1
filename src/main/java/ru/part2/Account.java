package ru.part2;

import lombok.Getter;

import java.util.*;


public class Account {

    @Getter
    private String name;

    private Map<CurrencySumma, Integer> balances = new HashMap<>();
    private Deque<Command> undoStack = new ArrayDeque<>();


    public Account(String name) {
        if (name == null || name.isBlank()) throw new AccountException("Ошибка. Не задано имя.");
        this.name = name;
    }

    public Map<CurrencySumma, Integer> getBalances() {
        return new HashMap<>(balances);
    }

    public void addSumma(Integer summa, CurrencySumma.EnumCurrency currency) {
        if (currency == null)
            throw new AccountException("Ошибка. Валюта не задана.");
        if (summa < 0)
            throw new AccountException("Ошибка. Передана сумма " + summa + ". Сумма должна быть больше нуля.");
        CurrencySumma cur = new CurrencySumma(currency);
        Integer num = balances.get(cur);
        if (num == null) {
            undoStack.push(() -> Account.this.balances.remove(cur));
        } else {
            undoStack.push(() -> Account.this.balances.put(cur, num));
        }
        balances.put(cur, summa);
    }

    public Integer getSumma(CurrencySumma.EnumCurrency currency) {
        CurrencySumma cur = new CurrencySumma(currency);
        Integer num = balances.get(cur);
        return num;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) throw new AccountException("Ошибка. Не задано имя.");
        String str = this.name;
        undoStack.push(() -> Account.this.name = str);
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

    public void undo() {
        if (!undoCheck()) throw new AccountException("Ошибка. Стек Undo пуст.");
        undoStack.pop().make();
    }

    public boolean undoCheck() {
        return !undoStack.isEmpty();
    }

    public AccountSave save() {
        return new AccSave();
//        List<StackNode> listSave = new ArrayList<>();
//
//        Class thisClass = this.getClass();
//        for (Field f : thisClass.getDeclaredFields()) {
//            if (!f.getName().equals("undoStack")) {
//                try {
//                    if (f.getName().equals("balances")) {
//                        balances.forEach((x, y) -> listSave.add(new StackNode(f.getName(), x.getCurrency(), y)));
//                    } else {
//                        listSave.add(new StackNode(f.getName(), null, f.get(this)));
//                    }
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return new AccountSave(listSave);
    }

//    public void load(AccountSave listSave) {
//        balances.clear();
//        for (StackNode sn : listSave.getListSave()) {
//            if (sn.getName().equals("balances")) {
//                CurrencySumma cur = new CurrencySumma(sn.getCurrency());
//                balances.put(cur, (Integer) sn.getValue());
//            } else {
//                Class thisClass = this.getClass();
//                for (Field f : thisClass.getDeclaredFields()) {
//                    if (f.getName().equals(sn.getName())) {
//                        try {
//                            f.setAccessible(true);
//                            f.set(this, sn.getValue());
//                        } catch (IllegalAccessException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }
//            }
//        }
//    }

    private class AccSave implements AccountSave {
        private String name = Account.this.name;
        private Map<CurrencySumma, Integer> balances = new HashMap<>(Account.this.balances);

        public void load(){
            Account.this.name = name;
            Account.this.balances.clear();
            Account.this.balances.putAll(balances);
        }
    }
}

interface Command {
    void make();
}
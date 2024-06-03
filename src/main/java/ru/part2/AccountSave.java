package ru.part2;

import java.util.ArrayList;
import java.util.List;

public class AccountSave {
    private List<StackNode> listSave = new ArrayList<>();

    public AccountSave(List<StackNode> listSave) {
        this.listSave = listSave;
    }

    public List<StackNode> getListSave() {
        return new ArrayList<>(listSave);
    }
}

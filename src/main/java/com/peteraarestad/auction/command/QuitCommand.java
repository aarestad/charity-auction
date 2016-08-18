package com.peteraarestad.auction.command;

import java.util.List;

public class QuitCommand implements Command {
    @Override
    public String execute(List<String> args) {
        System.exit(0);
        return null;
    }
}

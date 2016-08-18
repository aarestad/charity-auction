package com.peteraarestad.auction.command;

import java.util.List;

public class QuitCommand implements Command {
    @Override
    public String executeCommand(List<String> args) {
        System.exit(0);
        return null;
    }
}

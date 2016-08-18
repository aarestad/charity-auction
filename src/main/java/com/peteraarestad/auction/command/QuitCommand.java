package com.peteraarestad.auction.command;

import java.util.List;

/**
 * A Command that will exit the application immediately. Does not return normally.
 */
public class QuitCommand implements Command {
    @Override
    public String execute(List<String> args) {
        System.exit(0);
        return null;
    }
}

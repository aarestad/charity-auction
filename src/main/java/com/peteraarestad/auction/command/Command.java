package com.peteraarestad.auction.command;

import java.util.List;

/**
 * A Command. Used with the CommandRouter to determine which command to run when the user enters a command
 * on the command line.
 */
interface Command {
    /**
     * Execute this command and return a response
     *
     * @param args the arguments to pass to the command
     * @return the response as a String
     */
    String execute(List<String> args);
}

package com.peteraarestad.auction.command;

import java.util.List;

interface Command {
    String executeCommand(List<String> args);
}

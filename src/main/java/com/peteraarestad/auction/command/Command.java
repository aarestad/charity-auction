package com.peteraarestad.auction.command;

import java.util.List;

interface Command {
    String execute(List<String> args);
}

package com.peteraarestad.auction.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

public class CommandRouter {
    private final Map<String, Command> router = new HashMap<>();

    public void addRoute(String commandPattern, Command command) {
        router.put(commandPattern, command);
    }

    public String parseAndExecuteCommand(String command) {
        for (Map.Entry<String, Command> commandEntry : router.entrySet()) {
            Matcher matcher = Pattern.compile(commandEntry.getKey()).matcher(command);

            if (matcher.matches()) {
                List<String> arguments = newArrayList();

                for (int i = 1; i <= matcher.groupCount(); ++i) {
                    arguments.add(matcher.group(i));
                }

                return commandEntry.getValue().executeCommand(arguments);
            }
        }

        return "Invalid Command: " + command;
    }
}

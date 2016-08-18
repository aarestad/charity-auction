package com.peteraarestad.auction.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Lists.newArrayList;

/**
 * The command router. Commands are mapped using Java regular expressions using addRoute(), and executed using
 * parseAndExecuteCommand().
 */
public class CommandRouter {
    private final Map<String, Command> router = new HashMap<>();

    /**
     * Adds a command routing. Note that the first command found matching the input string will be run - if an input
     * could match multiple patterns, the chosen command will be arbitrary.
     *
     * @param commandPattern a regular expression pattern
     * @param command the Command implementation to run for this pattern
     */
    public void addRoute(String commandPattern, Command command) {
        router.put(commandPattern, command);
    }

    /**
     * Given a command input, find a Command, run it, and return its response. If a matching command was not found,
     * return an error message. Arguments are pulled out of the command input using regex groups in the pattern.
     *
     * @param commandInput the input
     * @return the result of the command or an error message
     */
    public String parseAndExecuteCommand(String commandInput) {
        for (Map.Entry<String, Command> commandEntry : router.entrySet()) {
            Matcher matcher = Pattern.compile(commandEntry.getKey()).matcher(commandInput);

            if (matcher.matches()) {
                List<String> arguments = newArrayList();

                for (int i = 1; i <= matcher.groupCount(); ++i) {
                    arguments.add(matcher.group(i));
                }

                return commandEntry.getValue().execute(arguments);
            }
        }

        return "Invalid Command: " + commandInput;
    }
}

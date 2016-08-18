package com.peteraarestad.auction.command;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommandRouterTest {
    @Mock Command mockCommand;

    @Test
    public void addCommandsAndParse() throws Exception {
        CommandRouter commandRouter = new CommandRouter();

        commandRouter.addRoute("^a$", args -> "this is command a");
        commandRouter.addRoute("^b$", args -> "this is command b");
        commandRouter.addRoute("^z (\\d+) (\\d+)$", mockCommand);

        assertThat(commandRouter.parseAndExecuteCommand("a"), is("this is command a"));
        assertThat(commandRouter.parseAndExecuteCommand("b"), is("this is command b"));
        assertThat(commandRouter.parseAndExecuteCommand("c"), is("Invalid Command: c"));
        assertThat(commandRouter.parseAndExecuteCommand("aa"), is("Invalid Command: aa"));
        assertThat(commandRouter.parseAndExecuteCommand(""), is("Invalid Command: "));

        commandRouter.parseAndExecuteCommand("z 123 345");
        verify(mockCommand).execute(newArrayList("123", "345"));
    }

    @Test(expected=NullPointerException.class)
    public void parseNull() {
        CommandRouter commandRouter = new CommandRouter();

        commandRouter.parseAndExecuteCommand(null);
    }
}
package com.example.university;

import com.example.university.commands.Command;
import com.example.university.commands.CommandManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class CommandManagerTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getParameter("userId")).thenReturn("1");

    }

    @Test
    public void shouldGetValidCommand() {
        Command c = CommandManager.get("viewApplicant");
        Assert.assertNotNull(c);
    }

    @Test
    public void shouldReturnAllCommands() {
        for (Map.Entry<String, Command> e : CommandManager.getAllCommands().entrySet()) {
            Assert.assertNotNull(e.getKey());
        }
    }
}

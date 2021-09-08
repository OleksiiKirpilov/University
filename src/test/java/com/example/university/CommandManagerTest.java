package com.example.university;

import com.example.university.commands.Command;
import com.example.university.commands.CommandManager;
import com.example.university.db.DbManager;
import com.example.university.utils.RequestType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class CommandManagerTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpSession session;

    RequestType requestType;


    @Before
    public void setUp() throws Exception {
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
    public void shouldReturnAllCommands() throws ServletException, IOException {
        for (Map.Entry<String, Command> e : CommandManager.getAllCommands().entrySet()) {
            String c = e.getKey();
        }
    }
}

package com.example.university;

import com.example.university.filters.AuthFilter;
import com.example.university.filters.EncodingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;

public class FiltersTest {

    EncodingFilter encFilter;
    AuthFilter authFilter;

    @Mock
    HttpSession session;

    @Mock
    FilterChain chain;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterConfig encFilterConfig;

    @Mock
    FilterConfig authFilterConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        encFilter = new EncodingFilter();
        authFilter = new AuthFilter();
        encFilter.init(encFilterConfig);
        authFilter.init(authFilterConfig);
    }

    @After
    public void tearDown() {
        encFilter.destroy();
        authFilter.destroy();
    }

    @Test
    public void encFilterShouldSetProperEncoding()
            throws ServletException, IOException {
        when(request.getCharacterEncoding()).thenReturn("UTF-8");
        encFilter.doFilter(request, response, chain);
        assertEquals("UTF-8", request.getCharacterEncoding());
    }

    @Test
    public void authFilterShouldAllowViewFaculties() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewAllFaculties");
        when(request.getAttribute("faculties")).thenReturn(true);
        authFilter.doFilter(request, response, chain);
        assertNotNull(request.getAttribute("faculties"));
    }

    @Test
    public void authFilterShouldNotAllowEditSubjects() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("editSubject");
        authFilter.doFilter(request, response, chain);
        assertEquals("editSubject", request.getParameter("command"));
    }

    @Test
    public void authFilterShouldViewProfileForUser() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute("userRole")).thenReturn("user");
        when(session.getAttribute("user")).thenReturn("user");
        when(request.getParameter("command")).thenReturn("viewProfile");
        authFilter.doFilter(request, response, chain);
        assertEquals("viewProfile", request.getParameter("command"));
    }

    @Test
    public void authFilterShouldAllowEditSubjectsForAdmin() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute("userRole")).thenReturn("admin");
        when(session.getAttribute("user")).thenReturn("admin");
        when(request.getParameter("command")).thenReturn("editSubject");
        authFilter.doFilter(request, response, chain);
        assertEquals("editSubject", request.getParameter("command"));
    }

}

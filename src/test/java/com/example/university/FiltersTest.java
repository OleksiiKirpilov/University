package com.example.university;

import com.example.university.filters.AuthFilter;
import com.example.university.filters.EncodingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeast;

public class FiltersTest {

    EncodingFilter encFilter;
    AuthFilter authFilter;
    HttpSession session;
    FilterChain chain;
    HttpServletRequest request;
    HttpServletResponse response;
    FilterConfig encFilterConfig;
    FilterConfig authFilterConfig;

    @Before
    public void setUp() throws Exception {
        session = mock(HttpSession.class);
        chain = mock(FilterChain.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        encFilterConfig = mock(FilterConfig.class);
        authFilterConfig = mock(FilterConfig.class);

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
        when(request.getCharacterEncoding()).thenReturn(null);
        encFilter.doFilter(request, response, chain);
        verify(request, times(1)).setCharacterEncoding(anyString());
    }

    @Test
    public void authFilterShouldAllowViewFaculties() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewAllFaculties");
        when(request.getAttribute("faculties")).thenReturn(true);
        authFilter.doFilter(request, response, chain);
        verify(chain, atLeast(1)).doFilter(request, response);
    }

    @Test
    public void authFilterShouldNotAllowEditSubjects() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("editSubject");
        authFilter.doFilter(request, response, chain);
        verify(response, atLeast(1)).sendError(anyInt());
    }

    @Test
    public void authFilterShouldViewProfileForUser() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute("userRole")).thenReturn("user");
        when(session.getAttribute("user")).thenReturn("user");
        when(request.getParameter("command")).thenReturn("viewProfile");
        authFilter.doFilter(request, response, chain);
        verify(chain, atLeast(1)).doFilter(request, response);
    }

    @Test
    public void authFilterShouldAllowEditSubjectsForAdmin() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute("userRole")).thenReturn("admin");
        when(session.getAttribute("user")).thenReturn("admin");
        when(request.getParameter("command")).thenReturn("editSubject");
        authFilter.doFilter(request, response, chain);
        verify(chain, atLeast(1)).doFilter(request, response);
    }

}

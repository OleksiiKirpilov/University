package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.UserDao;
import com.example.university.entities.Applicant;
import com.example.university.entities.Faculty;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import com.example.university.utils.Fields;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class CommandsTest {

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    RequestDispatcher dispatcher;
    User user;
    User admin;
    Applicant applicant;
    Faculty faculty;
    String email = "invalid_email@";
    String email2 = "invalid_email_2@";
    String invalidAdmin = "invalid_admin";
    String invalidFaculty = "INVALID_FACULTY";

    @Before
    public void setUp() throws ServletException, IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        user = new User(email, "xx", "ff", "ll", Role.USER, "en");
        UserDao userDao = new UserDao();
        userDao.create(user);

        admin = new User(email2, invalidAdmin, invalidAdmin,
                invalidAdmin, Role.ADMIN, "en");
        userDao.create(admin);

        String geo = "INVALIDGEO";
        applicant = new Applicant(geo, geo, geo, user);
        ApplicantDao applicantDao = new ApplicantDao();
        applicantDao.create(applicant);

        faculty = new Faculty(invalidFaculty, invalidFaculty, 1, 2);
        new FacultyDao().create(faculty);

        when(request.getSession()).thenReturn(session);
        when(request.getSession(anyBoolean())).thenReturn(session);
        when(session.getAttribute("userRole")).thenReturn(null);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        doNothing().when(dispatcher).forward(request, response);
        doNothing().when(request).setAttribute(anyString(), anyObject());
        when(request.getParameter(Fields.FACULTY_NAME_EN)).thenReturn(faculty.getNameEn());

        when(request.getParameter(Fields.APPLICANT_SCHOOL)).thenReturn(applicant.getSchool());
        when(request.getParameter(Fields.APPLICANT_DISTRICT)).thenReturn(applicant.getDistrict());
        when(request.getParameter(Fields.APPLICANT_CITY)).thenReturn(applicant.getCity());
        when(request.getParameter(Fields.APPLICANT_IS_BLOCKED))
                .thenReturn(String.valueOf(applicant.getBlockedStatus()));
    }

    @After
    public void tearDown() {
        new ApplicantDao().delete(applicant);
        new UserDao().delete(user);
        new UserDao().delete(admin);
        new FacultyDao().delete(faculty);
    }

    @Test
    public void testNoCommand() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("invalid_command");
        new FrontController().doGet(request, response);
    }

    @Test
    public void testLogin() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("login");
        new FrontController().doGet(request, response);
        new FrontController().doPost(request, response);
    }

    @Test
    public void testLogout() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("logout");
        new FrontController().doGet(request, response);
    }

    @Test
    public void testViewProfile() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewProfile");
        when(session.getAttribute("user")).thenReturn(user.getEmail());
        new FrontController().doGet(request, response);
    }

    @Test
    public void testEditProfile() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("editProfile");
        when(session.getAttribute("user")).thenReturn(user.getEmail());
        when(session.getAttribute("userRole")).thenReturn(user.getRole());
        new FrontController().doGet(request, response);

        when(request.getParameter("oldMail")).thenReturn(user.getEmail());
        when(request.getParameter("oldEmail")).thenReturn(user.getEmail());
        when(request.getParameter(Fields.USER_FIRST_NAME)).thenReturn(user.getFirstName());
        when(request.getParameter(Fields.USER_LAST_NAME)).thenReturn(user.getLastName());
        when(request.getParameter("email")).thenReturn(user.getEmail());
        when(request.getParameter("password")).thenReturn(user.getPassword());
        when(request.getParameter("lang")).thenReturn(user.getLang());
        new FrontController().doPost(request, response);

        when(request.getParameter("password")).thenReturn("qqqqQQQQ");
        new FrontController().doPost(request, response);

    }

    @Test
    public void testViewFaculty() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewFaculty");
        when(session.getAttribute("user")).thenReturn(admin.getEmail());
        when(session.getAttribute("userRole")).thenReturn(admin.getRole());
        when(session.getAttribute("name_en")).thenReturn(faculty.getNameEn());
        new FrontController().doGet(request, response);
    }






    @Test
    public void testViewAllFaculties() throws Exception {
        when(request.getParameter("command")).thenReturn("viewAllFaculties");
//        when(request.getParameter("password")).thenReturn("secret");
//        StringWriter stringWriter = new StringWriter();
//        PrintWriter writer = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(writer);
        new FrontController().doGet(request, response);
//        verify(request, atLeast(1)).getParameter("username");
//        writer.flush(); // it may not have been flushed yet...
//        assertTrue(stringWriter.toString().contains("list.jsp"));
    }

}
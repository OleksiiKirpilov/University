package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.SubjectDao;
import com.example.university.db.UserDao;
import com.example.university.entities.*;
import com.example.university.utils.Fields;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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
    String email = "invalidemail@";
    String email2 = "invalidemail_2@";
    String invalidAdmin = "invalidadmin";
    String invalidFacultyEn = "INVALID FACULTY";
    String invalidFacultyRu = "НЕВАЛИДНЫй ФАКУЛЬТЕТ";
//    StringWriter stringWriter;
//    PrintWriter printWriter;

    @Before
    public void setUp() throws ServletException, IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        user = new User(email, "xx", "ff", "ll", Role.USER, "en");
        UserDao userDao = new UserDao();
        userDao.create(user);

        admin = new User(email2, invalidAdmin, invalidAdmin, invalidAdmin, Role.ADMIN, "en");
        userDao.create(admin);

        String geo = "INVALIDGEO";
        applicant = new Applicant(geo, geo, geo, user);
        ApplicantDao applicantDao = new ApplicantDao();
        applicantDao.create(applicant);

        faculty = new Faculty(invalidFacultyRu, invalidFacultyEn, 1, 2);
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

//        stringWriter = new StringWriter();
//        printWriter = new PrintWriter(stringWriter);
//        when(response.getWriter()).thenReturn(printWriter);

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
        verify(response, times(0)).sendRedirect(anyString());
    }

    @Test
    public void testLogin() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("login");
        new FrontController().doGet(request, response);
        new FrontController().doPost(request, response);
        verify(response, times(2)).sendRedirect(anyString());
    }

    @Test
    public void testLogout() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("logout");
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());

    }

    @Test
    public void testViewProfile() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewProfile");
        when(session.getAttribute("user")).thenReturn(user.getEmail());
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
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
        verify(response, times(2)).sendRedirect(anyString());
    }

    @Test
    public void testViewFaculty() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewFaculty");
        when(session.getAttribute("user")).thenReturn(admin.getEmail());
        when(session.getAttribute("userRole")).thenReturn(admin.getRole());
        when(session.getAttribute("name_en")).thenReturn(faculty.getNameEn());
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }

    @Test
    public void testEditFaculty() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("editFaculty");
        when(request.getParameter(Fields.FACULTY_NAME_RU)).thenReturn(faculty.getNameRu());
        when(request.getParameter(Fields.FACULTY_NAME_EN)).thenReturn(faculty.getNameEn());
        when(request.getParameter(Fields.FACULTY_TOTAL_PLACES))
                .thenReturn(String.valueOf(faculty.getTotalPlaces()));
        when(request.getParameter(Fields.FACULTY_BUDGET_PLACES))
                .thenReturn(String.valueOf(faculty.getBudgetPlaces()));
        when(request.getParameter("oldName")).thenReturn(faculty.getNameEn());

        new FrontController().doGet(request, response);
        new FrontController().doPost(request, response);
        verify(response, times(1)).sendRedirect(anyString());
    }

    @Test
    public void testViewAllSubjects() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewAllSubjects");
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }

    @Test
    public void testViewSubject() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewSubject");
        SubjectDao subjectDao = new SubjectDao();
        Subject s = subjectDao.find(1);
        when(request.getParameter(Fields.SUBJECT_NAME_EN)).thenReturn(s.getNameEn());
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }

    @Test
    public void testViewApplicant() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("viewApplicant");
        when(request.getParameter("userId")).thenReturn(String.valueOf(user.getId()));
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }


    @Test
    public void testSetSessionLanguage() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("setSessionLanguage");
        new FrontController().doGet(request, response);
        verify(response, times(1)).sendRedirect(anyString());
    }

    @Test
    public void testCreateReport() throws ServletException, IOException {
        when(request.getParameter("command")).thenReturn("createReport");
        when(request.getParameter(Fields.ENTITY_ID)).thenReturn("3");
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }


    @Test
    public void testViewAllFaculties() throws Exception {
        when(request.getParameter("command")).thenReturn("viewAllFaculties");
        new FrontController().doGet(request, response);
        verify(response, times(0)).sendRedirect(anyString());
        verify(request, times(1)).getRequestDispatcher(any());
    }

}
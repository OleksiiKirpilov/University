package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyApplicantsDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.UserDao;
import com.example.university.entities.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FacultyApplicantsDaoTest {

    String email = "invalid@invalid.invalid2";
    User user = new User(email, email, "name", "surname", Role.USER, "en");
    Applicant applicant;
    Faculty faculty = new Faculty("ТЕСТОВЫЙФАКУЛЬТЕТ2", "TESTFACULTY2", 1, 2);
    FacultyApplicants fa;

    @Before
    public void setUp() {
        UserDao userDao = new UserDao();
        userDao.create(user);
        user = userDao.find(email);
        applicant = new Applicant("city", "district", "school", user);
        new ApplicantDao().create(applicant);
        new FacultyDao().create(faculty);
        fa = new FacultyApplicants(faculty, applicant);
        new FacultyApplicantsDao().create(fa);
    }

    @After
    public void tearDown() {
        new ApplicantDao().delete(applicant);
        new UserDao().delete(user);
        new FacultyDao().delete(faculty);
        new FacultyApplicantsDao().delete(fa);
    }
    @Test
    public void shouldFindAll() {
        Assert.assertTrue(new FacultyApplicantsDao().findAll().size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        FacultyApplicantsDao facultyApplicantsDao = new FacultyApplicantsDao();
        fa = facultyApplicantsDao.find(fa);
        Assert.assertNotNull(fa);
        facultyApplicantsDao.delete(fa);
        fa = facultyApplicantsDao.find(fa.getId());
        Assert.assertNull(fa);
        fa = new FacultyApplicants(faculty, applicant);
        facultyApplicantsDao.create(fa);
    }

}

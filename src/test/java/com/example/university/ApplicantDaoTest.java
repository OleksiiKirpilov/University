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

public class ApplicantDaoTest {

    String email = "invalid@invalid.invalid";
    User user = new User(email, email, "name", "surname", Role.USER, "en");
    Applicant applicant;
    Faculty faculty = new Faculty("ТЕСТОВЫЙФАКУЛЬТЕТ", "TESTFACULTY", 1, 2);
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
    public void shouldFindAllApplicants() {
        Assert.assertTrue(new ApplicantDao().findAll().size() > 0);
    }

    @Test
    public void shouldFindAllFacultyApplicants() {
        Assert.assertTrue(new ApplicantDao().findAllFacultyApplicants(faculty).size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        ApplicantDao applicantDao = new ApplicantDao();
        Assert.assertNotNull(applicant);
        applicant = applicantDao.find(user);
        Assert.assertNotNull(applicant);
        applicant.setBlockedStatus(true);
        applicantDao.update(applicant);
        applicant = applicantDao.find(applicant.getId());
        Assert.assertNotNull(applicant);
    }
}

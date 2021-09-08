package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyApplicantsDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.UserDao;
import com.example.university.entities.*;
import org.junit.Assert;
import org.junit.Test;

public class FacultyApplicantsDaoTest {

    @Test
    public void shouldFindAll() {
        Assert.assertTrue(new FacultyApplicantsDao().findAll().size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        String name = "TEST_FACULTY";
        Faculty f = new Faculty(name, name, 1, 10);
        FacultyDao facultyDao = new FacultyDao();
        facultyDao.create(f);
        String email = "invalid@invalid.invalid";
        UserDao userDao = new UserDao();
        ApplicantDao applicantDao = new ApplicantDao();
        User u = new User(email, email, "name", "surname", Role.USER, "en");
        userDao.create(u);
        Applicant a = new Applicant("city", "district", "school", u);
        applicantDao.create(a);
        FacultyApplicants fa = new FacultyApplicants(f, a);
        FacultyApplicantsDao facultyApplicantsDao = new FacultyApplicantsDao();
        facultyApplicantsDao.create(fa);
        Assert.assertNotNull(fa);
        fa = facultyApplicantsDao.find(fa);
        Assert.assertNotNull(fa);
        facultyApplicantsDao.delete(fa);
        fa = facultyApplicantsDao.find(fa.getId());
        Assert.assertNull(fa);
        applicantDao.delete(a);
        facultyDao.delete(f);
    }

}

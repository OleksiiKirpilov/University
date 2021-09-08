package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyDao;
import com.example.university.db.UserDao;
import com.example.university.entities.Applicant;
import com.example.university.entities.Faculty;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import org.junit.Assert;
import org.junit.Test;

public class ApplicantDaoTest {

    @Test
    public void shouldFindAllApplicants() {
        Assert.assertTrue(new ApplicantDao().findAll().size() > 0);
    }

    @Test
    public void shouldfindAllFacultyApplicants() {
        Faculty f = new FacultyDao().find(2);
        Assert.assertTrue(new ApplicantDao().findAllFacultyApplicants(f).size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        String email = "invalid@invalid.invalid";
        UserDao userDao = new UserDao();
        ApplicantDao applicantDao = new ApplicantDao();
        User u = new User(email, email, "name", "surname", Role.USER, "en");
        userDao.create(u);
        Applicant a = new Applicant("city", "district", "school", u);
        applicantDao.create(a);
        Assert.assertNotNull(a);
        a = applicantDao.find(u);
        Assert.assertNotNull(a);
        a.setBlockedStatus(true);
        applicantDao.update(a);
        a = applicantDao.find(a.getId());
        Assert.assertNotNull(a);
        applicantDao.delete(a);
        userDao.delete(u);
    }
}

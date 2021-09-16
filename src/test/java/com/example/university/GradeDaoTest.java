package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.GradeDao;
import com.example.university.db.UserDao;
import com.example.university.entities.Applicant;
import com.example.university.entities.Grade;
import com.example.university.entities.Role;
import com.example.university.entities.User;
import org.junit.Assert;
import org.junit.Test;

public class GradeDaoTest {

    @Test
    public void shouldFindAllGrades() {
        Assert.assertTrue(new GradeDao().findAll().size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        String email = "invalid@invalid.invalid5";
        ApplicantDao applicantDao = new ApplicantDao();
        UserDao userDao = new UserDao();
        User u = new User(email, email, "name", "surname", Role.USER, "en");
        userDao.create(u);
        Applicant a = new Applicant("city", "district", "school", u);
        applicantDao.create(a);
        Grade g = new Grade(1, a.getId(), 11, "diploma");
        GradeDao gradeDao = new GradeDao();
        gradeDao.create(g);
        Assert.assertTrue(g.getId() > 0);
        g.setGrade(12);
        gradeDao.update(g);
        g = gradeDao.find(g.getId());
        Assert.assertEquals(12, g.getGrade());
        gradeDao.delete(g);
        applicantDao.delete(a);
        userDao.delete(u);
    }
}

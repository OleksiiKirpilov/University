package com.example.university;

import com.example.university.db.FacultyDao;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Faculty;
import com.example.university.entities.Subject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SubjectDaoTest {

    @Before
    public void setUp() {
    }

    @Test
    public void shouldFindAllSubjects() {
        Assert.assertTrue(new SubjectDao().findAll().size() > 0);
        FacultyDao facultyDao = new FacultyDao();
        Faculty f = facultyDao.find(1);
        Assert.assertTrue(new SubjectDao().findAllFacultySubjects(f).size() > 0);
        Assert.assertTrue(new SubjectDao().findAllNotFacultySubjects(f).size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        SubjectDao subjectDao = new SubjectDao();
        String name = "TEST_SUBJECT";
        Subject s = new Subject(name, name);
        subjectDao.create(s);
        Assert.assertTrue(s.getId() > 0);
        s = subjectDao.find(s.getId());
        Assert.assertNotNull(s);
        s.setNameEn(name + name);
        subjectDao.update(s);
        s = subjectDao.find(s.getNameEn());
        Assert.assertNotNull(s);
        subjectDao.delete(s);
        s = subjectDao.find(s.getId());
        Assert.assertNull(s);
    }


}

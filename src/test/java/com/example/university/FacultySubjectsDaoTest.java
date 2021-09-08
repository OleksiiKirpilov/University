package com.example.university;

import com.example.university.db.FacultyDao;
import com.example.university.db.FacultySubjectsDao;
import com.example.university.db.SubjectDao;
import com.example.university.entities.Faculty;
import com.example.university.entities.FacultySubjects;
import com.example.university.entities.Subject;
import org.junit.Assert;
import org.junit.Test;

public class FacultySubjectsDaoTest {

    @Test
    public void shouldFindAll() {
        Assert.assertTrue(new FacultySubjectsDao().findAll().size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        String name = "TEST_FACULTY";
        Faculty f = new Faculty(name, name, 1, 10);
        FacultyDao facultyDao = new FacultyDao();
        facultyDao.create(f);
        SubjectDao subjectDao = new SubjectDao();
        String subjectName = "TEST_SUBJECT";
        Subject s = new Subject(name, name);
        subjectDao.create(s);
        FacultySubjectsDao facultySubjectsDao = new FacultySubjectsDao();
        FacultySubjects fs = new FacultySubjects(s, f);
        facultySubjectsDao.create(fs);
        fs = facultySubjectsDao.find(fs.getId());
        Assert.assertNotNull(fs);
        facultySubjectsDao.delete(fs);
        fs = facultySubjectsDao.find(fs.getId());
        Assert.assertNull(fs);
        facultySubjectsDao.deleteAllSubjects(f);
        Assert.assertNull(fs);
        subjectDao.delete(s);
        facultyDao.delete(f);
    }
}

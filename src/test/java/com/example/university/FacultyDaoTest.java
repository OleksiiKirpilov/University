package com.example.university;

import com.example.university.db.ApplicantDao;
import com.example.university.db.FacultyDao;
import com.example.university.entities.Faculty;
import org.junit.Assert;
import org.junit.Test;

public class FacultyDaoTest {

    @Test
    public void shouldFindAllFaculties() {
        Assert.assertTrue(new FacultyDao().findAll().size() > 0);
    }

    @Test
    public void shouldDoCrud() {
        String name = "TEST_FACULTY3";
        Faculty f = new Faculty(name, name, 1, 10);
        FacultyDao facultyDao = new FacultyDao();
        facultyDao.create(f);
        f = facultyDao.find(f.getId());
        Assert.assertNotNull(f);
        f.setTotalPlaces(5);
        facultyDao.update(f);
        f = facultyDao.find(f.getNameEn());
        Assert.assertNotNull(f);
        facultyDao.delete(f);
    }

}

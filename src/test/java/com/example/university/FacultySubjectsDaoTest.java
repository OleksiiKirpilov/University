package com.example.university;

import com.example.university.db.FacultyDao;
import com.example.university.db.FacultySubjectsDao;
import org.junit.Assert;
import org.junit.Test;

public class FacultySubjectsDaoTest {

    @Test
    public void shouldFindAll() {
        Assert.assertTrue(new FacultySubjectsDao().findAll().size() > 0);
    }
}

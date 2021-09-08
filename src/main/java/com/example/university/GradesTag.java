package com.example.university;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom tag class which needed in apply for faculty user form. The main
 * purpose of this class is remove the need of validation in ApplyFacultyView
 * command.
 * The maximum mark in subject is equal to 12, the lower one is zero.
 */
public class GradesTag extends SimpleTagSupport {

    private static final List<Integer> grades = new ArrayList<>();

    private int subjectId;
    private String examType;

    static {
        for (int i = 0; i <= 12; i++) {
            grades.add(i);
        }
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        out.println("<select name=\"" + subjectId + "_" + examType + "\">");
        for (Integer grade : grades) {
            out.println("<option>" + grade + "</option>");
        }
        out.println("</select>");
    }

}

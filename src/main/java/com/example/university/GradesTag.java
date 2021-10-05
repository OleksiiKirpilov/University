package com.example.university;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Custom tag class which needed in apply for faculty user form.
 * The maximum grade in subject is equal to 12, the lower one is zero.
 */
public class GradesTag extends SimpleTagSupport {

    private static final List<Integer> grades =
            IntStream.rangeClosed(0, 12).boxed().collect(Collectors.toList());

    private int subjectId;
    private String examType;


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
    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.println("<select style=\"width: 40px;\" name=\"" + subjectId + "_" + examType + "\">");
        for (Integer grade : grades) {
            out.println("<option>" + grade + "</option>");
        }
        out.println("</select>");
    }

}

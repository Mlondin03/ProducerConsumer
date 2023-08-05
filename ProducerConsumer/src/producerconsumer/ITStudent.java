/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producerconsumer;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author User
 */



public class ITStudent {

    private String name;
    private String studentId;
    private List<String> programme;
    private int[] courses;
    private List<Integer> marks;
    private double average;
    private boolean passed;

    public ITStudent(String name, String studentId, List<String> programme, int[] courses, List<Integer> marks, boolean passed) {
        this.name = name;
        this.studentId = studentId;
        this.programme = programme;
        this.courses = courses;
        this.marks = marks;
        this.average = calculateAverage();
        this.passed = passed;
    }

    public double calculateAverage() {
        double totalMarks = 0;
        for (Integer mark : marks) {
          totalMarks += mark;
        }
        return totalMarks / marks.size();
    }

    private boolean isPassed() {
        return average >= 50;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
     this.name = name;
    }


    public String getStudentId() {
        return studentId;
    }
    
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    
    public List<String> getProgramme() {
        return programme;
    }
    
    public void setProgramme(List<String> programme) {
        this.programme = programme;
    }
        
    public int[] getCourses() {
        return courses ;
    }
    
     public void setCourses(int[] courses) {
        this.courses = courses;
    }
    public List<Integer> getMarks() {
        return marks;
    }
    
    public void setMarks(List<Integer> marks) {
        this.marks = marks;
    }

    public double getAverage() {
        return average;
    }
    
    public void setAverage(double average) {
    this.average = average;
    }
    
    public void setPassed(boolean passed) {
    this.passed = passed;
    }

    @Override
    public String toString() {
        return "ITStudent{" +
                "name='" + name + '\'' +
                ", studentId=" + studentId +
                ", programme='" + programme + '\'' +
                ", courses=" + courses+
                ", marks=" + marks+
                ", average=" + average +
                ", passed=" + passed +
                '}';
    }

   
    public static ITStudent fromXML(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        // Get the root element of the XML document.
        org.w3c.dom.Element rootElement = document.getDocumentElement();

        // Get the name of the student.
        String name = rootElement.getElementsByTagName("name").item(0).getTextContent();

        // Get the student ID.
        String studentId = rootElement.getElementsByTagName("studentId").item(0).getTextContent();

        // Get the programme of study.
        NodeList programmeNodeList = rootElement.getElementsByTagName("programme");
        List<String> programmes = new ArrayList<>();
        for (int i = 0; i < programmeNodeList.getLength(); i++) {
          Node programmeElement = programmeNodeList.item(i);
          String programme = programmeElement.getTextContent();
          String programmeValue = programme.trim();
          programmes.add(programmeValue);
        }
        
        // Get the courses.
        int[] courses = Arrays.stream(rootElement.getElementsByTagName("courses").item(0).getTextContent().split(",")).mapToInt(Integer::parseInt).toArray();  
        
        // Get the marks.
        NodeList marksNodeList = rootElement.getElementsByTagName("marks");
        List<Integer> marks = new ArrayList<>();
        for (int i = 0; i < marksNodeList.getLength(); i++) {
          Node markElement =  marksNodeList.item(i);
           String mark = markElement.getTextContent();
           int markValue = Integer.parseInt(mark);
           marks.add(markValue);
        }

        // Get average mark.
        String average = rootElement.getElementsByTagName("average").item(0).getTextContent();

        // Get the result status.
        String passed = rootElement.getElementsByTagName("passed").item(0).getTextContent();

        ITStudent student = new ITStudent(name, studentId, programmes, courses, marks, Boolean.parseBoolean(passed));
        student.setName(name);
        student.setStudentId(studentId);
        student.setProgramme(programmes);
        student.setCourses(courses);
        student.setMarks(marks);
        student.setAverage(Double.parseDouble(average));
        student.setPassed(Boolean.parseBoolean(passed));

        return student;
    }
    
   
    
        public String toXML() {
            StringBuilder xml = new StringBuilder();
            xml.append("<ITStudent>");
            xml.append("<name>").append(name).append("</name>");
            xml.append("<studentId>").append(studentId).append("</studentId>");
            xml.append("<programme>").append(programme).append("</programme>");
            xml.append("<courses>");
            for (Integer course : courses) {
              xml.append("<course>").append(course).append("</course>");
            }
            xml.append("</courses>");
            xml.append("<marks>");
            for (Integer mark : marks) {
              xml.append("<mark>").append(mark).append("</mark>");
            }
            xml.append("</marks>");
            xml.append("<average>").append(average).append("</average>");
            xml.append("<passed>").append(passed).append("</passed>");
            xml.append("</ITStudent>");
            return xml.toString();
      }
}

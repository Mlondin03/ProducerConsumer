/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producerconsumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author User
 */

public class ProducerConsumer {

    /**
     * @param args the command line arguments
     */
    
   private static final int BUFFER_SIZE = 10;
    private static Semaphore full = new Semaphore(0);
    private static Semaphore empty = new Semaphore(BUFFER_SIZE);

    public static void main(String[] args) {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();
    }

    
    static class Producer extends Thread {
    @Override
    public void run() {
        int intArray[] = {70,80,90};
        Random rand = new Random();
        for (int i = 1; i <= 10; i++) {
            try {
                int[] marksArr = new int[3];
                for(int x = 0; x < marksArr.length; x++)
                    marksArr[x] = rand.nextInt(100);
                empty.acquire();
                // Generate student information
                List<String> courseList = Arrays.asList("Computer Security", "Programming", "Database");
                List<Integer> markList = Arrays.asList(marksArr[0], marksArr[1], marksArr[2]);
                ITStudent student = new ITStudent("Student " + i, "2020002" + i, courseList, intArray, markList, true);
                // Wrap student information into XML format
                String xml = "<student>" + student.toXML() + "</student>";
                // Save XML file to directory
                File file = new File("student" + i + ".xml");
                FileWriter writer = new FileWriter(file);
                writer.write(xml);
                writer.close();
                // Insert file name to buffer
                full.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch (FileNotFoundException e) {
                System.out.println("File not found");
            }
            catch (IOException e) {
                System.out.println("Error reading file");
            }
        }
    }
}

    public static String readXMLFile(int fileNumber) throws FileNotFoundException, IOException{

    // Create a new XML file object
    File file = new File("student" + fileNumber + ".xml");

    // Create a new BufferedReader object
    BufferedReader reader = new BufferedReader(new FileReader(file));

    // Read the contents of the XML file
    String xml = "";
    String line;
    try {
    while ((line = reader.readLine()) != null) {
        xml += line;
    }
    } catch (FileNotFoundException e) {
            System.out.println("File not found");
             } catch (IOException e) {
            System.out.println("Error reading file");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return xml;
}
    
    static class Consumer extends Thread {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                
                try {
                    
                                        
                    full.acquire();
                    // Remove file name from buffer
                    int fileNumber = empty.availablePermits();
                    // Read XML file from directory
                    String xml = readXMLFile(i);
                    
                    // Unwrap XML file and gather student information into ITStudent class
                    ITStudent student = ITStudent.fromXML(xml);
                    // Calculate average mark and determine pass/fail
                    double average = student.getAverage();
                    String passFail = average >= 50 ? "Passed" : "Failed";
                    // Print student information
                    System.out.println(student.getName() + ", " + student.getStudentId() + ", " + student.getProgramme() + ", " + student.getMarks()+ ", " + average + ", " + passFail);
                    //System.out.println(student.toString());
                
               } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (FileNotFoundException e) {
                    System.out.println("File not found");
                    //System.out.println("The file " + fileNumber + " does not exist");
                }
                catch (IOException e) {
                   System.out.println("Error reading file");
                } catch (Exception ex) {
                    Logger.getLogger(ProducerConsumer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}


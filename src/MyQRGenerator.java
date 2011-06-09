package gilbert.marty.util.qrcode;

import com.google.zxing.qrcode.encoder.*;
import com.google.zxing.qrcode.*;
import com.google.zxing.qrcode.decoder.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.*;
import com.google.zxing.*;
import java.io.*;
import java.util.*;
import au.com.bytecode.opencsv.*;


/*
* CSV file format for contacts -- no headers needed!
* firstname,lastname,institution,email,worknum
* firstname,lastname,institution,email,worknum
* firstname,lastname,institution,email,worknum
*/


public class MyQRGenerator {

    private int size = 300;

    public MyQRGenerator(){

    }

    public MyQRGenerator(int imgSize){
        size = imgSize;
    }

    public String create(String method, File inputFile, File outputDir){


        String resultString = null;

        if(!inputFile.exists()) {
            return "CSV file does not exist";
        }

        MyQRGenerator myqr = new MyQRGenerator();

        if(method.equals("contacts")){
            ArrayList<Contact> contacts = myqr.parseContactFile(inputFile);
            if(contacts == null){
               resultString = "Error parsing calendar CSV file";
            } else {
                boolean success = writeContacts(contacts, outputDir);
                if(!success) resultString = "Error writing contacts QR codes"; 
            }
        } else if (method.equals("calendar")){
            //System.out.println("parsing calendar objects");
            ArrayList<CalendarEvent> events = myqr.parseCalendarFile(inputFile);
            if(events == null){
               resultString = "Error parsing calendar CSV file";
            } else {
                boolean success = writeCalendarEvents(events, outputDir);
                if(!success) resultString = "Error writing calendar QR codes"; 
            }
        } else {
            resultString = "Method not currently supported";
        }
        return resultString;
    }

    /**
    * @param content String to be stored in the QR Code
    * @param filename Name of file (minus the .png extension -- this will be added)
    */
    private void writeQRCode(String content, String filename, String outputDirPath) throws Exception{
        QRCode qr = new QRCode();

        QRCodeWriter qrw = new QRCodeWriter();
        BitMatrix b = qrw.encode(content, BarcodeFormat.QR_CODE, size, size);

        java.awt.image.BufferedImage img = MatrixToImageWriter.toBufferedImage(b);
        javax.imageio.ImageIO.write(img, "png", new File(outputDirPath + File.separator+ filename + ".png"));
    }

    public boolean writeCalendarEvents(ArrayList<CalendarEvent> events, File outputDir){
        boolean success = true;
        for(CalendarEvent c : events){
            try{
                writeQRCode(c.toString(),
                    ((c.summary.replaceAll("\\s","")).replaceAll("'","")).substring(0,15),
                    outputDir.getPath());
             } catch (Exception e){
                success = false;
                //e.printStackTrace();
                //System.out.println(c.toString());
             }
        }
        return success;
    }

    public ArrayList<CalendarEvent> parseCalendarFile(File file){
        /**
        * CSV file format -- no headers needed!
        * SUMMARY,DTSTART,DTEND,LOCATION,DESCRIPTION
        * SUMMARY,DTSTART,DTEND,LOCATION,DESCRIPTION
        * SUMMARY,DTSTART,DTEND,LOCATION,DESCRIPTION
        * SUMMARY,DTSTART,DTEND,LOCATION,DESCRIPTION
        * etc
        *
        */

        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();

        try{
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            CSVParser parser = new CSVParser();
            
            while ( (line = br.readLine())!=null){

                //String[] tokens = line.split(","); 
                CalendarEvent c = new CalendarEvent();
                try{
                    String[] tokens = parser.parseLine(line);

                    c.summary = tokens[0];
                    c.dateStart = tokens[1];
                    c.dateEnd = tokens[2];
                    c.location = tokens[3];
                    //c.url = tokens[0];
                    c.description= tokens[4];
                } catch (Exception e){
                    System.err.println("Error processing line: " + line);
                    //don't add -- go to next line.
                    continue;
                }
    
                events.add(c);
            }

        /**
        } catch(FileNotFoundException fnfe){
            System.err.println(file + " cannot be found.");
        } catch (IOException ioe){
            ioe.printStackTrace();
        **/
        } catch (Exception e){
            events = null;
        } finally {
            try{
            br.close();
            } catch (Exception e){ e.printStackTrace();}
        }
        return events;
    }

    public boolean writeContacts(ArrayList<Contact> contacts, File outputDir){
        boolean success = true;
        for(Contact c : contacts){
            try{
                writeQRCode(c.toString(), 
                    c.lastName+"_"+c.firstName,
                    outputDir.getPath());
            } catch (Exception e){
                success = false;
            }
        }

        return success;
    }


    public ArrayList<Contact> parseContactFile(File file){
        /**
        * CSV file format:
        * firstname,lastname,institution,email,worknum
        * firstname,lastname,institution,email,worknum
        * firstname,lastname,institution,email,worknum
        * etc
        */
        FileReader fr = null;
        BufferedReader br = null;
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        try{
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;

            CSVParser parser = new CSVParser();
            
            while ( (line = br.readLine())!=null){
                String[] tokens = parser.parseLine(line);


                Contact c = new Contact();
                c.firstName = tokens[0];
                c.lastName = tokens[1];
                c.institution = tokens[2];
                c.email = tokens[3];
                c.workNum= tokens[4];
                /*
                c.address1= tokens[5];
                c.city= tokens[6];
                c.state= tokens[7];
                c.zip= tokens[8];
                */

                contacts.add(c);
            }

        } catch (Exception e){
            contacts = null;
        } finally {
            try{
            br.close();
            } catch (Exception e){}
        }

        return contacts;
    }

    class Contact{
        public String firstName;
        public String lastName;
        public String institution;
        public String email;
        public String url;
        public String workNum;
        public String address1;
        public String city;
        public String state;
        public String zip;

        public String toString(){
            String res = "MECARD:N:" +firstName + " " + lastName + ";" +
                "TEL:"      + workNum   + ";"+
                //"URL:"      + url       + ";"+
                "EMAIL:"    + email     + ";"+
                //"ADR:"      + address1 + "," + city + ", " + state + "," + zip + ";" +
                "NOTE:"     + institution + ";";
            return res;

        }
    }

    class CalendarEvent{
        public String summary;
        public String dateStart;
        public String dateEnd;
        public String location;
        public String description;
        
        public String toString(){
            String res="BEGIN:VCALENDAR"+
                "\nVERSION:2.0"+
                /*
                "\nBEGIN:VTIMEZONE"+
                "\nTZID:Eastern"+
                "\nBEGIN:STANDARD"+
                "\nDTSTART:16011104T020000"+
                "\nRRULE:FREQ=YEARLY;BYDAY=1SU;BYMONTH=11"+
                "\nTZOFFSETFROM:-0400"+
                "\nTZOFFSETTO:-0500"+
                "\nEND:STANDARD"+
                "\nBEGIN:DAYLIGHT"+
                "\nDTSTART:16010311T020000"+
                "\nRRULE:FREQ=YEARLY;BYDAY=2SU;BYMONTH=3"+
                "\nTZOFFSETFROM:-0500"+
                "\nTZOFFSETTO:-0400"+
                "\nEND:DAYLIGHT"+
                "\nEND:VTIMEZONE"+
                */
                "\nBEGIN:VEVENT" +
                "\nSUMMARY:"+ summary +
                "\nDESCRIPTION:"+ description +
                "\nLOCATION:"+ location +
                "\nDTSTART:"+ dateStart +
                "\nDTEND:"+ dateEnd +
                "\nEND:VENVENT"+
                "\nEND:VCALENDAR";
            return res;
        }
    }
}

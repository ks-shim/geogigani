package dwayne.shim.geogigani.allinone.data.service.util;

import java.io.*;

public class IndexPathUtil {

    public synchronized String getCurrentIndexPath(String indexPathFileName, String location1) throws Exception {
        File file = new File(indexPathFileName);
        if(!file.exists()) {
            file.createNewFile();
            writeALine(file, location1);
        }

        try {
            String currentLocation = readALine(file);
            if(currentLocation == null || currentLocation.trim().isEmpty()) throw new RuntimeException("Current Location is empty.");
            return currentLocation;
        } catch (Exception e) {
            writeALine(file, location1);
            return location1;
        }
    }

    public synchronized String getNextIndexPath(String indexPathFileName, String location1, String location2) throws Exception {
        File file = new File(indexPathFileName);
        String currentLocation = readALine(file);

        if (currentLocation.equals(location1)) return location2;
        else if (currentLocation.equals(location2)) return location1;
        else throw new RuntimeException(indexPathFileName + " might be expired.");
    }

    public synchronized void switchIndexPath(String indexPathFileName, String location1, String location2) throws Exception {
        File file = new File(indexPathFileName);
        String currentLocation = readALine(file);

        if (currentLocation.equals(location1)) writeALine(file, location2);
        else if (currentLocation.equals(location2)) writeALine(file, location1);
    }

    public synchronized void switchIndexPath(String indexPathFileName, String location) throws Exception {
        writeALine(new File(indexPathFileName), location);
    }

    private String readALine(File file) throws Exception {
        try (BufferedReader in = new BufferedReader(new FileReader(file))){
            return in.readLine().trim();
        }
    }

    private void writeALine(File file, String str) throws Exception {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))){
            out.write(str);
            out.flush();
        }
    }
}

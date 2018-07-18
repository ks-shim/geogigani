package dwayne.shim.geogigani.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Temp {

    public static void main(String[] args) throws Exception {
        File file = new File("D:/temp.txt");
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String upper = line.toUpperCase();
                String lower = line.replaceAll("_", "").toLowerCase();

                System.out.println("TravelDataIndexField." + upper + ".label(),");
            }
        }
    }
}

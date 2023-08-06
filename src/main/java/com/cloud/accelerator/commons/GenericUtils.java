package com.cloud.accelerator.commons;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class GenericUtils {

    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(fis);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            fis.close();
        }
        return prop;
    }

    public static String readProps(String property) throws IOException {
        Path userDir = Paths.get(System.getProperty("user.dir"));
        Properties prop = readPropertiesFile(userDir+"/src/test/resources/"+System.getProperty("profile")+"/environment.properties");
        return prop.getProperty(property);
    }

    public void gsutil() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        Properties landing = readPropertiesFile("landing-bucket");
        processBuilder.command("gsutil ls gs://" + landing);
        System.out.println("Test gs util");
    }

    public static void fake() {
        Faker faker = new Faker();
        String streetName = faker.address().streetName();
        String number = faker.address().buildingNumber();
        String city = faker.address().city();
        String country = faker.address().country();
        System.out.println(String.format("%s\n%s\n%s\n%s", number, streetName, city, country));
    }

    public static void readJson() throws IOException {
        Gson gson = new Gson();
        try {
            String folderName = Paths.get(System.getProperty("user.dir")) + "/src/test/resources/test_data/testdata.json";
            BufferedReader br = new BufferedReader(new FileReader(folderName));
            Object obj = gson.fromJson(br, Object.class);
            System.out.println(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}

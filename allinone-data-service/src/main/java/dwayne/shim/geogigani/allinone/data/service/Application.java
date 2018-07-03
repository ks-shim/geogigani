package dwayne.shim.geogigani.allinone.data.service;

import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Log4j2
@SpringBootApplication
@EnableScheduling
@PropertySource("classpath:config/allinone.properties")
public class Application extends SpringBootServletInitializer {

    @Value("${location.topn}")
    private int locationTopN;

    @Value("${location.original.dir}")
    private String locationOriginalDir;

    @Value("${location.index.dir}")
    private String locationIndexDir;

    @Value("${location.snapshot.dir}")
    private String locationSnapshotDir;

    @Value("${keyword.topn}")
    private int keywordTopN;

    @Value("${keyword.list.file}")
    private String keywordListFile;

    @Value("${keyword.snapshot.dir}")
    private String keywordSnapshotDir;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public IdWeightStorage locationStorage() throws Exception {
        IdWeightStorage storage = new IdWeightStorage();

        // 1. read from original files ...
        log.info("Start reading the original location files ...");
        File dir = new File(locationOriginalDir);
        for(File file : dir.listFiles())
            storage.addIdWeight(file.getName());
        log.info("Finished reading the original location files ...");

        // 2. read from snapshot (count, weight and score)
        log.info("Start reading all location snapshots ...");
        storage.readAllSnapshots(locationSnapshotDir);
        log.info("Finished reading all location snapshots ...");

        // 3. get ready to service (sort and pick topN)
        log.info("Start sorting and picking top-n location ...");
        storage.sortAndPickTopN(locationTopN);
        log.info("Finished sorting and picking top-n location ...");

        return storage;
    }

    @Bean
    public IdWeightStorage keywordStorage() throws Exception {
        IdWeightStorage storage = new IdWeightStorage();

        // 1. read from keyword list file ...
        log.info("Start reading the keyword list ...");
        try (BufferedReader in = new BufferedReader(new FileReader(keywordListFile))){
            String keyword = null;
            while((keyword = in.readLine()) != null) {
                keyword = keyword.trim();
                if(keyword.isEmpty()) continue;

                storage.addIdWeight(keyword);
            }
        }
        log.info("Finished reading the keyword list ...");

        // 2. read from snapshot (count, weight and score)
        log.info("Start reading all keyword snapshots ...");
        storage.readAllSnapshots(keywordSnapshotDir);
        log.info("Finished reading all keyword snapshots ...");

        // 3. get ready to service (sort and pick topN)
        log.info("Start sorting and picking top-n keywords ...");
        storage.sortAndPickTopN(keywordTopN);
        log.info("Finished sorting and picking top-n keywords ...");

        return storage;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

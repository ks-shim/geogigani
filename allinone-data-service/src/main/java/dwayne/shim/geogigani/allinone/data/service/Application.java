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

import java.io.File;

@Log4j2
@SpringBootApplication
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
        log.info("Start reading from the original location files ...");
        File dir = new File(locationOriginalDir);
        for(File file : dir.listFiles())
            storage.addIdWeight(file.getName());
        log.info("Finished reading from the original location files ...");

        // 2. read from snapshot (count, weight and score)
        storage.readAllSnapshots(locationSnapshotDir);

        // 3. get ready to service (sort and pick topN)
        storage.sortAndPickTopN(locationTopN);

        return storage;
    }

    @Bean
    public IdWeightStorage keywordStorage() throws Exception {
        IdWeightStorage storage = new IdWeightStorage();

        // 1. read from keyword list file ...

        // 2. read from snapshot (count, weight and score)

        // 3. get ready to service (sort and pick topN)

        return storage;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

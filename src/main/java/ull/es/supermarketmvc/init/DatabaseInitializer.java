package ull.es.supermarketmvc.init;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        copyDatabaseToResources();
    }

    private void copyDatabaseToResources() throws IOException {
        String sourcePath = "target/classes/database/supermarketProducts.db";
        String targetPath = "src/main/resources/database/supermarketProducts.db";

        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        File sourceFile = source.toFile();
        File targetFile = target.toFile();

        // Copia la base de datos solo si no existe o si es diferente
        if (!targetFile.exists() || sourceFile.lastModified() > targetFile.lastModified()) {
            FileCopyUtils.copy(sourceFile, targetFile);
            System.out.println("Base de datos copiada a " + targetPath);
        }
    }
}
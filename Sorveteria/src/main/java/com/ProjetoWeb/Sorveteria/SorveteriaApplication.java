package com.ProjetoWeb.Sorveteria;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.File;

@SpringBootApplication
public class SorveteriaApplication implements CommandLineRunner {

    private static final String UPLOAD_DIR = "C:/uploads"; 

    public static void main(String[] args) {
        SpringApplication.run(SorveteriaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (created) {
                System.out.println("Diretório de uploads criado com sucesso!");
            } else {
                System.out.println("Falha ao criar o diretório de uploads.");
            }
        } else {
            System.out.println("O diretório de uploads já existe.");
        }
    }
}

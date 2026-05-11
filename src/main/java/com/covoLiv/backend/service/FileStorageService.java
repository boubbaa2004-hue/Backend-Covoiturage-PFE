package com.covoLiv.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String sauvegarderFichier(MultipartFile fichier, String prefixe) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
        System.out.println("=== UPLOAD === Dossier : " + uploadPath);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
            System.out.println("=== UPLOAD === Dossier créé !");
        }

        String nomOriginal = fichier.getOriginalFilename();
        String extension = "";
        if (nomOriginal != null && nomOriginal.contains(".")) {
            extension = nomOriginal.substring(nomOriginal.lastIndexOf(".")).toLowerCase();
        }

        if (!extension.equals(".jpg") && !extension.equals(".jpeg")
                && !extension.equals(".png") && !extension.equals(".pdf")) {
            throw new IOException("Format non supporté: " + extension);
        }

        String nomFichier = prefixe + "_" + UUID.randomUUID() + extension;
        Path cheminFichier = uploadPath.resolve(nomFichier);
        Files.copy(fichier.getInputStream(), cheminFichier, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("=== UPLOAD === Fichier sauvegardé : " + cheminFichier);

        return nomFichier;
    }

    public byte[] lireFichier(String nomFichier) throws IOException {
        Path cheminFichier = Paths.get(uploadDir).resolve(nomFichier);
        if (!Files.exists(cheminFichier)) {
            throw new IOException("Fichier non trouvé: " + nomFichier);
        }
        return Files.readAllBytes(cheminFichier);
    }
}

package com.tienda.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


public interface FirebaseStorageService {

    public String cargaImagen(MultipartFile archivoLocalCliente, String carpeta, Long id);

    //El BuketName es el <id_del_proyecto> + ".appspot.com"
    final String BucketName = "techshop-11a67.appspot.com";

    //Esta es la ruta básica de este proyecto practica01
    final String rutaSuperiorStorage = "techshop";

    //Ubicación donde se encuentra el archivo de configuración Json
    final String rutaJsonFile = "firebase";
    
    //El nombre del archivo Json
    final String archivoJsonFile = "techshop-7e20e-firebase-adminsdk-spis7-b3f2f34a29.json";
}
package com.cibertec.veterinaria.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile multipartFile) throws IOException;
    Map delete(String publicId) throws IOException;
}

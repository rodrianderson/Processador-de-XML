package com.example.xml.recovery.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.dom4j.DocumentException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(MultipartFile file);

  public Resource load(String filename);

  public void deleteAll();

  public Stream<Path> loadAll();
  
  public void readXML(MultipartFile file) throws DocumentException, IOException;
}
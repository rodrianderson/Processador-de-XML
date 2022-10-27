package com.example.xml.recovery.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {

  private final Path root = Paths.get("uploads");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(MultipartFile file) {
    try {
      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }

  @Override
  public Resource load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  @Override
  public void deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll() {
    try {
      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Não foi possível carregar os arquivos!");
    }
  }

	@Override
	public void readXML(MultipartFile file) throws DocumentException, IOException {
		  SAXReader reader = new SAXReader();
		  Document  document = reader.read(file.getInputStream());
		  
		  Element root = document.getRootElement();

		  root.accept(new VisitorSupport() {		
		    @Override
		    public void visit(Element node) {
		    
		      if (node.getQualifiedName().equals("agente")) {
		    	   System.out.println(" ");
				   Element prodNode = (Element) node.element("codigo");
				   System.out.print("Código: " + prodNode.getText() + ", ");	        
		      }
		      if (node.getQualifiedName().equals("regiao")) {
		    	  String attributoNItem = node.attributeValue("sigla");
		          System.out.print("Sigla: " + attributoNItem + ", ");		        
		      }
		   
		    }
		  });
		
	}

}
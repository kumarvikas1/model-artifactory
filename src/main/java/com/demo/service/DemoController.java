package com.demo.service;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping(path = "/model-service")
public class DemoController {


	@PostConstruct
	public void create() throws IOException {
		if (!Files.exists(Paths.get(System.getProperty("user.dir") + "/tmp_models/"))) {
			Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/tmp_models/"));
		}
	}

	@GetMapping(path = "file")
	public StreamingResponseBody download() throws Exception {
		File initialFile = new File(System.getProperty("user.dir") + "/model/dummy_xgboost.dat");
		InputStream inputStream = new FileInputStream(initialFile);
		return outputStream -> {
			try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
				int read;
				while ((read = reader.read()) != -1) {
					outputStream.write(read);
				}
			}
		};
	}

	@GetMapping(path = "download-server/{model_name}")
	public byte[] download1(@PathVariable("model_name") String modelName) throws Exception {
		Path path = Files.list(Paths.get(System.getProperty("user.dir") + "/tmp_models/" + modelName))
				.findFirst().
				orElseThrow(() -> new IllegalArgumentException("Model not found"));
		return Files.readAllBytes(path);

		//return  new ClassPathResource("classpath:model/dummy_xgboost.dat").getInputStream().readAllBytes();
	}

	@PostMapping(value = "/upload/{modelName}",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity singleFileUpload(@RequestParam("file") MultipartFile file,
										   @PathVariable("modelName") String modelName) throws Exception {
		byte[] bytes = file.getBytes();
		Files.createDirectory(Paths.get(System.getProperty("user.dir") + "/tmp_models/" + modelName));
		Path path = Paths.get(System.getProperty("user.dir") + "/tmp_models/" + modelName + "/" + file.getOriginalFilename());
		Files.write(path, bytes);
		return ResponseEntity.status(201).build();
	}

	@GetMapping(path = "download-server/test")
	public String download2() throws Exception {
		return "ping";
	}
}

/* @#MergeImageToPdfApplication.java - 2020
 * You can modify, use, reproduce or distribute this software.
 */
package dev.requena.pdf.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import dev.requena.pdf.generator.service.PdfGeneratorService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class MergeImageToPdfApplication implements ApplicationRunner {

  private final PdfGeneratorService service;

  public MergeImageToPdfApplication(PdfGeneratorService service) {
    super();
    this.service = service;
  }

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(MergeImageToPdfApplication.class);
    app.setBannerMode(Mode.OFF);
    app.run(args);
  }

  /* (non-Javadoc)
   * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
   */ @Override
  public void run(ApplicationArguments args) throws Exception {

    try {

      String name = "";
      List<String> names = args.getOptionValues("output.file.name");
      if (!CollectionUtils.isEmpty(names)) {
        name = names.get(0);
      }

      List<String> titles = new ArrayList<>();
      List<String> titlesArgs = args.getOptionValues("images.titles");
      if (!CollectionUtils.isEmpty(titlesArgs)) {
        titles = Arrays.asList(titlesArgs.get(0).split(","));
      }

      List<String> images = new ArrayList<>();
      List<String> imagesArgs = args.getOptionValues("images.files.names");
      if (!CollectionUtils.isEmpty(imagesArgs)) {
        images = Arrays.asList(imagesArgs.get(0).split(","));
      }

      log.info("output file name = {}", name);
      log.info("titles = {}", titles);
      log.info("images = {}", images);

      service.generatePdfFromImages(name, titles, images);

    } catch (Exception e) {
      log.error("Error generating file: " + e.getMessage());
      return;
    }

    log.info("File successfully created.");
  }
}

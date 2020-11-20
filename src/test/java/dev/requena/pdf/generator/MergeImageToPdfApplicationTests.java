package dev.requena.pdf.generator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MergeImageToPdfApplicationTests {

  @Autowired private MergeImageToPdfApplication mergeImageToPdfApplication;

  @Test
  void generate() {

    try {
	
      String outputFileName = "generate_test_file";

      Path imagePath1 = Paths.get(ClassLoader.getSystemResource("amazon_sns.png").toURI());
      Path imagePath2 = Paths.get(ClassLoader.getSystemResource("amazon_sqs.png").toURI());

      List<String> arguments = new ArrayList<String>();
      arguments.add("--output.file.name=" + outputFileName);
      arguments.add("--images.titles=" + imagePath1.getFileName() + "," + imagePath2.getFileName());
      arguments.add(
          "--images.files.names="
              + imagePath1.toAbsolutePath()
              + ","
              + imagePath2.toAbsolutePath());
      
      DefaultApplicationArguments args =
          new DefaultApplicationArguments((String[]) arguments.toArray(new String[arguments.size()]));
      mergeImageToPdfApplication.run(args);

      Path outputFile = Paths.get(ClassLoader.getSystemResource(outputFileName).toURI());

      assertEquals(true, outputFile.toFile().exists());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

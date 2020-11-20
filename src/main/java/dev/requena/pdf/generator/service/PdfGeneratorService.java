/* @#PdfGeneratorService.java - 2020
 * You can modify, use, reproduce or distribute this software.
 */
package dev.requena.pdf.generator.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

/** @author rrequena */
@Service
public class PdfGeneratorService {

  private String inputFolderPath;
  private String outPutFolder;

  /**
   * Constructor
   *
   * @param inputFolderPath
   * @param outPutFolder
   */
  public PdfGeneratorService(
      @Value(value = "${input.images.folder.path}") String inputFolderPath,
      @Value(value = "${output.images.folder.path}") String outPutFolder) {
    super();
    this.inputFolderPath = inputFolderPath;
    this.outPutFolder = outPutFolder;
  }

  /**
   * Method that create a PDF file with the images
   *
   * @param title
   * @param name
   * @param images
   * @throws Exception
   * @throws MalformedURLException
   */
  public void generatePdfFromImages(String name, List<String> titles, List<String> images)
      throws Exception {

    if (titles.size() != images.size()) {
      throw new Exception("The length of the titles has to be the same of the images");
    }

    Document document = new Document();
    PdfWriter.getInstance(document, new FileOutputStream(getFileName(name)));

    document.open();

    addImages(document, titles, images);

    document.close();
  }

  /**
   * Method that
   *
   * @param document
   * @param images
   * @throws IOException
   * @throws MalformedURLException
   * @throws DocumentException
   */
  private void addImages(Document document, List<String> titles, List<String> images)
      throws IOException, DocumentException {

    for (int i = 0; i < images.size(); i++) {

      String title = titles.get(i);
      String image = images.get(i);

      if (!ObjectUtils.isEmpty(inputFolderPath)) {
        image = inputFolderPath + image;
      }

      addTitle(document, title);

      Image img = Image.getInstance(image);
      document.add(img);
    }
  }

  /**
   * Method that validate if it is the name provided or generate a new one
   *
   * @param name
   * @return name of the file
   */
  private String getFileName(String name) {

    if (!ObjectUtils.isEmpty(name)) {
      if (!name.contains(".pdf")) {
        name = name + ".pdf";
      }
      return name;
    } else {
      name = "generated_pdf_" + System.currentTimeMillis() + ".pdf";
    }

    if (!ObjectUtils.isEmpty(outPutFolder)) {
      name = outPutFolder + name;
    }

    return name;
  }

  /**
   * Method that add a title to the document if it is provided
   *
   * @param document
   * @param title
   * @throws DocumentException
   */
  private void addTitle(Document document, String title) throws DocumentException {

    if (!ObjectUtils.isEmpty(title)) {
      Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
      Chunk chunk = new Chunk(title, font);
      document.add(chunk);
    }
  }
}

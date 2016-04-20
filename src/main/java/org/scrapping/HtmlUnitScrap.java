package org.scrapping;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.xerces.impl.dv.util.Base64;
import org.scrapping.dao.Person;
import org.scrapping.dao.PersonJpaController;

import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author Rigoberto Leander Salgado Reyes <rlsalgado2006@gmail.com>
 * <p>
 * Copyright 2016 by Rigoberto Leander Salgado Reyes.
 * <p>
 * This program is licensed to you under the terms of version 3 of the
 * GNU Affero General Public License. This program is distributed WITHOUT
 * ANY EXPRESS OR IMPLIED WARRANTY, INCLUDING THOSE OF NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. Please refer to the
 * AGPL (http:www.gnu.org/licenses/agpl-3.0.txt) for more details.
 */
public class HtmlUnitScrap {
    public static void main(String... args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DirectoryDAOPU");

        String[] genders = {"Femenino", "Masculino"};

        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);

        try {
            final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getOptions().setCssEnabled(true);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            PersonJpaController jpaController = new PersonJpaController(emf);

            File imageFolder = new File("images");
            if (!imageFolder.exists()) {
                imageFolder.mkdir();
            }

            final HtmlPage page = webClient.getPage("http://directorio.uci.cu");
            for (String g : genders) {
                HtmlForm form = (HtmlForm) page.getElementById("directorio-de-personas-mostrar-form");

                HtmlSelect filters = form.getSelectByName("filtros");
                filters.setDefaultValue("Género");

                HtmlSelect gender = form.getSelectByName("sexo");
                gender.setDefaultValue(g);

                HtmlPage results = form.getInputByValue("Buscar").click();

                AtomicInteger size = new AtomicInteger(1);

                for (int j = 1; j <= size.get(); j++) {

                    HtmlTable mainTable = (HtmlTable) results.getElementById("table_result");

                    for (int i = 0; i < mainTable.getRowCount(); i++) {
                        Person p = new Person();

                        HtmlTable table1 = (HtmlTable) mainTable.getCellAt(i, 2).getElementsByTagName("table").get(0);
                        HtmlTable table2 = (HtmlTable) mainTable.getCellAt(i, 4).getElementsByTagName("table").get(0);
                        HtmlTable table3 = (HtmlTable) mainTable.getCellAt(i, 5).getElementsByTagName("table").get(0);
                        HtmlTable table4 = (HtmlTable) mainTable.getCellAt(i, 7).getElementsByTagName("table").get(0);

                        //NOMBRES, APELLIDOS, USUARIO, SOLAPIN
                        setNameLastNameUsernameIdInsignia(p, table1);

                        //CATEGORÍA, CARGO, NO. EXP.
                        setCategoryPositionNoExp(p, table2);

                        //ÁREA, FACULTAD, GRUPO
                        setAreaFacultyGroup(p, table3);

                        //PROVINCIA, MUNICIPIO, APTO., TELEF.
                        setProvinceTownFlatPhone(p, table4);

                        p.setGender(getGender(g));

                        //FOTO
                        downloadPhoto(p);

                        jpaController.create(p);
                    }

                    System.out.println(j + " ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

                    //IR A LA SIGUIENTE PÁGINA
                    results = nextPage(results, size);
                }
            }

            webClient.close();
        } catch (IOException | FailingHttpStatusCodeException ex) {
            Logger.getLogger(HtmlUnitScrap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static HtmlPage nextPage(HtmlPage results, AtomicInteger size) throws IOException {
        HtmlTable paging = (HtmlTable) results.getElementById("table_result_paginado");
        HtmlLabel totalPages = (HtmlLabel) results.getElementById("label_page_total");
        HtmlTableCell next = paging.getCellAt(0, 5);

        size.set(Integer.parseInt(totalPages.asText().replaceFirst("de ", "")));
        HtmlDivision div = (HtmlDivision) next.getFirstElementChild();
        HtmlAnchor anchor = (HtmlAnchor) div.getFirstElementChild();
        if (anchor != null) {
            results = anchor.click();
        }
        return results;
    }

    private static void downloadPhoto(Person p) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        String baseUrl = "http://directorio.uci.cu/sites/all/modules/custom/directorio_de_personas/display_foto.php?id=";
        URL url = new URL(baseUrl + p.getIdInsignia());
        BufferedImage img = ImageIO.read(url);
        if (img == null) {
            img = ImageIO.read(new File(HtmlUnitScrap.class.getClassLoader().getResource("no-pic.jpg").getPath()));
        }
        ImageIO.write(img, "jpg", baos);
        baos.flush();
        String base64String = Base64.encode(baos.toByteArray());
        baos.close();
        byte[] bytearray = Base64.decode(base64String);
        BufferedImage imag = ImageIO.read(new ByteArrayInputStream(bytearray));
        String stdName = p.getFirstName().replaceAll(" ", "-") + "-" + p.getLastName().replaceAll(" ", "-");
        if (!p.getIdInsignia().equals("")) {
            ImageIO.write(imag, "jpg", new File("images" + File.separator + p.getIdInsignia() + ".jpg"));
        } else {
            ImageIO.write(imag, "jpg", new File("images" + File.separator + stdName + ".jpg"));
        }
    }

    private static void setProvinceTownFlatPhone(Person p, HtmlTable table4) {
        p.setProvince(table4.getRow(0).getCell(1).asText());
        p.setTown(table4.getRow(1).getCell(1).asText());

        if (table4.getRowCount() == 4) {
            p.setFlat(table4.getRow(2).getCell(1).asText());
            p.setPhone(table4.getRow(3).getCell(1).asText());
        } else {
            p.setFlat("");
            p.setPhone("");
        }
    }

    private static void setAreaFacultyGroup(Person p, HtmlTable table3) {
        int tmp = 0;
        String str = table3.getRow(tmp).getCell(0).asText();
        if (str.equals("Facultad")) {
            p.setFaculty(table3.getRow(tmp++).getCell(1).asText());
            p.setArea("");
        } else {
            p.setArea(table3.getRow(tmp++).getCell(1).asText());
            p.setFaculty("");
        }
        if (table3.getRowCount() == 2) {
            str = table3.getRow(tmp).getCell(1).asText();
            if (!str.equals("")) {
                p.setFacultyGroup(str);
            } else {
                p.setFacultyGroup("");
            }
        } else {
            p.setFacultyGroup("");
        }
    }

    private static void setCategoryPositionNoExp(Person p, HtmlTable table2) {
        int tmp = 0;
        p.setCategory(table2.getRow(tmp++).getCell(1).asText());
        if (table2.getRowCount() == 3) {
            p.setPosition(table2.getRow(tmp++).getCell(1).asText());
        } else {
            p.setPosition("");
        }
        p.setNoExp(table2.getRow(tmp).getCell(1).asText());
    }

    private static void setNameLastNameUsernameIdInsignia(Person p, HtmlTable table1) {
        p.setFirstName(table1.getRow(0).getCell(1).asText());
        p.setLastName(table1.getRow(1).getCell(1).asText());
        p.setUserName(table1.getRow(2).getCell(1).asText());
        p.setIdInsignia(table1.getRow(3).getCell(1).asText());
    }

    private static char getGender(String g) {
        return g.toLowerCase().charAt(0);
    }
}

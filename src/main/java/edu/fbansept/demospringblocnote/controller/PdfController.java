package edu.fbansept.demospringblocnote.controller;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceCmyk;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import edu.fbansept.demospringblocnote.dao.UtilisateurDao;
import edu.fbansept.demospringblocnote.model.*;
import edu.fbansept.demospringblocnote.security.JwtUtil;
import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
public class PdfController {

    private UtilisateurDao utilisateurDao;
    private JwtUtil jwtUtil;

    @Value("${dossier.upload}")
    private String dossierUpload;

    @Autowired
    PdfController(
            UtilisateurDao utilisateurDao,
            JwtUtil jwtUtil){
        this.utilisateurDao = utilisateurDao;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test/pdf")
    public ResponseEntity<String> getPdf (
            @RequestHeader(value="Authorization") String authorization) {

        try {

            StringBuffer fileName = new StringBuffer();
            fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
            fileName.append(".pdf");

            //++++++++++ RECUPERATION DES NOTES DE L'UTILISATEUR ++++++++++

            String token = authorization.substring(7);
            Integer idUtilisateur = jwtUtil.getTokenBody(token).get("id",Integer.class);

            String username = jwtUtil.getTokenBody(token).getSubject();

            Optional<Utilisateur> utilisateur = utilisateurDao.trouverParPseudo(username);

            //++++++++++ GENERATION DU PDF ++++++++++

            //Documentation / tutoriel :
            //https://kb.itextpdf.com/home/it7kb/examples/itext-7-jump-start-tutorial-chapter-1

            PdfWriter pdfWriter = new PdfWriter(dossierUpload + "/" + fileName);
            PdfDocument pdf = new PdfDocument(pdfWriter);
            Document document = new Document(pdf, PageSize.A4);
            //postionnement paysage
            //Document document = new Document(pdf, PageSize.A4.rotate());

            document.setMargins(20, 20, 20, 20);

            // ++++++++++ AJOUT D'UN BACKGROUND +++++++++++++++

            //note l'image doit avoir un ratio correspondant à 595 x 842
            //une plus grande taille garantie moins de pixelisation lors d'un zoom
            Resource resource = new ClassPathResource("static/images/pdf_background.png");

            PdfCanvas canvas = new PdfCanvas(pdf.addNewPage());
            canvas.addImageFittedIntoRectangle(
                    ImageDataFactory.create(IOUtils.toByteArray(resource.getInputStream())),
                    PageSize.A4,
                    false);

            // ++++++++ AJOUT D'UN LOGO ++++++++++

            //note : redimensionner une ne baissera pas son poid, mais il y  aura moins de pixelisation au zoom
            resource = new ClassPathResource("static/images/logo.png");
            Image logo = new Image(ImageDataFactory.create(IOUtils.toByteArray(resource.getInputStream())));
            logo.setWidth(80);
            logo.setHeight(80);

            // ++++++++ AJOUT D'UN TITRE ++++++++++

            //Attention : les PDF pouvant être destiné à l'impression doivent utiliser
            //le Format CMJN (cyan Magenta Jaune Noir) correspondant au cartouche d'encre
            //DeviceCmyk prend 4 paramètres entre 0 et 1
            //color picker en ligne : https://www.google.com/search?q=color+picker

            Color blueColor = new DeviceCmyk(0.5f, 0.37f, 0, 0.59f);

            PdfFont policeTitre = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);
            Paragraph titre = new Paragraph("Bloc note").setFontSize(36);
            titre.setFont(policeTitre);
            titre.setFontColor(blueColor);

            // ++++++++ CREATION DE L'ENTETE  ++++++++++

            //création d'un tableau avec une colonne de 20% et une autre de 80% de largeur
            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 80}))
                    .useAllAvailableWidth();

            Cell cell = new Cell();
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.add(titre);

            table.addCell(logo);
            table.addCell(cell);

            supprimeBordure(table);

            document.add(table);

            // ++++++++ CREATION D'UNE SIMPLE LISTE  ++++++++++

            //----- SAUTER UNE LIGNE ------
            document.add(new Paragraph().add(new Text("\n")));

            document.add(new Paragraph("Dans ce document vous trouverez : "));

            List list = new List()
                    .setSymbolIndent(12)
                    .setListSymbol("\u2022");//caractère unicode du point

            list.add(new ListItem("Toutes les notes textuelles"))
                    .add(new ListItem("Toutes les TODO listes"))
                    .add(new ListItem("Un graphique de la répartitions des type de notes"));

            document.add(list);

            // ++++++++ CREATION D'UNE POLICE VIA UN FICHIER TTF  ++++++++++

            //*********************

            //Attention à bien ajouter la configuration suivante dans le fichier pom.xml
            //dan sle noeud concernant "maven plugin resource"

            //<plugin>
            //    <groupId>org.apache.maven.plugins</groupId>
            //    <artifactId>maven-resources-plugin</artifactId>
            //    <version>3.1.0</version>
            //
            //    <configuration>
            //        <encoding>UTF-8</encoding>
            //        <nonFilteredFileExtensions>
            //            <nonFilteredFileExtension>ttf</nonFilteredFileExtension>
            //        </nonFilteredFileExtensions>
            //    </configuration>
            //</plugin>

            resource = new ClassPathResource("static/fonts/FREESCPT.TTF");
            byte[] fontByte = IOUtils.toByteArray(resource.getInputStream());

            PdfFont policeManuscrite = PdfFontFactory.createFont(fontByte, PdfEncodings.IDENTITY_H);

            //++++++++ LISTE DES NOTES ++++++++

            PdfFont policeTitreNote = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);

            for(Note note : utilisateur.get().getListeNote()) {

                //----- SAUTER UNE LIGNE ------
                document.add(new Paragraph().add(new Text("\n")));

                Paragraph titreNote = new Paragraph(note.getTitre()).setFontSize(16);
                titreNote.setFont(policeTitreNote);
                titreNote.setFontColor(blueColor);
                document.add(titreNote);

                if(note instanceof NoteTexte) {

                    String texte = ((NoteTexte)note).getTexte();

                    Paragraph contenu = new Paragraph(texte)
                            .setFontSize(20)
                            .setFont(policeManuscrite)
                            .setMultipliedLeading(0.8f);//change l'espace après une ligne

                    document.add(contenu);

                } else {

                    NoteListe noteListe = (NoteListe)note;

                    for(Tache tache : noteListe.getListeTache())
                    {
                        Paragraph ligne = new Paragraph();



                        ligne.add(tache.getTexte())
                                .setFont(policeManuscrite)
                                .setFontSize(20)
                                .setMultipliedLeading(0.6f);//change l'espace après une ligne

                        document.add(ligne);
                    }
                }
            }


            /*
            Ajout d'image avec redimenssionnement (pour faire baisser le poid du pdf)

            BufferedImage inputImage = ImageIO.read(resource.getInputStream());

            BufferedImage imgageRedimenssionne = new BufferedImage(200, 200, inputImage.getType());
            Graphics2D g = imgageRedimenssionne.createGraphics();
            g.drawImage(inputImage, 0, 0, 200, 200, null);
            g.dispose();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imgageRedimenssionne, "png", baos);

            Image img = new Image(ImageDataFactory.create(baos.toByteArray()));

             */

            DefaultPieDataset dataset = new DefaultPieDataset();
            dataset.setValue("Note texte", 22);
            dataset.setValue("Note Liste", 34);

            JFreeChart chart = ChartFactory.createPieChart("",
                    dataset, true, false, false);

            chart.setBorderVisible(false);
            chart.getPlot().setBackgroundPaint(java.awt.Color.WHITE);
            chart.getPlot().setOutlineVisible(false);
            ((PiePlot) chart.getPlot()).setLabelGenerator(null);

            byte[] chartPng = ChartUtils.encodeAsPNG(
                    chart.createBufferedImage(400, 200), false, 100);

            Image imageChart= new Image(ImageDataFactory.create(chartPng));
            imageChart.setHorizontalAlignment(HorizontalAlignment.CENTER);

            document.add(imageChart);

            pdf.close();

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void supprimeBordure(Table table)
    {
        for (IElement iElement : table.getChildren()) {
            ((Cell)iElement).setBorder(Border.NO_BORDER);
        }
    }
}







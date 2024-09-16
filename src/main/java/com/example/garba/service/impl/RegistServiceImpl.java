package com.example.garba.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.garba.dto.RegistDto;
import com.example.garba.entity.Registration;
import com.example.garba.exception.MobileNumberAlreadyExistsException;
import com.example.garba.mapper.RegistMapper;
import com.example.garba.repository.RegistRepository;
import com.example.garba.service.RegistService;
import com.itextpdf.text.*;
// import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class RegistServiceImpl implements RegistService {

    private RegistRepository registRepository;

    public RegistServiceImpl(RegistRepository registRepository){
        this.registRepository = registRepository;
    }

    @Override
    public RegistDto registerDetail(RegistDto registDto, MultipartFile photo) {
        if(registRepository.existsByMobileNo(registDto.getMobileNo()) && registDto.getId()==0){
            throw new MobileNumberAlreadyExistsException("Mobile number is already registered");
        }
        Registration registration = RegistMapper.mapToRegistration(registDto);
        if(photo != null && !photo.isEmpty()){
            try {
                registration.setPhoto(photo.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to process the photo", e);
            }
        }else {
            Optional<Registration> existRegistration = registRepository.findById(registDto.getId());

            if(existRegistration.isPresent()){
                registration.setPhoto(existRegistration.get().getPhoto());
            } else {
              // If the record is not found, you can either throw an exception
              // or return null, depending on your design choice.
              throw new NoSuchElementException("No registration found with ID: " + registDto.getId());
          }

        }
        Registration savRegistration = registRepository.save(registration);
        if(registDto.getId() == 0){
            String  regsNo = String.valueOf(savRegistration.getId() + 100);
            savRegistration.setRegsNo(regsNo);
             registRepository.save(savRegistration);
        }
       
        
       return RegistMapper.mapToRegistDto(savRegistration);
    //   return  this.registRepository ;
    }

    @Override
    public List<RegistDto> findByMobileNo(String mobileNo) {
        List<Registration> registEntities  = registRepository.findByMobileNo(mobileNo);
       
         if (registEntities.isEmpty()) {
            throw new NoSuchElementException("No records found for the mobile number: " + mobileNo);
        }
        return registEntities.stream().map(RegistMapper:: mapToRegistDto)
                                    .collect(Collectors.toList());
    }

    @Override
    public RegistDto getDetailById(Long id) {

      Optional<Registration> registration = registRepository.findById(id);

      if(registration.isPresent()){
        return RegistMapper.mapToRegistDto(registration.get());
      } else {
        // If the record is not found, you can either throw an exception
        // or return null, depending on your design choice.
        throw new NoSuchElementException("No registration found with ID: " + id);
    }
    }

    @Override
    public List<RegistDto> getAllData() {
        try{
        List<Registration> registrations = registRepository.findAll();

        return registrations.stream().map(RegistMapper:: mapToRegistDto)
                                    .collect(Collectors.toList());
        }catch(Exception e){
            throw new RuntimeException("An unexpected error occurred while processing your request", e);
        }
    }

    @Override
    public byte[] downloadPdfById(Long id) throws Exception {
       
        Optional<Registration> registration = registRepository.findById(id);

        if(registration.isPresent()){

            Registration user = registration.get();
            Rectangle pagesSize = new Rectangle(300,450);
           
            Document document = new Document(pagesSize);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            PdfContentByte border = writer.getDirectContent();
           
            border.setColorStroke(new BaseColor(0,153,153));
            border.setLineWidth(20f);
            border.rectangle(pagesSize.getLeft() + 10,
                             pagesSize.getBottom() + 10,
                             pagesSize.getWidth() - 20,   // Width (reduce by 20 for padding)
                             pagesSize.getHeight() - 20); 
            border.stroke();

            PdfContentByte border2 = writer.getDirectContent();
           
            border2.setColorStroke(new BaseColor(253,101,101));
            border2.setLineWidth(11.5f);
            border2.rectangle(pagesSize.getLeft() + 14,
                             pagesSize.getBottom() + 14,
                             pagesSize.getWidth() - 28,   // Width (reduce by 20 for padding)
                             pagesSize.getHeight() - 28); 
            border2.stroke();

            

           
            PdfContentByte canvas = writer.getDirectContentUnder();

            canvas.setColorFill(new BaseColor(253,233,214));
            canvas.rectangle(0, 0, pagesSize.getWidth(), pagesSize.getHeight());
            canvas.fill();
                 PdfGState gs = new PdfGState();
        gs.setFillOpacity(0.4f); // Set opacity (0.1 is very transparent, 1.0 is fully opaque)
        canvas.setGState(gs);
            Image watermark = Image.getInstance("src/assets/udaanLogo.png");
            watermark.setAbsolutePosition(0, 80); // Position the image
        watermark.scaleAbsolute(300, 200); // Scale the image if needed
        canvas.addImage(watermark);

        // PdfGState texts = new PdfGState();
        // texts.setFillOpacity(0.2f); // Set opacity (0.1 is very transparent, 1.0 is fully opaque)
        // canvas.setGState(texts);
        //     Font watermarkFont = new Font(Font.FontFamily.HELVETICA,70,Font.BOLD, BaseColor.GREEN);
        //     ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, new Paragraph("UDAAN", watermarkFont)
        //                                 , document.getPageSize().getWidth()/2
        //                                 , document.getPageSize().getHeight()/2, 0);

            //Add custom title Header
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD, new BaseColor(102,0,204) );
            Chunk titleChunk = new Chunk("UDAAN GARBA", titleFont);
            titleChunk.setUnderline(new BaseColor(51,0,102), 3f, 0, -4f, 0, 0);
            Paragraph title = new Paragraph(titleChunk);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setLeading(20f); // Adjust space before title
            title.setSpacingBefore(20f); // Adjust space before title
            title.setSpacingAfter(30f);
            document.add(title);

           
            // document.add(new Paragraph(" "));

            if (user.getPhoto() != null) {
                byte[] photoBytes = user.getPhoto();
                Image photoImage = Image.getInstance(photoBytes);
                photoImage.scaleToFit(100, 100); // Adjust size to passport size
                photoImage.setAbsolutePosition((pagesSize.getWidth() - 90) / 2, pagesSize.getHeight() - 170); // Position below the title
                document.add(photoImage);
                document.add(new Paragraph(" ")); // Add some space below the photo
                document.add(new Paragraph(" ")); // Add some space below the photo
                document.add(new Paragraph(" ")); // Add some space below the photo
                document.add(new Paragraph(" ")); // Add some space below the photo
                document.add(new Paragraph(" ")); // Add some space below the photo
            }

             // Add user details
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font regularFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

             PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100); // Set table width to 100% of page width
            table.setHorizontalAlignment(Element.ALIGN_CENTER); // Center the table

            // Add table cells with field names in bold and values in normal font
            float[] columnWidths = { 45f, 55f };
            table.setWidths(columnWidths);

            // Add table cells with field names in bold and values in normal font
            addAlignedTableCell(table, "First Name: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, user.getFirstName(), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Last Name: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, user.getLastName(), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Mobile No: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, String.valueOf(user.getMobileNo()), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Age: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, String.valueOf(user.getAge()), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Category: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, String.valueOf(user.getGender()), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Batch Time: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, String.valueOf(user.getBatchTime()), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Address: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, String.valueOf(user.getAddress()), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Registration No: ", boldFont, Element.ALIGN_RIGHT,false);
            addAlignedTableCell(table, user.getRegsNo(), regularFont, Element.ALIGN_LEFT,true);

            addAlignedTableCell(table, "Status: ", boldFont, Element.ALIGN_RIGHT,false);
            addStatusTableCell(table, user.getPayment());

           

            // Add the table to the document
            document.add(table);
           
            document.close();
            } catch (DocumentException e) {
                throw new Exception("Error while generating PDF", e);
            }
            return out.toByteArray();
        } else {
            throw new Exception("User not found");
        }
        

    }

    private void addAlignedTableCell(PdfPTable table, String text, Font font, int alignment,boolean isLeft) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(PdfPCell.NO_BORDER); // Remove the border around cells
        cell.setHorizontalAlignment(alignment); // Set the text alignment (right or left)
        if (isLeft) cell.setPaddingLeft(20f);

        table.addCell(cell);
    }

    private void addStatusTableCell(PdfPTable table, String payment) {
        Font buttonFont = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD, BaseColor.WHITE); // White text


        PdfPCell cell = new PdfPCell();

        if("paid".equalsIgnoreCase(payment)){
            cell.setPhrase(new Phrase("Active",buttonFont));
            cell.setBackgroundColor(BaseColor.GREEN);
        }else {
            cell.setPhrase(new Phrase("Disable",buttonFont));
            cell.setBackgroundColor(BaseColor.RED);
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center text in the cell
        cell.setPaddingLeft(10f); // Add padding to simulate a button look
        cell.setBorder(PdfPCell.NO_BORDER); // No border to simulate a flat button
    
        table.addCell(cell);
    }

    
    

}

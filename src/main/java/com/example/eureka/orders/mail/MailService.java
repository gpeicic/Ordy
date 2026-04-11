package com.example.eureka.orders.mail;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.exception.ResourceNotFoundException;
import com.example.eureka.exception.ValidationException;
import com.example.eureka.orders.Order;
import com.example.eureka.supplier.Supplier;
import com.example.eureka.supplier.SupplierMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final SupplierMapper supplierMapper;
    private final CompanyMapper companyMapper;
    public MailService(JavaMailSender mailSender, SupplierMapper supplierMapper, CompanyMapper companyMapper) {
        this.mailSender = mailSender;
        this.supplierMapper = supplierMapper;
        this.companyMapper = companyMapper;
    }

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public void sendOrderPdf(Order order, byte[] pdfBytes) {
        Supplier supplier = supplierMapper.findById(order.getSupplierId());
        if (supplier == null) {
            throw new ResourceNotFoundException("Dobavljač nije pronađen: " + order.getSupplierId());
        }
        if (supplier.getMail() == null || supplier.getMail().isBlank()) {
            throw new ValidationException("Dobavljač nema email adresu: " + supplier.getName());
        }
        Company company = companyMapper.findById(order.getCompanyId());
        if (company == null) {
            throw new ResourceNotFoundException("Kompanija nije pronađena: " + order.getCompanyId());
        }

        String confirmUrl = baseUrl + "/orders/confirm/" + order.getId();

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(supplier.getMail());
            helper.setSubject("Narudžbenica #" + order.getId() + " - " + company.getName());

            String htmlContent = String.format(
                    "<p>U privitku se nalazi narudžbenica #%d.</p>" +
                            "<p>Molimo Vas da potvrdite primitak narudžbe klikom na link ispod:</p>" +
                            "<p><a href='%s' style='background-color: #4CAF50; color: white; padding: 10px 20px; " +
                            "text-decoration: none; border-radius: 5px;'>POTVRDI NARUDŽBU</a></p>" +
                            "<br><p>S poštovanjem,<br>%s</p>",
                    order.getId(), confirmUrl, company.getName()
            );

            helper.setText(htmlContent, true);
            helper.addAttachment("narudzbenica_" + order.getId() + ".pdf", new ByteArrayResource(pdfBytes));

        } catch (MessagingException e) {
            throw new RuntimeException("Greška pri slanju maila dobavljaču: " + supplier.getMail(), e);
        }

        mailSender.send(message);
    }
}

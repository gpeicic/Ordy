package com.example.eureka.orders.pdfGenerator;

import com.example.eureka.company.Company;
import com.example.eureka.company.CompanyMapper;
import com.example.eureka.orders.Order;
import com.example.eureka.supplier.Supplier;
import com.example.eureka.supplier.SupplierMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

    public void sendOrderPdf(Order order, byte[] pdfBytes) {
        Supplier supplier = supplierMapper.findById(order.getSupplierId());
        Company company = companyMapper.findById(order.getCompanyId());

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(supplier.getMail());
            helper.setSubject("Narudžbenica #" + order.getId() + " - " + company.getName());
            helper.setText("U privitku se nalazi narudžbenica #" + order.getId() + ".\n\nS poštovanjem,\n" + company.getName());
            helper.addAttachment("narudzbenica_" + order.getId() + ".pdf", new ByteArrayResource(pdfBytes));
        } catch (MessagingException e) {
            throw new RuntimeException("Greška pri slanju maila", e);
        }

        mailSender.send(message);
    }
}

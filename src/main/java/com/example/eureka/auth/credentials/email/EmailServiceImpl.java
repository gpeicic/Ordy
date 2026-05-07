package com.example.eureka.auth.credentials.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendCredentials(String toEmail,
                                String ownerUsername, String ownerPassword,
                                String employeeUsername, String employeePassword,
                                String setupLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Vaši pristupni podaci — Ordy");

            String html = """
                <html><body style="font-family:sans-serif;max-width:600px;margin:auto">
                  <h2 style="color:#2c3e50">Dobrodošli u Ordy!</h2>
                  <p>Vaši pristupni podaci su generirani. <strong>Preporučujemo da ih odmah promijenite.</strong></p>
                  
                  <h3 style="color:#2980b9">Vlasnik (Admin)</h3>
                  <table style="border-collapse:collapse;width:100%%">
                    <tr><td style="padding:8px;border:1px solid #ddd;background:#f9f9f9"><b>Korisničko ime</b></td>
                        <td style="padding:8px;border:1px solid #ddd">%s</td></tr>
                    <tr><td style="padding:8px;border:1px solid #ddd;background:#f9f9f9"><b>Lozinka</b></td>
                        <td style="padding:8px;border:1px solid #ddd">%s</td></tr>
                  </table>
                  
                  <h3 style="color:#27ae60">Zaposlenik</h3>
                  <table style="border-collapse:collapse;width:100%%">
                    <tr><td style="padding:8px;border:1px solid #ddd;background:#f9f9f9"><b>Korisničko ime</b></td>
                        <td style="padding:8px;border:1px solid #ddd">%s</td></tr>
                    <tr><td style="padding:8px;border:1px solid #ddd;background:#f9f9f9"><b>Lozinka</b></td>
                        <td style="padding:8px;border:1px solid #ddd">%s</td></tr>
                  </table>
                  
                  <br>
                  <a href="%s" style="display:inline-block;padding:12px 24px;background:#2980b9;color:white;
                     text-decoration:none;border-radius:4px;font-size:16px">
                    Promijeni korisničko ime i lozinku
                  </a>
                  <p style="color:#888;font-size:12px;margin-top:24px">
                    Ovaj link vrijedi 24 sata. Ako niste vi inicirali registraciju, ignorirajte ovaj email.
                  </p>
                </body></html>
                """.formatted(ownerUsername, ownerPassword, employeeUsername, employeePassword, setupLink);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Greška pri slanju emaila: " + e.getMessage(), e);
        }
    }

}

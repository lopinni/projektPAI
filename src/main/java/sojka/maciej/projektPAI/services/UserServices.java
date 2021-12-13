package sojka.maciej.projektPAI.services;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sojka.maciej.projektPAI.entities.User;
import sojka.maciej.projektPAI.entities.UserRepo;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class UserServices {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        repo.save(user);
        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "kombajn5280@gmail.com";
        String senderName = "ProjektPAI";
        String subject = "Potwierdzenie rejestracji";
        String content = "Drogi/a [[name]],<br>"
                + "Kliknij tutaj aby potwierdzić rejestrację:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\"> POTWIERDŹ </a></h3>"
                + "Pozdrawiam,<br>"
                + "ProjektPAI.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName()+" "+user.getLastName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public boolean verify(String verificationCode) {
        User user = repo.findByVerificationCode(verificationCode);
        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            repo.save(user);
            return true;
        }
    }

    public void deleteUser(Long id) { if(repo.existsById(id)) repo.deleteById(id); }

    public void updateUser(Long id, User u){
        if(repo.existsById(id)){
            u.setId(repo.findById(id).get().getId());
            u.setPassword(
                    passwordEncoder.encode(u.getPassword()));
            repo.save(u);
        }
    }

}

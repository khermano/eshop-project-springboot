package cz.muni.fi.userservice.sampledata;

import cz.muni.fi.userservice.entity.User;
import cz.muni.fi.userservice.service.UserService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class SampleDataLoading {
    @Autowired
    private UserService userService;

    final static Logger log = LoggerFactory.getLogger(SampleDataLoading.class);


    private static Date toDate(int year, int month, int day) {
        return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    private User user(String password, String givenName, String surname, String email, String phone, Date joined, String address) {
        User u = new User();
        u.setGivenName(givenName);
        u.setSurname(surname);
        u.setEmail(email);
        u.setPhone(phone);
        u.setAddress(address);
        u.setJoinedDate(joined);
        if(password.equals("admin")) u.setAdmin(true);
        return u;
    }

    @PostConstruct
    public void loadUserSampleData() {
            String pepaJiriEvaPassword = "heslo";
            String adminPassword = "admin";
            User pepa = user(pepaJiriEvaPassword, "Pepa", "Novák", "pepa@novak.cz", "603123456", toDate(2015, 5, 12), "Horní Kotěhůlky 12");
            User jiri = user(pepaJiriEvaPassword, "Jiří", "Dvořák", "jiri@dvorak.cz", "603789123", toDate(2015, 3, 5), "Dolní Lhota 56");
            User eva = 	user(pepaJiriEvaPassword, "Eva", "Adamová", "eva@adamova.cz", "603457890", toDate(2015, 6, 5), "Zadní Polná 44");
            User admin = user(adminPassword, "Josef", "Administrátor", "admin@eshop.com", "9999999999", toDate(2014, 12, 31), "Šumavská 15, Brno");

            userService.registerUser(pepa, pepaJiriEvaPassword);
            userService.registerUser(jiri, pepaJiriEvaPassword);
            userService.registerUser(eva, pepaJiriEvaPassword);
            userService.registerUser(admin, adminPassword);
            log.info("Loaded eShop users.");
    }
}

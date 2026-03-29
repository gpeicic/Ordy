## ORDY
Backend za praćenje cijena dobavljača namijenjen ugostiteljskim objektima. 
Aplikacija se spaja na hrvatski sustav moj-e-račun, automatski 
dohvaća ulazne račune i omogućuje usporedbu cijena po dobavljačima kroz vrijeme.

## Što radi

- Automatski dohvat i parsanje UBL XML računa s moj-e-račun platforme
- Fuzzy matching naziva proizvoda između različitih računa 
- Usporedba cijena istog proizvoda kroz vrijeme i između dobavljača
- Generiranje narudžbenica u PDF formatu i slanje na mail dobavljača
- Klijent ima evidenciju povijesti računa, usporedba cijena različitih proizvoda te pristup
  naručivanju od različitih dobavljača

## Tech stack

- Java 21 / Spring Boot
- MyBatis + PostgreSQL
- JWT autentikacija
- iText7 (PDF generiranje)
- Spring Mail (SMTP)

## Pokretanje

1. Kloniraj repo
2. Postavi `application.properties` prema `application.properties.example`
3. Pokreni PostgreSQL i kreiraj bazu
4. `mvn spring-boot:run`

## Environment varijable
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/eureka
spring.datasource.username=
spring.datasource.password=
jwt.secret=
spring.mail.username=
spring.mail.password=
```

## Status

U aktivnom razvoju. JWT autentikacija implementirana, frontend u planu.

Wiktor Krzyżanowski Z501
Nr albumu: 8178

Projekt zaliczeniowy laboratoria z  przedmiotu JIPP5.

Ogólny opis projektu:

Celem projektu było utworzenie aplikacji symulującej proste forum internetowe na którym użytkownicy mogą zakładać swoje tematy oraz pisać posty. Jak to przystało na dzisiejsze czasy użytkownicy mogą oceniać posty innych użytkowników na plus oraz na minus, ciekawym featurem jest możliwość przemyślenia swojego posta i dodania mu oceny :D Założona została prymitywna architektura mikroserwisowa opierająca się o dwa (users oraz posts) web seriwsy wystawiające REST'owe API pozwalające na interakcje z nimi. Backend został zaimplementowany przy użyciu frameworka Spring Boot + JWT (w dużym skrócie). W celu reprezentacji danych i realizowania pewnej logiki forum internetowego został stworzony także client oparty o Angulara 7 oraz Bootstrapa (nie jestem mistrzem CSS) umożliwiający pełną interakcję oraz manipulację danymi. W ramach rozwoju projektu przychodziły mi coraz to nowe pomysły na realizację pewnej nowej logiki biznesowej oraz dodatkowych zachowań, jednakże realizacja tych pomysłów została odłożona w czasie.

Linki do repozytorium:
- Users Service: https://github.com/wkrzyzanowski/forum-users-api
- Posts Service: https://github.com/wkrzyzanowski/forum-posts-api
- Forum Client: https://github.com/wkrzyzanowski/forum-client

Narzędzia potrzebne do uruchomienia projektu:
- JDK 11.0.4 LTS
- Maven 3.6.0
- NodeJS 12.14.0 + NPM 6.13.4
- Angular CLI 7.3.0

Pozostałe wymagania do uruchomienia projektu:
- Wolne porty: 9091 (users-service), 9092 (posts-service), 4200 (forum-client)
- Zmieniając porty należy pamiętać, także o zmianie konfiguracji Angular Proxy !
- Po instalacji niezbędnych node_modules projekt będzie zajmował ~ 300MB

Instrukcja uruchomienia:
1. Users Service:
 - otworzyć nowe okno command line w folderze "forum-users-api"
 - uruchomić polecenie "mvn clean spring-boot:run"
 - serwis powinien być teraz dostępny pod adresem http://localhost:9091/
 - można to zweryfikować "curl http://localhost:9091/" w odpowiedzi powinno przyjść HTTP 403 Forbidden

2. Posts Service:
 - otworzyć nowe okno command line w folderze "forum-posts-api"
 - uruchomić polecenie "mvn clean spring-boot:run"
 - serwis powinien być teraz dostępny pod adresem http://localhost:9092/
 - można to zweryfikować "curl http://localhost:9092/" w odpowiedzi powinno przyjść HTTP 403 Forbidden

3. Forum Client:
 - otworzyć nowe okno command line w folderze "forum-client"
 - uruchomić polecenie "npm install" w celu instalacji potrzebnych bibliotek ~30 sekund
 - uruchomić program poleceniem "npm start" i w przeglądarce wejść na adres: http://localhost:4200/

Dodatkowe informacje:
 - w celu uproszczenia konfiguracji projektu została użyta relacyjna baza danych H2 przechowująca dane w pamięci
komputera w czasie działania serwisów: users-service oraz posts-service
 - dostęp do bazy danych możliwy jest poprzez:
	
	USERS-SERVICE:
	LINK: http://localhost:9091/usersdb
	JDBC URL: jdbc:h2:mem:usersdb
	USER: sa
	PASS: (empty)

	POSTS-SERVICE:
	LINK: http://localhost:9092/postdb
	JDBC URL: jdbc:h2:mem:postsdb
	USER: sa
	PASS: (empty)

 - baza danych podczas uruchamiania poszczególnych serwisów inicjowana jest początkowymi wartościami w tym
   udostępnieniem dwóch użytkowników, którzy mogą korzystać z aplikacji:
	
	LOGIN: test1@test.com
	PASSWORD: test

	LOGIN: test2@test.com
	PASSWORD: test

 - w projekcie zaimplementownany jest moduł security opierający się na JWT (Json Web Token) o ważności 
   tokena ~ 2000 min w celach demonstracyjncyh - dostęp do web-serwisów możliwy jest tylko po podaniu tokena z 
   wyjątkiem endpointów do logowania oraz tworzenia użytkowników


 - przegląd dostępnych końcówek RESTowych do poszczególnych serwisów dostępny jest pod adresami:
	
	USERS-SERVICE: http://localhost:9091/swagger-ui.html
	POSTS-SERVICE: http://localhost:9092/swagger-ui.html
    



# Sistem-za-izdavanje-licnih-dokumenata
Sistem se sastoji iz tri podsistema. Klijentske aplikacije, servisa i tajmera. Klijentska aplikacija na odredjeni interval vremena, a ciji signal dobija od podsistema za vreme putem Java Messaging Service-a, salje zahtev centralnom serveru putem REST zahteva i zatim ceka odgovor.

# Java ISDS

[![Build Status](https://travis-ci.org/czgov/java-isds.svg?branch=master)](https://travis-ci.org/czgov/java-isds)

Original project [JAVA_ISDS](https://github.com/xrosecky/JAVA_ISDS) was developed by Vaclav Rosecky.  
Goal of this fork is to provide stable builds in Maven Central and continuous integration on Travis CI.

Original javadoc and documentation was written in Czech language.



## Contributions
We welcome contributions of all kinds. 

## Original library description in Czech language

*Multiplatformní knihovna v Javě pro přístup k ISDS (informačnímu systému datových schránek).*

Knihovna se skládá ze čtyř modulů:

1) ISDSCommon -- knihovna definující společné rozhraní pro obě implementace.

3) ISDSWebServices -- vygenerované webové služby.

2) TinyISDS -- minimalistická knihovna pro přístup k ISDS, podporuje stažení
seznamu přijatých zpráv, stažení zprávy a získání haše zprávy. Ostatní operace
nejsou podporovány.

4) ISDS -- knihovna pro přístup k ISDS s plnou funkcionalitou, tzn.
odesílání zpráv, ověření integrity stažených zpráv, vyhledávání datových
schránek a podobně.

Build se provádí mavenem, takže jen stačí spustit příkaz `mvn clean install` a ten se
o vše potřebné postará.

# Java ISDS

**Project is no longer being actively maintained.**

[![Build Status](https://travis-ci.org/czgov/java-isds.svg?branch=master)](https://travis-ci.org/czgov/java-isds)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cz.abclinuxu.datoveschranky/isds/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.abclinuxu.datoveschranky/isds)
[![Javadocs](http://javadoc.io/badge/cz.abclinuxu.datoveschranky/isds.svg)](http://javadoc.io/doc/cz.abclinuxu.datoveschranky/isds)

Original project [JAVA_ISDS](https://github.com/xrosecky/JAVA_ISDS) was developed by Vaclav Rosecky.  
Goal of this fork is to provide stable builds in Maven Central and continuous integration on Travis CI.

Original javadoc and documentation was written in Czech language.

## Usage

Maven users will need to add following dependency to their `pom.xml`:
```xml
<dependency>
    <groupId>cz.abclinuxu.datoveschranky</groupId>
    <artifactId>isds</artifactId>
    <version>x.x.x</version>
</dependency>
```

Example for downloading all messages received in last 30 days from test ISDS account:
```java
// init manager for connecting to test environment with following credentials
String username = "YOUR_USERNAME";
String password = "YOUR_PASSWORD";
Config config = new Config(DataBoxEnvironment.TEST);
Authentication auth = new BasicAuthentication(config, username, password);
DataBoxManager manager = new DataBoxManager(config, auth);

// fetch messages for last 30 days
Date from = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30));
Date to = new Date();

// assume we won't have to handle paging in demo :)
// fetch all message envelopes in given time scope
int offset = 1;
int limit = Integer.MAX_VALUE;
// filter = null -> don't filter
EnumSet<MessageState> filter = null;
System.out.println(String.format("fetching envelopes received from %s to %s", from, to));
List<MessageEnvelope> envelopes = manager
        .getDataBoxMessagesService()
        .getListOfReceivedMessages(from, to, filter, offset, limit);

// create directory for message attachments
File dirForAttachments = new File("dir-for-attachments");
dirForAttachments.mkdirs();
AttachmentStorer storeForMessageAttachments = new FileAttachmentStorer(dirForAttachments);
for (MessageEnvelope env : envelopes) {
    // download and unmarshal each message
    // optionally there is method downloadSIgnedMessage which returns *.zfo binary content
    Message message = manager.getDataBoxDownloadService().downloadMessage(env, storeForMessageAttachments);
    System.out.println(String.format("Message %s from %s contained: %s",
            env.getAnnotation(), env.getSender(), message.getAttachments()));
}
```

## OSGi ready
This library is OSGi ready. 
It provides `features.xml` for easy installation into 
[Apache Karaf](http://karaf.apache.org/) or [JBoss Fuse](https://developers.redhat.com/products/fuse/overview/).

Installation into JBoss Fuse 6.3 would look like this:  
```bash
# download features.xml from maven repo to fuse -- replace snapshot version for appropriate one
JBossFuse:karaf@root> features:addurl mvn:cz.abclinuxu.datoveschranky/karaf/1.0.0-SNAPSHOT/xml/features

# download jars and install them to fuse
JBossFuse:karaf@root> features:install isds
```

**Note** Fuse should check maven central for release artifacts out of the box.
Snasphot artifacts must be explicitelly enabled in fuse config (config file `$FUSE_HOME/etc/org.ops4j.pax.url.mvn.cfg`).

**Note** Following commands work for JBoss Fuse 6.x and Karaf 2.x. 
Commands in Karaf 3.x onwards have different names.


To verify everything is installed correctly:
```bash
JBossFuse:karaf@root> osgi:list
START LEVEL 100 , List Threshold: 50
   ID   State         Blueprint      Spring    Level  Name
# note that bcprov was already present in fuse
[ 293] [Active     ] [            ] [       ] [   80] JavaISDS :: Common (1.0.0.SNAPSHOT)
[ 294] [Active     ] [            ] [       ] [   80] JavaISDS :: JavaISDS (1.0.0.SNAPSHOT)
[ 295] [Active     ] [            ] [       ] [   80] JavaISDS :: Web Services (1.0.0.SNAPSHOT)
[ 296] [Active     ] [            ] [       ] [   80] bcpkix (1.54)
```

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

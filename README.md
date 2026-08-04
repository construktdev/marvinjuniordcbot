# FootInfoBot ⚽🤖

Ein vielseitiger Discord-Bot für den Dynamo-Dresden-Fan-Server, geschrieben in Java mit [JDA](https://github.com/discord-jda/JDA). Der Bot kombiniert Spieltags- und News-Benachrichtigungen mit einer Reihe von Moderations- und Community-Funktionen.

> [!NOTE]
> Dieses Projekt war früher als *MarvinjuniorBot* bekannt. Die Zusammenarbeit wurde inzwischen beendet – der Bot ist nun ein rein generischer Discord-Bot.

> [!IMPORTANT]
> Dies ist ein kleines, privates Projekt. Es wird **kein Support** geleistet.

---

## Inhaltsverzeichnis

- [Features](#features)
- [Tech-Stack](#tech-stack)
- [Projektstruktur](#projektstruktur)
- [Installation](#installation)
- [Konfiguration](#konfiguration)
- [Verwendung](#verwendung)
- [Slash-Commands](#slash-commands)
- [Lizenz](#lizenz)

---

## Features

### ⚽ Spieltag- & Ergebnis-Updates
- Automatischer Embed-Versand zu jedem Spieltag mit Informationen zum anstehenden Spiel
- Nach Spielende wird automatisch ein Ergebnis-Embed gepostet
- Regelmäßiger Abgleich des Spielstatus per geplantem Intervall-Job

### 📰 News-Feed
- Automatisches Posten der neuesten [kicker](https://kicker.de)-News zu Dynamo Dresden via RSS
- Stündlicher Abgleich, ob ein neuer Artikel vorliegt (kein Duplicate-Posting)
- Pausiert automatisch an Spieltagen, um Spam zu vermeiden

### 👋 Willkommens-System
- Begrüßungs-Embed für neue Mitglieder inkl. Avatar und Regelverweis
- Automatische Vergabe einer „Verifiziert"-Rolle beim Beitritt

### 🔔 Benachrichtigungs-Panel
- Admin-Command zum Versenden eines Panels mit Button
- Nutzer können darüber selbstständig eine Streamer-Benachrichtigungsrolle an-/abwählen

### 🛡️ Moderation & Anti-Scam
- **Anti-Spam:** Automatisches Timeout bei zu vielen Nachrichten in kurzer Zeit
- **Anti-URL:** Erkennung und Filterung von Links per Regex
- **Honeypot-System:** Dedizierter Köder-Kanal zur Erkennung und zum Bann von Spam-Bots
- **Nuke-Command:** Schnelles Zurücksetzen eines Kanals (löscht alle Nachrichten)

### 🧰 Weitere Utility-Commands
- `/whois` – Detaillierte Nutzerinformationen abrufen
- `/purge` – Mehrere Nachrichten auf einmal löschen
- Automatischer Wechsel des Bot-Status (Activity Shift)
- Umfangreiches Logging (Datei- und Discord-Log-Channel)

---

## Tech-Stack

| Komponente          | Technologie                                                             |
|---------------------|--------------------------------------------------------------------------|
| Sprache             | Java 21                                                                   |
| Discord-API-Wrapper | [JDA](https://github.com/discord-jda/JDA) 6.1.0                          |
| Build-Tool          | Maven                                                                     |
| Scheduling          | [Quartz Scheduler](http://www.quartz-scheduler.org/) 2.3.2               |
| RSS-Verarbeitung    | [ROME](https://github.com/rometools/rome) 1.19.0                         |
| JSON-Verarbeitung   | Jackson Databind 2.20.0                                                  |
| Logging             | Logback 1.5.13 (SLF4J)                                                   |
| Code-Style          | Checkstyle (an `checkstyle.xml` gebunden, läuft in der `validate`-Phase) |

---

## Projektstruktur

```
FootInfoBot/
├── src/main/java/de/construkter/footinfobot/
│   ├── Main.java                     # Einstiegspunkt, Bot-Setup & Job-Scheduling
│   ├── config/                       # Laden der config.properties
│   ├── logging/                      # Datei- & Discord-Logging
│   ├── moderation/
│   │   ├── antiScam/                 # Honeypot-System
│   │   ├── antiSpam/                 # Spam-Erkennung & Timeouts
│   │   ├── antiUrls/                 # Link-Filter
│   │   └── utils/                    # z. B. Nuke-Command
│   └── modules/
│       ├── activityShift/            # Automatischer Status-Wechsel
│       ├── commands/                 # /whois, /purge
│       ├── matches/                  # Spieltag-Embeds & Ergebnisse
│       ├── news/                     # RSS-News-Feed
│       ├── notifications/            # Benachrichtigungs-Panel
│       ├── systemLogging/            # System-Events
│       └── welcome/                  # Willkommens-Nachrichten
├── src/main/resources/logback.xml
├── config.properties.example
├── checkstyle.xml
├── pom.xml
└── LICENSE
```

---

## Installation

### Voraussetzungen

- Java Development Kit **21** oder neuer
- [Maven](https://maven.apache.org/) 3.8+
- Ein Discord-Bot-Token ([Discord Developer Portal](https://discord.com/developers/applications))

### Repository klonen

```bash
git clone https://github.com/construktdev/FootInfoBot.git
cd FootInfoBot
```

### Bauen

```bash
mvn clean package
```

Das erzeugt eine ausführbare `.jar`-Datei (inkl. aller Abhängigkeiten) im `target/`-Verzeichnis dank des `maven-assembly-plugin`.

---

## Konfiguration

Kopiere die Beispieldatei und passe sie an:

```bash
cp config.properties.example config.properties
```

Die Datei muss im Arbeitsverzeichnis liegen, aus dem der Bot gestartet wird, und folgende Werte enthalten:

| Key               | Beschreibung                                                       |
|-------------------|----------------------------------------------------------------------|
| `token`           | Bot-Token aus dem Discord Developer Portal                          |
| `guild`           | Guild-ID des Haupt-Servers                                           |
| `mainLog`         | Channel-ID für den Haupt-Log-Kanal                                  |
| `welcomeChannel`  | Channel-ID für Willkommensnachrichten                               |
| `verifiedRole`    | Rollen-ID der „Verifiziert"-Rolle                                    |
| `memberRole`      | Rollen-ID der Member-Rolle                                          |
| `streamRole`      | Rollen-ID der Streamer-Benachrichtigungsrolle                       |
| `gamedayChannel`  | Channel-ID für Spieltag-Nachrichten                                  |
| `newsChannel`     | Channel-ID für Team-News                                             |

> [!TIP]
> IDs erhältst du in Discord, indem du den Entwicklermodus aktivierst (Einstellungen → Erweitert) und mit Rechtsklick auf Server, Kanal oder Rolle „ID kopieren" wählst.

---

## Verwendung

Bot starten:

```bash
java -jar target/FootInfoBot-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Optional lässt sich der Spieltag-Job beim Start einmalig manuell auslösen:

```bash
java -jar target/FootInfoBot-1.0-SNAPSHOT-jar-with-dependencies.jar --test
```

Beim erfolgreichen Start gibt der Bot den Invite-Link in der Konsole aus und registriert automatisch alle Slash-Commands für die verbundenen Guilds.

---

## Slash-Commands

| Command                          | Berechtigung | Beschreibung                                                  |
|-----------------------------------|--------------|-----------------------------------------------------------------|
| `/notifications <channel>`        | Admin        | Sendet das Panel zur Verwaltung von Stream-Benachrichtigungen  |
| `/purge <amount>`                 | Admin        | Löscht eine angegebene Anzahl an Nachrichten (2–100)            |
| `/whois <user>`                   | Alle         | Zeigt detaillierte Informationen zu einem Nutzer                |
| `/nuke`                           | Manage Channel | Erstellt den aktuellen Kanal neu und löscht damit alle Inhalte |

---

## Lizenz

Dieses Projekt steht unter der [Apache License 2.0](LICENSE).

---

<div align="center">

**FootInfoBot** – Forza SGD 🖤💛

</div>

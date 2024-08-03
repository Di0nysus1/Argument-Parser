# ArgumentParser LIB

Ein einfacher, erweiterbarer ArgumentParser für Java, der es ermöglicht, Konsolenargumente flexibel zu verarbeiten. Der Parser unterstützt sowohl Argumente mit Werten als auch sogenannte "EmptyArguments" (Schalter ohne Werte).

## Funktionen

- **Flexible Argumente:** Unterstützt sowohl obligatorische als auch optionale Argumente.
- **Alias-Unterstützung:** Argumente können alternative Namen (Aliase) haben.
- **Standardwerte:** Wenn ein Argument nicht übergeben wird, kann ein Standardwert verwendet werden.
- **Automatische Hilfeausgabe:** Der Parser kann eine Hilfeausgabe generieren, wenn `-help` als Argument übergeben wird.

## Installation

Kopiere die Java-Klassen (`ArgumentParser`, `ValueArgument`, `EmptyArgument`) in dein Projekt und integriere sie wie unten beschrieben.

## Verwendung

Hier ist ein Beispiel, wie du den `ArgumentParser` verwenden kannst:

```java
import de.dion.argumentparser.*;

public class Main {
    public static void main(String[] args) {
        // Erstellen einer neuen ArgumentParser-Instanz
        ArgumentParser ap = new ArgumentParser();
        
        // Hinzufügen von Argumenten
        ap.add(new ValueArgument("headless", true, "Determines if the browser runs in headless mode. If TRUE, the windows will be invisible."));
        ap.add(new ValueArgument("lang", "en:de", "Specifies the language pair for translation.").alias("language"));
        ap.add(new ValueArgument("translator", "deepl", "Specifies the translation service to use. Possible values: \"google\" or \"deepl\""));
        ap.add(new ValueArgument("driverpath", "C:\\path\\to\\geckodriver.exe", "Path to the Geckodriver binary.").alias("path").required());
        ap.add(new EmptyArgument("penis2").description("Ein Test Argument"));

        // Parsen der Argumente
        ap.parseArguments(args);
        
        // Abrufen der Argumentwerte
        boolean headless = ap.getBoolean("headless");
        File driverPath = ap.getFile("driverpath");
        String lang = ap.getString("lang");
        String translator = ap.getString("translator");

        // Verwendung der abgerufenen Argumente
        System.out.println(ap.getArgument("penis2").hasBeenSet());
        System.out.println(headless);
    }
}

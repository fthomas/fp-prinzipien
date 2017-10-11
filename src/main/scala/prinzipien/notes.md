
## Intro

* was ist FP
* programmieren mit reinen Funktion. Beispiele was nicht reine Funktionen
  sind.
* FP ist eine Einschränkung wie wir Programme schreiben und nicht welche.
*



Aufgabe: Wir wollen ein kleines rein funktionales Programm schreiben,
dass mit dem Benutzer interagiert und eine "DB" verwendet.

Dazu gehen wir wie folgt vor. Wie drücken unser Programm als einen Wert
aus, der beschreibt welche Effekte ausgeführt werden sollen. Diese Werte
und Funktionen, die damit arbeiten, sind rein funktional. Dann schreiben
wir einen Interpreter, der diesen Wert nimmt und die Seiteneffekte ausführt.
Dies ist die einzige nicht referentiell transparente Funktion in unserem Code
und sie wird nur in unserer main Methode verwendet.

## io

Wir beginnen mit der Interaktion mit dem Benutzer

* ConsoleIO1: Wir drücken unser Programm als einen Wert aus der beschreibt
  welche Effekte ausgeführt werden sollen. Für uns reichen readLine und
  println, daher zwei case classes ReadLine() und PrintLine(string).
  Ein ConsoleIO1 ist entweder das Eine oder das Andere (sealed trait).
  Der Typparameter steht für den Typ des Rückgabewerts jeder einzelnen Aktion.
  
* ConsoleIO1: Die Programme, die wir mit dieser Datentyp bauen könne sind
  langweilig. Entweder ist es nur eine Aktion oder wir könnten vielleicht
  eine List[ConsoleIO1] mit mehreren *unabhängigen* Aktionen haben. Aber
  wir können noch kein Programm ausdrücken, dass eine Zeile einliest und
  diese Zeile wieder ausgibt.

  Lösungen: Eine neue case class, die genau das macht (ReadLine und dann
  PrintLine) oder wir bauen eine generische Aktion, die eine ConsoleIO[A]
  und mit derem Ergebnis eine weitere ConsoleIO erzeugt. => Das ist FlatMap
  
* ConsoleIO2: FlatMap zeigen.

* App1: Jetzt können wir unser erstes "interessanteres" Programm mit ConsoleIO2
  schreiben: Ein paar PrintLines, gefolgt von einem ReadLine und einem PrintLine,
  das die eingegebene Zeile wieder ausgibt. `program: ConsoleIO2[Unit]` ist eine
  ConsoleIO-Programm, das einen Wert vom Typ `Unit` zurückgibt und es wurde aus
  reinen Funktionen aufgebaut.
  
* Interpreter1: Zum Ausführen brauchen wir jetzt noch einen Interpreter.
  <Kurz erklären> und dann App1 ausführen.
  
* App2: App1 ist simpel. Wie schaffen es eine Schleife zu bauen, die uns solange
  grüßt bis wir "exit" eingeben?
  
* App3: dem Teilprogramm, das wir wiederholen wollen, geben wir einen Namen (loop)
  und verwenden es dort wo es wieder beginnen soll. Demo. Was haben wir geschafft?
  Ein interaktives Programm mit einer Schleife gebaut aus Werten und reinen Funktionen.
  
## kv

Jetzt wollen wir Werte in eine Datenbank schreiben und daraus lesen. Unsere DB ist
ein einfacher Key-Value-Store.

* KVStore1: Wie ConsoleIO2 nur mit unseren Get und Put Operationen anstatt Readline
  und PrintLine. Operationen näher erklären (Eingaben und Ausgaben).

* App1: Mit KVStore1 können wir unser erstes einfaches Programm schreiben, das
  Aktionen auf einem Key-Value-Store ausführt. Wie interpretieren wir so ein Programm?

* Interpreter1: Die run Funktion macht aus einem KVStore1[A] kein A wie es der ConsoleIO2
  Interpreter gemacht hat. Stattdessen gibt er uns eine Funktion zurück (Store => (Store, A).
  Das ist ein referentiell transparenter Interpreter. Hier werden keine Seiteneffekte
  ausgeführt. Ich verbinde mich nicht mit einer DB und führe dort irgendwelche Aktionen aus.
  Stattdessen wird eine Funktion erzeugt, die auf einem Store Aktionen ausführt und
  einen geänderten Store und einen Wert liefert. Store ist eine immutable Map, d.h.
  er wird auch nicht in place geändert.

* So ein Interpreter ist interessant für Tests und eine alternativen zum Mocken. Für Tests und
  Produktion werden einfach unterschiedliche Interpreter verwendet.

* App2: Wie können wir innerhalb eines KVStore1-Programms bei einem Get einen Default
  angeben fals kein entsprechendes Mapping im K-V-Store verfügbar ist. Anstatt Option[String]
  wollen wir als Rückgabetyp ein String. Eine Möglichkeit ist eine neue primitive Operation
  "GetOrElse(key, default): KVStore[String]" zu implementieren. Eine weitere Möglichkeit
  ist eine Funktion, die einen beliebigen Wert in unser Programm bringt: A => KVStore[A].

* KVStore2: Pure und map erklären. Damit lässt sich dann getOrElse als abgeleitete Operation
  definieren:
  
    def getOrElse(key: String, default: String): KVStore2[String] =
      Get(key).map(_.getOrElse(default))
      
* App3: Zeigen wie wir Pure verwenden und wie man mit flatMap und map seine Programme
  in einer for-comprehension schöner schreiben kann.
  
## app

Nächster Schritt: ConsoleIO und KVStore kombinieren, um ein Programm zu bauen mit dem
interaktiv einen Key-Value-Store abfragen und modifizieren kann.

* ConsoleIO / KVStore: Wie in den Beispielen davor nur ohne FlatMap und Pure. Ziel ist beide
  zu kombinieren, daher hilft uns flatMap nur für ConsoleIO und KVStore nicht unbedingt weiter

* Wir müssen Operationen von beiden Sprachen in eine gemeinsame Sprache übersetzen.
  Dazu überlegen wir uns einen Datentyp in den wir sowohl ConsoleIO als auch KVStore
  Operationen übersetzen können.
  
* Task: kapselt beliebige Seiteneffekte in Delay.

* Interpreter zeigt wie ConsoleIO und KVStore in Task übersetzt werden kann. Dies sind
  reine Funktionen. Die einzige nicht-rt Funktion ist unsafeRun: Task[A] => A weil hier
  Funktionen in Delay ausgeführt werden.
  
* App: Programm zeigen und demonstrieren 

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
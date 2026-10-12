# Teste JUnit 5 pentru laboratorul OOP

Suita contine 25 de teste simple in 7 clase. Fiecare metoda foloseste `@Test`
si verifica un comportament: pregateste obiectele, apeleaza metoda aplicatiei,
apoi verifica rezultatul. Nu exista teste parametrizate, mocking sau helperi.

## Organizare

| Package | Clasa | Teste | Ce verifica |
| --- | --- | ---: | --- |
| Operations | OperationsTest | 5 | Adunare, scadere, inmultire, impartire cu rezultat fractionar si impartire la zero |
| Operations | OperatorRegistryTest | 2 | Inregistrarea unui operator si respingerea unui simbol necunoscut |
| Numbers | ComplexNumbersTest | 3 | Constructor/gettere si afisarea componentelor imaginare pozitive sau negative |
| Parsers | ComplexParserTest | 4 | Semne, coeficient imaginar implicit, spatii si input malformat |
| Parsers | ExpressionParserTest | 3 | Construirea expresiei, operator necunoscut si operand invalid |
| Expressions | ExpressionEvaluatorTest | 4 | Exemplul complet, precedenta si asociativitatea scaderii/impartirii |
| Validators | ExpressionArgumentsValidatorTest | 4 | Structura valida, argumente lipsa, numar par de argumente si operator gol |
| **Total** | | **25** | |

Componentele numerelor sunt `float`. Comparatiile numerice folosesc direct
`assertEquals(expected, actual, 0.0001f)`, pentru a permite mici diferente de
rotunjire la valorile din aceste exemple. Fiecare rezultat complex este verificat
prin ambele componente. `toString()` se verifica separat, prin compararea exacta
a textului, inclusiv spatiile si partea zecimala.

Pentru input invalid, `assertThrows` verifica exceptia. Impartirea la zero
produce `ArithmeticException`, iar argumentele invalide testate produc
`IllegalArgumentException`. Testul structurii valide apeleaza direct validatorul:
va esua daca apelul arunca o exceptie, fara sa fie necesara o asertiune suplimentara.

## Inputul aplicatiei

Fiecare operand si fiecare operator reprezinta un argument separat:

```text
"3+4i" "*" "2-5i" "+" "1+2i"
```

Testul complet trece prin parser si evaluator si verifica rezultatul `27-5i`.
Parserul accepta componente intregi in text, de exemplu `-2-3i`, `2+i` sau
`3+4*i`. Spatiile din operanzi sunt eliminate, iar operatorii sunt curatati de
spatiile de la margini. Valorile sunt stocate ca `float`; operatiile pot produce
rezultate fractionare. Nu sunt testate ca functionalitati acceptate parantezele,
componentele zecimale in text sau numerele reale/imaginare fara forma completa.

Evaluatorul aplica `*` si `/` inainte de `+` si `-`. Operatorii cu aceeasi
precedenta se evalueaza de la stanga la dreapta. Exemplele `10 - 3 - 2` si
`8 / 2 / 2` verifica aceasta ordine fara sa implice suport pentru paranteze in input.

## Rulare

Proiectul foloseste JUnit Jupiter 5.11.4, inclus in JUnit Platform Console
Standalone 1.11.4. Daca JAR-ul lipseste, descarca-l o singura data din directorul
proiectului:

```powershell
New-Item -ItemType Directory -Path lib -Force
Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar' -OutFile 'lib\junit-platform-console-standalone-1.11.4.jar'
```

Ruleaza toate testele din directorul proiectului:

```powershell
.\run-tests.ps1
```

Optional, selecteaza explicit un JDK instalat:

```powershell
.\run-tests.ps1 -JdkHome 'C:\Users\tudor\.jdks\openjdk-27'
```

Scriptul compileaza separat `src/` si `Teste/`, descopera testele si afiseaza
rezumatul executiei. Clasele compilate si rapoartele XML sunt scrise intr-un
director nou sub `out/junit/`, a carui cale apare in consola. Codul de iesire
este 0 pentru succes si nenul pentru erori de compilare sau teste esuate.
In IntelliJ, pastreaza `src/` ca sursa si `Teste/` ca sursa de teste, cu biblioteca
JUnit 5 configurata, apoi ruleaza directorul `Teste/`.

Suita este intentionat restransa la exemple reprezentative pentru laborator.
Au fost eliminate combinatiile repetitive, verificarea identitatii obiectelor,
partajarea listelor si cazurile extreme de float. Rezultatele asteptate sunt
calculate independent; un test esuat trebuie investigat, nu ajustat pentru a
ascunde o eroare din implementare. Rezumatul si rapoartele fiecarei executii
inlocuiesc vechiul raport static, care nu mai descria versiunea curenta.

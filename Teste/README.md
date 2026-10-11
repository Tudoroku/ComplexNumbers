# JUnit 5 tests

The existing IntelliJ source roots remain `src/` and `Teste/`. The module's
test-scoped JUnit5 library uses the standalone JUnit Platform 1.11.4 jar
(including JUnit Jupiter 5.11.4 and parameterized-test support).

Download the dependency once from Maven Central, from the project directory:

```powershell
New-Item -ItemType Directory -Path lib -Force
Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar' -OutFile 'lib\junit-platform-console-standalone-1.11.4.jar'
```

Run all tests:

```powershell
.\run-tests.ps1
# Or select an installed JDK explicitly:
.\run-tests.ps1 -JdkHome 'C:\Users\tudor\.jdks\openjdk-27'
```

The runner compiles production and tests separately, discovers all test classes,
and writes XML reports to a fresh directory under `out/junit/`, printed at
startup. It returns a nonzero exit status for compilation or test failures.
In IntelliJ, reload the module/library configuration and run the `Teste` folder.

## Actual input grammar

`Main` passes command-line arguments directly to `ExpressionParser.parse(String[])`.
Each complex operand is one argument, and each binary operator is a separate
argument. For example:

```text
"3+4i" "*" "2-5i" "+" "1+2i"
```

This represents the product of 3+4i and 2-5i followed by adding 1+2i; the
independently calculated result is 27-5i. All five requested reference cases
are expressible in this grammar and covered at operation and integration levels.

The parser's current regular expression requires an integer-form real component,
an imaginary sign, an optional nonnegative integer-form coefficient, and
lowercase `i`. These components are stored as floats. Examples: `-2-3i`,
`+2+i`, `0+0i`, and `3+4*i`. Whitespace inside an operand is removed;
operator arguments are trimmed. Quote operands containing spaces in the shell.
The parser removes asterisks inside operand arguments; multiplication between
operands still needs a separate `"*"` argument.

Bare reals, bare imaginary numbers, decimal components, a whole expression in
one argument, and unregistered operators are unsupported. The application
intentionally does not accept parenthesized input; no test requires it.
Although `Float.parseFloat` is now used, the regular expression still rejects
decimal points and exponent notation. Fractional constructor/operation tests
therefore use the float API directly; integration tests obtain fractional
results by dividing operands in the supported input grammar.

The parser preserves operator order. The evaluator applies multiplication and
division (precedence 2) before addition and subtraction (precedence 1), and
equal-precedence operations associate left to right.

## Test expectations

Expected arithmetic results are literal independently calculated values.
Complex results are checked by both float components. The shared assertion uses
an absolute tolerance of `1e-6f` near zero and a relative tolerance of `1e-6f`
for larger expected values; `1e-9` would be too strict for general float
arithmetic. Cases cover fractional inputs, decimal rounding such as
0.1 + 0.2, and nonintegral division including one third. Constructors also
cover finite float extremes, with exact checks for the smallest nonzero float.
Parser cases beyond the integer range now expect successful float parsing and
float rounding, rather than an integer overflow exception.

No exact division-by-zero exception contract exists, so
tests require an unchecked failure without prescribing a specific subtype.

Production bugs must remain failing tests. Zero division and truncation of
negative fractional imaginary components in `toString()` remain covered.
String expectations follow the existing whole-number formatting while
requiring fractional magnitudes to be preserved.

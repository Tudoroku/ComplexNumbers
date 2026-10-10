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

An operand needs an integer real component, an imaginary sign, an optional
nonnegative integer coefficient, and lowercase `i`. Examples: `-2-3i`,
`+2+i`, `0+0i`, and `3+4*i`. Whitespace inside an operand is removed;
operator arguments are trimmed. Quote operands containing spaces in the shell.
The parser removes asterisks inside operand arguments; multiplication between
operands still needs a separate `"*"` argument.

Bare reals, bare imaginary numbers, decimal components, a whole expression in
one argument, and unregistered operators are unsupported. The application
intentionally does not accept parenthesized input; no test requires it.

The parser preserves operator order. The evaluator applies multiplication and
division (precedence 2) before addition and subtraction (precedence 1), and
equal-precedence operations associate left to right.

## Test expectations

Expected arithmetic results are literal independently calculated values.
Complex results are checked by both components. Components and constructors
are `int`, so no fractional component API or floating-point tolerance is
applicable. Division cases have exact integer results and avoid imposing a
rounding policy. No exact division-by-zero exception contract exists, so
tests require an unchecked failure without prescribing a specific subtype.

Production bugs must remain failing tests. No tests are disabled or weakened
to accommodate the division stub.

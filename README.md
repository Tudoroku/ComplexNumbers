# ComplexNumbers

A Java command-line application that parses and evaluates arithmetic expressions containing **complex numbers**. Built as an object-oriented programming project, it focuses on separating parsing, arithmetic operations, and expression evaluation into distinct components.

## Features

- **Complex-number arithmetic:** addition (`+`), subtraction (`-`), multiplication (`*`), and division (`/`).
- **Operator precedence:** multiplication and division are evaluated before addition and subtraction; operators with equal precedence are evaluated left to right.
- **Multiple operations:** evaluates expressions containing more than two operands.
- **Input validation:** rejects malformed complex numbers, unknown operators, and invalid operand/operator sequences.
- **Extensible operation design:** arithmetic behaviors implement a common `Operation` interface and are registered by symbol.
- **JUnit 5 tests:** includes unit and integration tests for the arithmetic and expression-processing components.

## Example

The program accepts **one command-line argument per operand or operator**:

```powershell
java -cp out\production Main "3+4i" "*" "2-5i" "+" "1+2i"
```

Output:

```text
27 - 5i
```

This evaluates `(3 + 4i) × (2 - 5i) + (1 + 2i)`, with multiplication performed first.

## Architecture

The code is organized by responsibility:

| Component | Responsibility |
| --- | --- |
| `Numbers.ComplexNumbers` | Represents a complex number with integer real and imaginary components. |
| `Parsers.ComplexParser` | Converts an individual complex-number argument into a `ComplexNumbers` object. |
| `Parsers.ExpressionParser` | Validates the argument sequence and constructs an `Expression`. |
| `Expressions.Expression` | Holds the ordered operands and operations. |
| `Expressions.ExpressionEvaluator` | Evaluates an expression using two stacks and operator precedence. |
| `Operations.Operation` | Defines the contract for an arithmetic operation: `apply`, `getSymbol`, and `getPrecedence`. |
| `Operations.AddOperation`, `SubtractOperation`, `MultiplyOperation`, `DivideOperation` | Implement the four supported arithmetic operations. |
| `Operations.OperatorRegistry` | Registers operations and resolves them from their symbols. |
| `Main` | Wires the components together and runs the command-line application. |

**Evaluation flow:**

```text
Command-line arguments
         |
         v
   ExpressionParser ----> ComplexParser
         |                OperatorRegistry
         v
     Expression
         |
         v
 ExpressionEvaluator
    (two stacks)
         |
         v
   ComplexNumbers result
```

### Object-oriented design

- **Strategy pattern:** each arithmetic operation is a separate implementation of the `Operation` interface.
- **Single responsibility:** number parsing, expression parsing, evaluation, and arithmetic are handled by separate classes.
- **Extensibility:** another binary operator can be introduced by implementing `Operation` and registering it in `Main` (provided the input syntax remains compatible).

## Project structure

```text
ComplexNumbers/
├── src/
│   ├── Main.java
│   ├── Numbers/
│   │   └── ComplexNumbers.java
│   ├── Parsers/
│   │   ├── ComplexParser.java
│   │   └── ExpressionParser.java
│   ├── Expressions/
│   │   ├── Expression.java
│   │   └── ExpressionEvaluator.java
│   └── Operations/
│       ├── Operation.java
│       ├── OperatorRegistry.java
│       ├── AddOperation.java
│       ├── SubtractOperation.java
│       ├── MultiplyOperation.java
│       └── DivideOperation.java
├── Teste/                 # JUnit tests and testing instructions
├── run-tests.ps1          # PowerShell test runner
└── .gitignore
```

## Getting started

### Requirements

- A Java Development Kit (JDK), with `java` and `javac` available in your terminal.
- PowerShell for the commands below. Alternatively, open `src/Main.java` in IntelliJ IDEA and configure the program arguments.

### Compile and run (PowerShell)

```powershell
git clone https://github.com/Tudoroku/ComplexNumbers.git
cd ComplexNumbers

New-Item -ItemType Directory -Path out\production -Force | Out-Null
$sources = Get-ChildItem .\src -Recurse -Filter '*.java' |
    ForEach-Object { $_.FullName }
javac -encoding UTF-8 -d out\production $sources

java -cp out\production Main "3+4i" "*" "2-5i" "+" "1+2i"
```

### Input format and limitations

- Each operand and operator must be passed as a **separate argument**, alternating between them: `operand operator operand ...`.
- Complex-number operands have an integer real part and an imaginary part, such as `3+4i`, `-2-3i`, `+2+i`, or `0+0i`.
- Use lowercase `i`. Spaces inside a quoted operand are ignored.
- Parentheses, standalone real or imaginary numbers, and decimal components are **not supported** by the parser.
- Complex-number components are stored as Java `int` values. In particular, division uses **integer arithmetic**, so fractional components are truncated; arithmetic overflow is also possible.

## Testing

Tests use **JUnit 5**, via the JUnit Platform Console Standalone **1.11.4** JAR. Since that dependency is not committed to the repository, download it once from the project root:

```powershell
New-Item -ItemType Directory -Path lib -Force | Out-Null
Invoke-WebRequest `
    -Uri 'https://repo.maven.apache.org/maven2/org/junit/platform/junit-platform-console-standalone/1.11.4/junit-platform-console-standalone-1.11.4.jar' `
    -OutFile 'lib\junit-platform-console-standalone-1.11.4.jar'

.\run-tests.ps1
```

The script compiles application and test sources, discovers the tests, and writes JUnit XML reports under `out/junit/`. For details, see [`Teste/README.md`](Teste/README.md).

## Purpose

This project was developed to practice **Java OOP, interface-based design, parsing, operator precedence, and automated testing** in a small but extensible codebase.


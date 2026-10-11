# Test report

Verified on 2026-10-11 with OpenJDK 27 and JUnit Jupiter 5.11.4
(JUnit Platform standalone 1.11.4).

Production and test sources compiled successfully on both complete runs.
Each run executed **176 tests: 169 passed, 7 failed, 0 skipped, 0 aborted**.
JUnit XML records 7 assertion failures and 0 errors. The runner correctly
returns exit code 1 because the production bugs remain unfixed.
SHA-256 checks against the snapshot taken at the start of this float adaptation
confirmed every production source file is unchanged by this task.

## Classes modified

Adapted eight existing test classes and the shared assertion helper for the
float model. Registry and expression-parser tests remain applicable without
code changes. All ten test classes are included in the complete suite.

| Test class | Executed | Passed | Failed | Skipped |
|---|---:|---:|---:|---:|
| Numbers.ComplexNumbersTest | 22 | 20 | 2 | 0 |
| Operations.AddOperationTest | 9 | 9 | 0 | 0 |
| Operations.SubtractOperationTest | 9 | 9 | 0 | 0 |
| Operations.MultiplyOperationTest | 10 | 10 | 0 | 0 |
| Operations.DivideOperationTest | 16 | 12 | 4 | 0 |
| Operations.OperatorRegistryTest | 11 | 11 | 0 | 0 |
| Parsers.ComplexParserTest | 35 | 35 | 0 | 0 |
| Parsers.ExpressionParserTest | 23 | 23 | 0 | 0 |
| Expressions.ExpressionTest | 4 | 4 | 0 | 0 |
| Expressions.ExpressionEvaluatorTest | 37 | 36 | 1 | 0 |
| **Total** | **176** | **169** | **7** | **0** |

Updated `Support.TestSupport` to compare float components using absolute and
relative tolerances of 1e-6. Numeric parameterized tests now take float arguments.
Added fractional arithmetic, rounding, finite float extremes, fractional
expression storage, and nonintegral evaluation cases. Replaced obsolete
integer-range rejection tests with successful float parsing/rounding cases.
The existing runner, JUnit library, and source roots remain unchanged.

## Investigated implementation bugs

1. **Division by zero is not rejected.** Four operation tests and one integration
   test fail because a zero divisor produces NaN components rather than an
   exception. Floating-point division by zero does not automatically throw.
   Suggested fix: reject a divisor whose real and imaginary components are
   both zero, for example with `ArithmeticException`. Tests deliberately
   require only an unchecked exception because no exact subtype is documented.

2. **String formatting truncates negative fractional imaginary components.**
   Two tests fail: 2.5-0.75i is rendered as `2.5 - 0i`, and -2.5-3.75i is
   rendered as `-2.5 - 3i`. The cast to long discards the fractional part.
   Suggested fix: preserve the float magnitude when formatting, e.g.
   `-imaginary`, with an explicit formatting policy for whole numbers.

The previously reported division stub has been replaced by the user's
implementation. All nonzero-divisor arithmetic tests now pass, including
fractional results. The old integer-negation overflow test was replaced by
float-specific representation and extreme-value tests because the model
no longer has integer components.

All 7 failing tests remain enabled with mathematically correct expectations.
There were no compilation errors introduced by the tests and no incorrect
test expectations found during failure review.

## Supported and unsupported syntax

Input is an alternating `String[]` of complete complex operands and binary
operators. Negative signs and the imaginary-component sign belong inside
the operand argument. Whitespace inside operands and around operator arguments
is supported. A single operand is valid. Precedence is applied by the evaluator,
with equal-precedence operations evaluated left to right.

Bare real or imaginary numbers, decimal components, a whole expression in one
argument, exponent notation, and unknown operators are unsupported. The parser
now stores floats using `Float.parseFloat`, but its regular expression still
only matches integer-form components. Decimal operands are therefore tested
through the constructor and operations. Parser/evaluator integration tests
use supported integer-form inputs and division to obtain fractional results.
Parenthesized expressions are intentionally unsupported; no tests require them.
All five requested mathematical reference cases are represented in the actual
argument-array grammar.

## Reproduction and detailed results

Run `.\run-tests.ps1` from the project directory. See `Teste/README.md`
for dependency setup and JDK selection.

The final verification log is `out/junit/float-verification.log`.
Final XML reports are in
`out/junit/da8f73c6-228f-47d1-bd05-ac606eac14f0/reports/`.
Generated classes, logs, and reports stay under the existing ignored `out/`
directory.

# Test report

Verified on 2026-10-08 with OpenJDK 27 and JUnit Jupiter 5.11.4
(JUnit Platform standalone 1.11.4).

Production and test sources compiled successfully on both complete runs.
Each run executed **150 tests: 131 passed, 19 failed, 0 skipped, 0 aborted**.
JUnit XML records 19 assertion failures and 0 errors. The runner correctly
returns exit code 1 because the production bugs remain unfixed.
SHA-256 checks against the initial snapshot confirmed every production
source file is unchanged.

## Classes modified

All ten existing classes were empty placeholders and are now implemented.

| Test class | Executed | Passed | Failed | Skipped |
|---|---:|---:|---:|---:|
| Numbers.ComplexNumbersTest | 16 | 15 | 1 | 0 |
| Operations.AddOperationTest | 7 | 7 | 0 | 0 |
| Operations.SubtractOperationTest | 7 | 7 | 0 | 0 |
| Operations.MultiplyOperationTest | 8 | 8 | 0 | 0 |
| Operations.DivideOperationTest | 11 | 1 | 10 | 0 |
| Operations.OperatorRegistryTest | 11 | 11 | 0 | 0 |
| Parsers.ComplexParserTest | 35 | 35 | 0 | 0 |
| Parsers.ExpressionParserTest | 23 | 23 | 0 | 0 |
| Expressions.ExpressionTest | 4 | 4 | 0 | 0 |
| Expressions.ExpressionEvaluatorTest | 28 | 20 | 8 | 0 |
| **Total** | **150** | **131** | **19** | **0** |

Added `Support.TestSupport` for assertions on both integer components and
a registry fixture. Added a PowerShell runner, an IntelliJ test-scoped JUnit
library, and dependency/setup documentation without changing the source roots.

## Investigated implementation bugs

1. **Division is a stub.** `src/Operations/DivideOperation.java` returns
   `new ComplexNumbers(1, 1)` for every input. Seven arithmetic unit cases and
   seven evaluator cases fail because of this behavior. For example, dividing
   3+4i by 1-2i should produce -1+2i, but produces 1+1i.
   Suggested fix: for left a+bi and right c+di, compute the denominator
   c*c+d*d, the real numerator a*c+b*d, and the imaginary numerator b*c-a*d.
   Use adequately wide intermediates. Because the model stores integers,
   establish an explicit policy for nonintegral quotients before implementing
   them; the current tests use only exact integer quotients.

2. **Division by zero succeeds.** Three operation tests and one integration
   test fail because a zero divisor also produces 1+1i.
   Suggested fix: reject a divisor whose real and imaginary components are
   both zero, for example with `ArithmeticException`. Tests deliberately
   require only an unchecked exception because no exact subtype is documented.

3. **String formatting overflows for the minimum integer imaginary value.**
   One test fails: `new ComplexNumbers(0, Integer.MIN_VALUE).toString()`
   returns `0 - -2147483648i` instead of `0 - 2147483648i`.
   Suggested fix: widen before negation, e.g. `-(long) imaginary`.

All 19 failing tests remain enabled with mathematically correct expectations.
There were no compilation errors introduced by the tests and no incorrect
test expectations found during failure review.

## Supported and unsupported syntax

Input is an alternating `String[]` of complete complex operands and binary
operators. Negative signs and the imaginary-component sign belong inside
the operand argument. Whitespace inside operands and around operator arguments
is supported. A single operand is valid. Precedence is applied by the evaluator,
with equal-precedence operations evaluated left to right.

Bare real or imaginary numbers, decimal components, a whole expression in one
argument, and unknown operators are unsupported. Components are integers, so
fractional-value tests and a floating-point tolerance are not applicable.
Parenthesized expressions are intentionally unsupported; no tests require them.
All five requested mathematical reference cases are represented in the actual
argument-array grammar.

## Reproduction and detailed results

Run `.\run-tests.ps1` from the project directory. See `Teste/README.md`
for dependency setup and JDK selection.

The final verification log is `out/junit/verification.log`.
Final XML reports are in
`out/junit/0aae621e-55fa-4748-8f91-8e4685b98a0f/reports/`.
Generated classes, logs, and reports stay under the existing ignored `out/`
directory.

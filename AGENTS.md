# AGENTS.md

## Communication instructions
- Use ASD-STE100 standard for communication.
- Explanations must be detailed if asked for.

## Developer instructions
- Only the changes I ask you to make should be made.
- Always use idiomatic Kotlin.
- Adherence to SOLID principles and other good practices.
- Refactors must be small (60-100 lines).
- If something is not implemented, use TODO(reason).
- Every dependency addition must be properly justified.
- Do not remove comments I've written.
- Follow `OPINIONS.md` wherever possible.

## Testing instructions
- New feature must have tests.
- Strictly avoid mocks.

## Guardrails
- Do not access gitignored files.
- Do not access files outside of Ludo directory.
- Refactors cannot be made without prior permission.

## Important files
- `OPINIONS.md`: Opinions about code quality standards and methods.

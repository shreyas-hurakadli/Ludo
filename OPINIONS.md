# Opinions

## Prefer `when` Statements Over Nested `if-else`

Nested `if-else` statements are generally discouraged. Prefer `when` statements when handling multiple conditions, as they provide a flatter and more readable control flow.

### Avoid

```kotlin
if (condition1) {
    if (nestedCondition1) {
        // Handle nested condition 1
    } else if (nestedCondition2) {
        // Handle nested condition 2
    } else {
        // Handle other cases
    }
} else {
    if (nestedCondition3) {
        // Handle nested condition 3
    } else {
        // Handle other cases
    }
}
```

### Prefer
```kotlin
when (x) {
    condition1 -> handleCondition1()
    else -> handleOtherCondition()
}
```

### Avoid trailing commas
Just a personal choice
# Contribution Guidelines

First of all, thank you for supporting Javacord with your contribution!
This guide will help you to create high quality pull requests.

## Important links

* [Javacord Discord Server](https://discord.gg/Javacord)

## Commit Best Practices

While there are no strict rules for commit messages, it is strongly recommended to read 
[How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/).

## Testing

When you create pull requests, make sure your changes work by testing them.
If your PR is untested, please mark it as a draft.

## Code Conventions

Javacord follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with a few exceptions:

* Column limit: `120` (instead of `100`)
* Block indentation: `4 spaces` (instead of `2 spaces`)
* Having annotations in the same line as the declaration is **not** allowed:
  ```java
  // Incorrect
  @Override public int hashCode() {
      // ...
  }
  
  // Correct
  @Override
  public int hashCode() {
      // ...
  }
  ```
* Ignored exceptions must be called `ignored`:
  ```java
  try {
      int i = Integer.parseInt(response);
      return handleNumericResponse(i);
  } catch (NumberFormatException ignored) {
      // it's not numeric; that's fine, just continue
  }
  return handleTextResponse(response);
  ```
* You are **not** allowed to omit braces!
  ```java
  // Incorrect
  if (condition)
      return;
  ```
  ```java
  // Correct
  if (condition) {
      return;
  }
  ```
* Acronyms must also be written in camel case! Also try to avoid acronyms that are not commonly known.
  ```java
  // Incorrect
  HTTPRequest
  ```
  ```java
  // Correct
  HttpRequest
  ```
* Name your methods so that they are understandable without reading the documentation.
  A perfect example of this are the `getXyzByName(...)` methods in Javacord. 
  Instead of having one meaningless boolean parameter to distinguish case, we have two different methods.
  ```java
  // Would you know if this includes servers called "javacord"?
  Collection<Server> servers = api.getServersByName("Javacord", true);
  ```
  ```java
  // With two methods it is much clearer:
  Collection<Server> servers = api.getServersByName("Javacord");
  Collection<Server> servers = api.getServersByNameIgnoreCase("Javacord");
  ```
* Do **not** use single letter variable names!
  ```java
  // Incorrect
  Server s = api.getServerById(123L);
  ```
  ```java
  // Correct
  Server server = api.getServerById(123L);
  ```
* Files must be encoded in `UTF-8 without BOM`
* Static imports must not be used
* Wildcard imports must not be used
* **All** methods must have JavaDoc comments. This also applies to private methods. JavaDocs have the following format:
```java
/**
 * Sets the foo.
 *
 * @param foo The foo to set.
 */
public void setFoo(Foo foo) {
     this.foo = foo;
}
```
```java
/**
 * Gets the foo with the given bar.
 *
 * @param bar The bar of the foo.
 *            If one line is too short, format the next line like this.
 * @return The foo with the given bar.
 */
public void getFooByBar(Bar bar) {
     return null;
}
```
Other formats are not allowed:
```java
/* DO NOT FORMAT IT LIKE THIS OR WITH ANY OTHER "CUSTOM" FORMAT */
/**
 * Gets the foo with the given bar.
 * 
 * @param bar
 *          - The bar of the foo.
 * @return
 *          - The foo with the given bar.
 */
public Foo getFooByBar(Bar bar) {
    return null;
}
```

It is a good idea to familiarize yourself with the style of the code by browsing the code a bit, especially looking for classes or methods that are similar to the ones you are working on.
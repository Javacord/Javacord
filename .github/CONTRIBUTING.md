# Contribution Guidelines

First of all: Thank you for considering to support Javacord with your contribution! 
This guideline should help you to create high quality issues, pull requests, etc.

## Overview

* [Contribution Guidelines](#contribution-guidelines)
  * [Overview](#overview)
  * [Important links](#important-links)
* [Issues](#issues)
  * [When to open an issue?](#when-to-open-an-issue)
  * [What should I put into an issue?](#what-should-i-put-into-an-issue)
    * [Bug report](#bug-report)
    * [Feature suggestion](#feature-suggestion)
* [Pull Requests](#pull-requests)
  * [When to create a Pull Request?](#when-to-create-a-pull-request)
  * [Code Conventions](#code-conventions)
  * [Commit Best Practices](#commit-best-practices)
  * [Testing](#testing)
  * [Unfinished pull requests](#unfinished-pull-requests)
  * [Which branch to base my pull request on?](#which-branch-to-base-my-pull-request-on)
  
## Important links

* [Javacord Discord Server](https://discord.gg/0qJ2jjyneLEgG7y3) (code `0qJ2jjyneLEgG7y3`)

# Issues

## When to open an issue?

Issues are perfect for bug reports or feature suggestions, but please don't create an issue for support.
If you have any problems using Javacord (or any other kind of problem), feel free to join our Discord Server (see [important links](#important-links)).

## What should I put into an issue?

This depends on whether you want to report a bug or suggest a feature.
However, please create a new issue for every single (non related) topic instead of creating an issue which contains many topics.
Please also choose a meaningful title for your issue (negative example: "Found a bug").

### Bug report

A bug report should always contain the version number you are using as well as a detailed description on the bug itself.
If you have an exception, please always include it in the issue as well as the code that caused it.

### Feature suggestion

You are free on how to suggest features, just try to explain it as clear as possible. :-)

# Pull Requests

## When to create a Pull Request?

First of all you should check if creating a pull request is a good idea.
This little checklist should help you to decide if a pull request is the right choice:

* How experienced are you with Java? You should already have pretty good experience with Java (and Javacord) before creating pull requests.
  We appreciate every contribution, but the code should retain a high quality.
  As a beginner there might be better ways of contributing (e.g., reporting bugs, suggesting features, or helping to improve documentation).

Please also create an issue or contact me on our Discord Server (see [important links](#important-links)) server before creating huge pull requests.
There might already someone else working on it or at least there are some plans on how to implement it.
Involving the community in the development process is always a good idea :)

## Code Conventions

Pull requests must respect the code style of the project.
It's recommended to browse through the existing code to get familiar with the style of the project.
These are the code conventions of this project:

* Javacord follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) with a few exceptions:
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
* The following points are not covered by the style guide or are consistent with the style guide but emphasized as they are often ignored: 
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
  * Acronyms must be camel case, too! You should also try to avoid acronyms which are not widely known.
    ```java
    // Incorrect
    HTTPRequest
    ```
    ```java
    // Correct
    HttpRequest
    ```
  * Name your methods in a way that they are understandable without actually reading the docs.
    A perfect example for this are the `getXyzByName(...)` methods in Javacord.
    Instead of having a meaningless boolean parameter for case sensitivity, it's split into two different methods.
    ```java
    // Would you know if this would contain servers which are called "javacord" as well?
    Collection<Server> servers = api.getServersByName("Javacord", true);
    ```
    ```java
    // Instead it's split into two different methods which makes everything clear without looking into the docs:
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
* **All** method must have JavaDoc comments. This also includes private methods. JavaDocs are in the following format:
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
   
   * @param bar
   *          - The bar of the foo.
   * @return
   *          - The foo with the given bar.
   */
  public Foo getFooByBar(Bar bar) {
      return null;
  }
  ```
  It's also recommended taking a look at similar classes, methods, etc. before writing your JavaDocs.
  For example all listener classes have the same format (`This listener listens to xyz.`).
  This is the case for a lot of other comments as well, which are very similar.

Most parts of this style-guide are automatically validated when building the project.

## Commit Best Practices

There are no strict rules on how commits should look like, but here are some guidelines which you should keep in mind:

* Keep your commits small. If you want to implement a large feature, try to split the commits into small but working pieces.
* Choose meaningful commit names (e.g., `Fix NPE when sending messages` instead of just `Bugfix`). 
  Please also do not use slang in your commits (e.g., `Ups, I fucked up in my last commit, lol. Fixed it now!`).
* Do not commit anything other than code! IDE specific files must not be committed (e.g., the `.idea` folder in IntelliJ).
* Keep the commit history linear. Rebase your commits and do not introduce merge commits.
* Do not include memes or easter eggs in your code. That's my job!

While we do not enforce a specific format for commit messages, this blog post should be a must-read for every good developer:
[How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/).

## Testing

Do **not** create pull requests that are untested.
There were pull requests in the past starting with something like `This is not tested, but ...`.
If it takes more time to review the pull request than implementing it, it's no help.
Depending on the pull request it might also be good to describe how you tested your code.
In some rare cases (e.g. if you only improved documentation), it's not necessary to test the code.

## Unfinished pull requests

You are allowed create pull requests before they are finished.
This is especially useful for large pull requests as you can discuss your changes with other users or/and ask for opinions.
Please mark the pull request with a `[WIP] ` prefix.

## Which branch to base my pull request on?

Always base your pull request on the development branch.
Never commit directly into the master branch!

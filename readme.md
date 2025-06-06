# CP-SAT Advanced Bootcamp - Boilerplate Project :rocket:

> A Kickstarter project for candidates appearing for CP-SAT exam Advanced Exam

> Please note that this project is created to be used in IntelliJ IDEA.

## ⛏️ Built with

<a href="https://www.java.com" target="_blank"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="50" height="80"> </a>
&nbsp;&nbsp;&nbsp;
<a href="https://www.selenium.dev" target="_blank"> <img src="https://upload.wikimedia.org/wikipedia/commons/9/9f/Selenium_logo.svg" alt="selenium" width="180" height="80"> </a>

## Table of Contents

- [Getting Started](#small_airplane-getting-started)
    - [Pre-requisite](#desktop_computer-prerequisite)
    - [Validate Installations](#shield-validate-installations)
- [Forking the Repository](#hammer_and_wrench-forking-the-repository)
- [Validating the Fork](#validating-the-fork)
- [Run Boilerplate Tests](#arrow_forward-run-boilerplate-tests)
- [Project Dependencies](#dependabot-project-dependencies)

## :small_airplane: Getting Started

### :desktop_computer: Prerequisite

Before running this project, please make sure that following libraries are installed in your machine.

- [Java JDK 16 or higher](https://www.oracle.com/in/java/technologies/downloads/)
- [Node-Js LTS](https://nodejs.org/en)
- Git ([Windows](https://gitforwindows.org/) / [Mac](https://git-scm.com/download/mac))
- Download Maven from [here](https://maven.apache.org/download.cgi) & set `MAVEN_HOME` as environment variable by
  following [this](https://www.qamadness.com/knowledge-base/how-to-install-maven-and-configure-environment-variables/)
  guide

### :shield: Validate Installations

To check whether all prerequisites have been correctly installed, run the following instructions and see if you get the
expected results.

```bash
java --version
```

```bash
node --version
npm --version
```

```bash
git --version
```

```bash
mvn --version
```

:memo: Please keep in mind that the version number in the output of the above commands may differ from what is shown in
the screenshot, and it should represent the version number of the libraries that you have installed.

## :hammer_and_wrench: Forking the Repository

### Step-by-Step Guide to Fork the Repo

1. **Navigate to the Repository**
    - Go to the GitHub repository you want to fork. For example, visit
    - Repository Link: IntelliJ IDEA Project
      > [https://github.com/prajapati-hitesh/cpsat-advanced-bootcamp-practice](https://github.com/prajapati-hitesh/cpsat-advanced-bootcamp-practice)
2. **Click on the Fork Button**
    - In the top-right corner of the repository page, click the `Fork` button.
      > ![Step 2 - Click on the Fork Button](https://i.ibb.co/Pzmz4Vsg/image.png)

3. **Select Your Account**
    - Choose your GitHub account or an organization where you want to fork the repository.
    - ![Step 3 - Select Your Account](https://i.ibb.co/Wv6Jrdc6/image.png)

4. **Wait for the Forking Process**
    - GitHub will create a copy of the repository under your account. This may take a few seconds.

## Validating the Fork

### How to Validate the Repo is Forked Under Your Username

1. **Check the Repository URL**
    - After forking, ensure you are redirected to your forked repository. The URL should be
      > `https://github.com/[your-username]/cpsat-advanced-bootcamp-practice`

2. **Verify the Repository Details**
    - Look at the top-left corner of the repository page. You should see `your-username/cpsat-advanced-bootcamp-practice`.

3. **Check the Fork Badge**
    - There should be a `Forked from original-username/repository-name` badge under the repository name.

If all the above steps are confirmed, you have successfully forked the repository under your username.

## :arrow_forward: Run Boilerplate Tests

- Open `cmd` or `terminal` based on your machine and run following command

  ```bash
      mvn clean test
    ``` 
  > _If everything is set up correctly, you should see a browser being launched and a login scenario running
  on [The Internet](https://the-internet.herokuapp.com/) website._

3. To start writing the code, open the project in InteliJ IDE. _(
   i.e. [IntelliJ IDEA](https://www.jetbrains.com/idea/download/))_

## :dependabot: Project Dependencies

This project utilizes several dependencies to provide various functionalities. Below is a table listing each dependency,
its version, and a URL to its repository for more information.

### Project Dependencies

| Dependency Name        | Version    | Link                                                                                                               |
|------------------------|------------|--------------------------------------------------------------------------------------------------------------------|
| assertj-core           | 4.0.0-M1   | [assertj-core](https://search.maven.org/artifact/org.assertj/assertj-core/4.0.0-M1/jar)                            |
| assertj-guava          | 4.0.0-M1   | [assertj-guava](https://search.maven.org/artifact/org.assertj/assertj-guava/4.0.0-M1/jar)                          |
| commons-beanutils      | 1.10.1     | [commons-beanutils](https://search.maven.org/artifact/commons-beanutils/commons-beanutils/1.10.1/jar)              |
| commons-configuration2 | 2.12.0     | [commons-configuration2](https://search.maven.org/artifact/org.apache.commons/commons-configuration2/2.12.0/jar)   |
| fabricator_2.13        | 2.1.9      | [fabricator_2.13](https://search.maven.org/artifact/com.github.azakordonets/fabricator_2.13/2.1.9/jar)             |
| freemarker             | 2.3.34     | [freemarker](https://search.maven.org/artifact/org.freemarker/freemarker/2.3.34/jar)                               |
| gson                   | 2.13.1     | [gson](https://search.maven.org/artifact/com.google.code.gson/gson/2.13.1/jar)                                     |
| guava                  | 33.4.8-jre | [guava](https://search.maven.org/artifact/com.google.guava/guava/33.4.8-jre/jar)                                   |
| jackson-annotations    | 2.19.0     | [jackson-annotations](https://search.maven.org/artifact/com.fasterxml.jackson.core/jackson-annotations/2.19.0/jar) |
| jackson-core           | 2.19.0     | [jackson-core](https://search.maven.org/artifact/com.fasterxml.jackson.core/jackson-core/2.19.0/jar)               |
| javafaker              | 1.0.2      | [javafaker](https://search.maven.org/artifact/com.github.javafaker/javafaker/1.0.2/jar)                            |
| javatuples             | 1.2        | [javatuples](https://search.maven.org/artifact/org.javatuples/javatuples/1.2/jar)                                  |
| joda-time              | 2.14.0     | [joda-time](https://search.maven.org/artifact/joda-time/joda-time/2.14.0/jar)                                      |
| jsoup                  | 1.10.1     | [jsoup](https://search.maven.org/artifact/org.jsoup/jsoup/1.10.1/jar)                                              |
| junit-jupiter          | 5.13.0-RC1 | [junit-jupiter](https://search.maven.org/artifact/org.junit.jupiter/junit-jupiter/5.13.0-RC1/jar)                  |
| junit-platform-runner  | 1.13.0-RC1 | [junit-platform-runner](https://search.maven.org/artifact/org.junit.platform/junit-platform-runner/1.13.0-RC1/jar) |
| log4j-api              | 2.24.3     | [log4j-api](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-api/2.24.3/jar)                       |
| log4j-core             | 2.24.3     | [log4j-core](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-core/2.24.3/jar)                     |
| log4j-slf4j-impl       | 2.24.3     | [log4j-slf4j-impl](https://search.maven.org/artifact/org.apache.logging.log4j/log4j-slf4j-impl/2.24.3/jar)         |
| oshi-core              | 6.8.1      | [oshi-core](https://search.maven.org/artifact/com.github.oshi/oshi-core/6.8.1/jar)                                 |
| passay                 | 1.6.6      | [passay](https://search.maven.org/artifact/org.passay/passay/1.6.6/jar)                                            |
| poi                    | 5.4.1      | [poi](https://search.maven.org/artifact/org.apache.poi/poi/5.4.1/jar)                                              |
| poi-ooxml              | 5.4.1      | [poi-ooxml](https://search.maven.org/artifact/org.apache.poi/poi-ooxml/5.4.1/jar)                                  |
| QuickChart             | 1.2.0      | [QuickChart](https://search.maven.org/artifact/io.quickchart/QuickChart/1.2.0/jar)                                 |
| rest-assured           | 5.5.2      | [rest-assured](https://search.maven.org/artifact/io.rest-assured/rest-assured/5.5.2/jar)                           |
| rgxgen                 | 2.0        | [rgxgen](https://search.maven.org/artifact/com.github.curious-odd-man/rgxgen/2.0/jar)                              |
| selenium-java          | 4.32.0     | [selenium-java](https://search.maven.org/artifact/org.seleniumhq.selenium/selenium-java/4.32.0/jar)                |
| selenium-shutterbug    | 1.6        | [selenium-shutterbug](https://search.maven.org/artifact/com.assertthat/selenium-shutterbug/1.6/jar)                |
| slf4j-simple           | 2.0.17     | [slf4j-simple](https://search.maven.org/artifact/org.slf4j/slf4j-simple/2.0.17/jar)                                |
| webdrivermanager       | 6.1.0      | [webdrivermanager](https://search.maven.org/artifact/io.github.bonigarcia/webdrivermanager/6.1.0/jar)              |
| zip4j                  | 2.11.5     | [zip4j](https://search.maven.org/artifact/net.lingala.zip4j/zip4j/2.11.5/jar)                                      |


Each dependency listed here is vital for the functionality and performance of this project. For more detailed
information, visit the URLs provided.

## :magic_wand: Brought to you by

<a href="https://cpsat.agiletestingalliance.org/" target="_blank"><img src="https://cpsat.agiletestingalliance.org/wp-content/uploads/2019/06/abt-logo-unsmushed.png" width="100" height="100"></a>
&nbsp;
<a href="https://www.agiletestingalliance.org/" target="_blank"><img src="https://www.agiletestingalliance.org/wp-content/uploads/2021/02/ATA-logo.png" width="110" height="110"></a>

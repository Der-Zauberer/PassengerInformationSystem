# Countributing

## Project Setup

1. Clone the project with `git clone https://github.com/Der-Zauberer/PassengerInformationSystem` in your project workspace
2. If you are using eclipse:
    * Click on `File > New > Java Project` in the program menu
    * Uncheck `Use default location` and select your cloned project folder (name and other fields should be filled automatically after that)
    * Click on `Finish`
    * Don't create the module-info if eclipse asks
    * Do a right-click on the project in the Package-Explorer on the left side and select `Configure > Convert to Maven Project`
    * Do a right-click on the project in the Package-Explorer on the left side and select `Maven > Update Project...` and select `OK`
3. If you are using IntelliJ:
    * Click on `New > Project from Existing Source...` in the program menu
    * Select your cloned project folder
    * Select `Maven` and click on `Create`
4. Enable lombok in your IDE [Guide](https://www.baeldung.com/lombok-ide)
5. You can run the project as Java Application in your IDE
6. You can export the project with the maven goal `package` in your IDE or with `maven package` in the command line if maven is installed

## Conventional Commits

Please conventional commit messages on that project:
* for features `git commit -m "feat: message"`
* for fixes `git commit -m "fix: message"`
* for documentations `git commit -m "doc: message"`
The commits have to be in present tense and rarely contain verbs (ex. `feat: authentication process in service`)

## Usage

The webinterface runns at `http://localhost:8080`. You can login with the standard user, username and password are both `admin`.

You can find the Open-API definition at `http://localhost:8080/api-docs` and swagger at `http://localhost:8080/swagger-ui/index.html`.

## Issues

### Feature Request Template

```
<persona> wants <feature> because of <benefit>

**Acceptance criteria:**
  * ...
  * ...
```

Add the `enhancement` label on the creation of the issue.

Example:
```
The passenger wants a departure board of trains for a specific train station because he wants to see if his next train connections are on time and depart at the scheduled platform.

**Acceptance criteria:**
- Departure boards display the next 7 trains at a station
- Station is defined by a URL parameter
- Departures are sorted by departure time with the next departure first
- Departures contain information about the train
  - scheduled departure time,
  - real departure time (if not same as scheduled time)
  - delay in minutes 
  - scheduled destination
  - real destination  (if not same as scheduled destination)
  - scheduled platform
  - real platform (if not same as scheduled platform)
  - additional disruption information (if present)
- Board content is fully translated
```


Add the `bug` label on the creation of the issue.

### Bug Template

```
**Expected Behavior:**
<expected behavior description>

**Actual Behavior:**
<actual behavior description>

**Steps to Reproduce:**
<steps to reproduce description>

**Tested with platform:**
<tested with plattform and sortware and screenshots>
```

## Pull Request

Please link the related issue to the pull request. Pull-Reuests are invalid if they don't have a related issue.

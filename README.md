# theTouchline

A Fantasy Premier League (FPL) tool that pulls live player data from the official FPL API and helps you spot good-value players. So far you can filter the player pool by minutes, price, defensive contribution and position, and rank players by points per million (£m).

The long term goal is to have a web app that can be a companion to the Fantasy Premier League game identifying good value players and having the ability to tinker with team drafts and set ups in the Touchline Lab. I am also hoping to add in team data and news further down the line so it not only looks at players, you would be able to take information on the team too. This is built as a learning project after completing a software engineering bootcamp, with a focus on clean layering, test-first development and understanding the tools rather than copying code.

## Features

- **Live data sync** from the FPL `bootstrap-static` endpoint, mapped into a PostgreSQL database
  - Runs on application startup and on demand via a "Sync Latest Player Data" button (`POST /players/sync`)
  - Upserts players by their FPL id, so existing rows are updated rather than duplicated
  - Team and position names are read from the API response, not hard-coded
- **Player table** showing name, position, team, price, total points, points per million, minutes and defensive contribution per 90
- **Combinable filters**, all optional, where leaving a box empty means "don't apply this condition":
  - Minimum minutes played
  - Maximum price (£m)
  - Minimum defensive contribution per 90 (DefCon)
  - Position (dropdown)
- **Value metric:** points per million, with guards for missing or zero cost and missing points
- **Per-position ranking** in the service layer (`rankValueByPosition`), because a squad has fixed slots (2 GK, 5 DEF, 5 MID, 3 FWD) and players should be compared with others in the same position

- ## Current WIP Screenshots:

The players page, very simple tailwind styling so far - all players on screen with no filtering

<img width="1882" height="897" alt="image" src="https://github.com/user-attachments/assets/17a8791f-c230-4082-a6b5-d3cf12ec0a9c" />




Filtering of players example:

<img width="1901" height="813" alt="image" src="https://github.com/user-attachments/assets/6b99da82-0055-46c6-875c-6bf02e4cd8a9" />

## Tech stack

| Area | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot (Web, Data JPA) |
| Templates | Thymeleaf |
| Styling | Tailwind CSS |
| Database | PostgreSQL |
| Migrations | Flyway |
| Build | Gradle (wrapper included) |
| Boilerplate | Lombok |
| Testing | JUnit 5 |
| Data source | Official FPL API (unofficial and undocumented, so fields can change between seasons) |

## Architecture

```
Controller  ->  Service  ->  Repository  ->  PostgreSQL
(HTTP, params)  (filtering,    (Spring Data JPA)
                 ranking,
                 value logic)

FplApiService: fetches the FPL API, maps it to Player, saves it
```

- **`PlayerController`** reads request parameters, builds a `PlayerFilter`, calls the service and renders the `players` template.
- **`PlayerService`** holds the filtering and ranking logic, with no web or database dependencies, so it can be unit tested with plain objects.
- **`PlayerFilter`** is a Java `record` of optional conditions. A `null` component means "skip this condition".
- **`FplApiService`** fetches and maps the API data, using one query and an in-memory lookup rather than one query per player, inside a transaction.
- **`Player`** is the JPA entity, including derived helpers such as `getCostInMillions()` and `pointsPerMillion()`.

Note that `nowCost` is stored as the API provides it, in tenths of a million (46 means £4.6m).

## Getting started

### Prerequisites

- JDK 21
- PostgreSQL running locally
- Node.js and npm (for the Tailwind build)

### Setup

1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   cd theTouchline
   ```
2. Create a local PostgreSQL database (the dev setup uses `the_touchline_dev`).
3. Configure the database connection for your environment (for example in an `application-local` properties file, which should not be committed if it contains credentials).
4. Build the Tailwind CSS:
   ```bash
   npm install
   # run your Tailwind build or watch script (see package.json)
   ```
5. Run the app:
   ```bash
   ./gradlew bootRun
   ```
6. Open <http://localhost:8080/players>. The first sync runs on startup and populates the table.

Flyway applies the database migrations automatically on startup.

### Running the tests

```bash
./gradlew test
```

## Usage

The filter form on `/players` sends a GET request, so filtered views are bookmarkable:

```
/players?minMinutes=400&maxPrice=6.0&minDefCon=5.0&position=Defender
```

| Parameter | Type | Meaning |
|---|---|---|
| `minMinutes` | integer | Minimum total minutes played |
| `maxPrice` | decimal | Maximum price in £m |
| `minDefCon` | decimal | Minimum defensive contribution per 90 |
| `position` | text | `Goalkeeper`, `Defender`, `Midfielder` or `Forward` (empty means all) |

## Testing approach

The service logic was built test-first. Tests cover:

- Ranking by points per million, overall and within each position
- Each filter on its own, with control players to prove the filter isn't just returning nothing
- Null and zero values (players with no minutes, cost, DefCon or position are excluded when a condition is set)
- Cost and points-per-million edge cases in the `Player` entity

Tests were also checked by deliberately breaking the code and confirming they fail.

## Roadmap

- [ ] Style the page with Tailwind (table, filter bar, responsive layout)
- [ ] Sort options (points, price, DefCon, points per million)
- [ ] Wire per-position ranking into the page
- [ ] Replace position strings with a `Position` enum
- [ ] Store sync snapshots over time to track price, form and ownership changes
- [ ] "Players to watch" list based on that history
- [ ] Auth0 login and a personal watchlist
- [ ] Scheduled sync instead of syncing on every startup

## What I learned

- Layering a Spring Boot app (controller, service, repository) and keeping business logic testable
- Consuming a JSON API with Jackson and avoiding N+1 queries with a lookup map and `@Transactional`
- Designing optional, composable filters with a record and chained stream filters
- Test-driven development with JUnit 5, including null and boundary cases
- Server-rendered pages with Thymeleaf and GET forms that preserve user input

## Disclaimer

This is an unofficial project and is not affiliated with or endorsed by the Premier League or Fantasy Premier League. It relies on an undocumented API that may change without notice.

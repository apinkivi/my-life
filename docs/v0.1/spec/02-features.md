# Features

A user can give AI access to all provided data so it can ask relevant questions and give better suggestions.

## 1. Mental module

### 1.1. Mood tracking

**Description:**
A user can quickly select their current mood from the [Circumplex model](https://en.wikipedia.org/wiki/Emotion_classification#Circumplex_model).  
Both the user and AI can explore mood history later.

Possible values are:
- 😐 Neutral
- 🧐 Alert
- 🤩 Excited
- 😄 Happy
- 😊 Content
- 😌 Relaxed
- 🧘 Calm
- 😑 Bored
- 😞 Depressed
- 😢 Sad
- 😰 Distressed
- 😠 Angry
- 😬 Tense

**User Stories:**
- As a user, I want to select my current mood so I can track my feelings.
- As a user, I want to see my mood history so I can notice patterns.

**Acceptance Criteria:**
- Mood can be set multiple times per day.
- Mood history can be viewed (e.g., as a calendar or chart).
- Neutral mood is shown at the center of the UI.

**UI Mockup:**
Mood selection is visualized as a circumplex, with 😐 Neutral at the center and other moods arranged around it:

```
    😬  🧐
  😠      🤩
😰          😄
      😐
😢          😊
  😞      😌
    😑  🧘
```

## 2. Routine module

**Description:**
Defines customized tasks which repeat cycle is predictable. Shows upcoming, missed and don't break cycle. Those can be marked done and they don't fill calendar, tasks and notifications.

### Parameters

Name|Description|Data Type|Default value
--|--|--|--
workingSchedule|Working or studying days and hours.|Repeating intervals|<ul><li>`R/--W-1T08:00/--W-1T16:00`<li>`R/--W-2T08:00/--W-2T16:00`<li>`R/--W-3T08:00/--W-3T16:00`<li>`R/--W-4T08:00/--W-4T16:00`<li>`R/--W-5T08:00/--W-5T16:00`
morningRoutine|Time after woke up to get work.|Duration|`PT1H30M`
needForSleep|https://www.kaypahoito.fi/nix02713|Duration|By age:<ul><li>6-13 - `PT10H`<li>14-17 - `PT9H`<li>18-64 - `PT8H`<li>65+ - `PT7H30M`

### Predefined tasks

Definition of done|Importance|Repeat|Expire|Description
--|--|--|--|--
Woke up early|Health, important|Working mornings|Working start - morning routine
Beard cut|Health|
Teeth brushed|Health, important|Mornings and evenings
Breakfast eaten|Health, important|Mornings
Dishes in the cupboard|Everyday|Mornings
Lunch eaten|Health, important|T12H/T14H
Dinner eaten|Health, important|T18H/T20H
Tomorrow's food planned|Everyday|Daily
Adequately hydrated|Health, important|Daily
Tomorrow's electricity usage panned|Economic|Daily (14.15 FI)
House is clean|Everyday|Daily
Laundry washed|Everyday|Daily
Bed linen  washed|Everyday|Weekly
Bathrooms cleaned|Everyday|Weekly|Toilet and hand towels washed https://www.martat.fi/kodinhoito/siivous/kylpyhuoneen-puhdistus/
Nails cut|Health|Monthly
Hair cut|Health|Monthly
Seasonal clothes changed|Everyday|Every 6 months
Carpets washed|Everyday|Yearly
Windows washed|Everyday|Yearly
Leaves raked|Everyday|Yearly
Firewood made|Everyday|Yearly
Tax return completed|Everyday|Yearly
Clothes in the closet|Everyday|Daily
Exercised|Health, important|Three times in a week
Unnecessary items discarded|Everyday|Weekly
Dishwashing timed|Everyday|Evenings
Teeth gaps cleaned|Health, important|Evenings
Went to bed on time|Health, important|Evenings before work|Working start - morning routine - need for sleep
Mother's day|Health|Yearly
Father's day|Health|Yearly
Valentine's day|Health|Yearly
Singles Day|Economic|Yearly
Back Friday|Economic|Yearly
Cyber Monday|Economic|Yearly
Teeth checked|Health, important|Every second year

### Additional tasks

Definition of done|Importance|Repeat|Expire
--|--|--|--
Medications taken|Health, important|*Daily*
Lawn mowed|Everyday|Summer weekly
Ventilation maintained|Everyday, important|Every 6 months
Seasonal lighting installed|Everyday|Every 6 months
Car inspected|Everyday, important|Yearly
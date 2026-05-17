package my.life.routine

import my.life.time.Date
import my.life.time.Day
import my.life.time.Day.*
import my.life.time.Month.*
import my.life.time.hour
import my.life.time.hours
import my.life.time.minutes

val average = configure("US", Date(1994), { man = true }) {}

@Suppress("SpellCheckingInspection")
val jukka = configure("FI", Date(1983, 5, 4), {
    man = true
    townHouse = true
}) {
    group(ACTIVITY) {
        daily(WORK, ECONOMIC, 8.hour, 8.hours) {
            days = Day.entries.take(5) - Tuesday
        }
        daily("$WORK.2", ECONOMIC, 7.hour, 8.hours) {
            days(Tuesday)
        }
    }

    // TODO children's school start
    daily("lääkkeet otettu", HEALTH, 1.minutes)
    daily("maski pesty", HEALTH, 3.minutes) { before = BREAKFAST }
    daily("vatsat treenattu", WELFARE, 10.minutes)
    daily("liikuttu", WELFARE, 30.minutes) // Mielummin aamulla
    daily("tavarasta luovuttu", ECONOMIC, 30.minutes) // https://www.is.fi/asuminen/art-2000010094660.html
    weekly("lasten pedit pedattu", ECONOMIC) {
        duration = 30.minutes
    }
    yearly("mikkeliairshow.fi", FREE_TIME) {
        start = October
    }
    yearly("mikkeliwaterweek.fi", FREE_TIME) {
        start = September
    }
}
package my.life.routine

import my.life.time.Day
import kotlin.test.Test
import kotlin.test.assertEquals

class EventPrinterTest {
    @Test fun planMondayByAverage() = assertEquals("""
                 6:36 AM > sleep
                 6:51 AM ) sleep
                 6:52 AM < beard
                 6:57 AM > beard
                 6:58 AM < teeth
                 7:02 AM > teeth
                 7:03 AM < dishes to cupboard
                 7:13 AM > dishes to cupboard
                 7:14 AM < breakfast
                 7:34 AM > breakfast
                 7:35 AM ( work
                 8:00 AM < work
                11:00 AM < lunch
                11:30 AM > lunch
                 2:00 PM < electricity price
                 2:02 PM > electricity price
                 2:30 PM < snack
                 2:40 PM > snack
                 4:00 PM > work
                 5:00 PM < dinner
                 5:45 PM > dinner
                 8:00 PM < supper
                 8:20 PM > supper
                 8:30 PM < dishwash
                 8:40 PM > dishwash
                 9:00 PM < teeth gaps
                 9:05 PM > teeth gaps
                10:36 PM < sleep
        """.trimIndent(), EventPrinter(average).printPlan(Day.Monday))

    @Test fun planMondayForJukka() = assertEquals("""
                 6.36 > uni
                 6.51 ) uni
                 6.52 < parta
                 6.57 > parta
                 6.58 < hampaat
                 7.02 > hampaat
                 7.03 < astiat kaappiin
                 7.13 > astiat kaappiin
                 7.14 < aamupala
                 7.34 > aamupala
                 7.35 ( työ
                 8.00 < työ
                11.00 < lounas
                11.30 > lounas
                14.00 < sähkön hinta
                14.02 > sähkön hinta
                14.30 < välipala
                14.40 > välipala
                16.00 > työ
                16.25 ) työ
                17.00 < päivällinen
                17.45 > päivällinen
                20.00 < iltapala
                20.20 > iltapala
                20.30 < astianpesu
                20.40 > astianpesu
                21.00 < hammasvälit
                21.05 > hammasvälit
                22.51 < uni
        """.trimIndent(), EventPrinter(jukka).printPlan(Day.Monday))

    @Test fun planFridayForJukka() = assertEquals("""
                 6.36 > uni
                 6.51 ) uni
                 6.52 < parta
                 6.57 > parta
                 6.58 < hampaat
                 7.02 > hampaat
                 7.03 < astiat kaappiin
                 7.13 > astiat kaappiin
                 7.14 < aamupala
                 7.34 > aamupala
                 7.35 ( työ
                 8.00 < työ
                11.00 < lounas
                11.30 > lounas
                14.00 < sähkön hinta
                14.02 > sähkön hinta
                14.30 < välipala
                14.40 > välipala
                16.00 > työ
                16.25 ) työ
                17.00 < päivällinen
                17.45 > päivällinen
                20.00 < iltapala
                20.20 > iltapala
                20.30 < astianpesu
                20.40 > astianpesu
                21.00 < hammasvälit
                21.05 > hammasvälit
                22.51 < uni
        """.trimIndent(), EventPrinter(jukka).printPlan(Day.Friday))

    @Test fun planSaturdayForJukka() = assertEquals("""
                 0.01 < uni
                 8.01 > uni
                 8.16 ) uni
                 8.17 < parta
                 8.22 > parta
                 8.23 < hampaat
                 8.27 > hampaat
                 8.28 < astiat kaappiin
                 8.38 > astiat kaappiin
                 8.39 < aamupala
                 8.59 > aamupala
                 9.00 < viikonloppu
                11.00 < lounas
                11.30 > lounas
                14.00 < sähkön hinta
                14.02 > sähkön hinta
                14.30 < välipala
                14.40 > välipala
                15.00 > viikonloppu
                17.00 < päivällinen
                17.45 > päivällinen
                20.00 < iltapala
                20.20 > iltapala
                20.30 < astianpesu
                20.40 > astianpesu
                21.00 < hammasvälit
                21.05 > hammasvälit
        """.trimIndent(), EventPrinter(jukka).printPlan(Day.Saturday))

    @Test fun planSundayForJukka() = assertEquals("""
                 0.01 < uni
                 8.01 > uni
                 8.16 ) uni
                 8.17 < parta
                 8.22 > parta
                 8.23 < hampaat
                 8.27 > hampaat
                 8.28 < astiat kaappiin
                 8.38 > astiat kaappiin
                 8.39 < aamupala
                 8.59 > aamupala
                 9.00 < viikonloppu
                11.00 < lounas
                11.30 > lounas
                14.00 < sähkön hinta
                14.02 > sähkön hinta
                14.30 < välipala
                14.40 > välipala
                15.00 > viikonloppu
                17.00 < päivällinen
                17.45 > päivällinen
                20.00 < iltapala
                20.20 > iltapala
                20.30 < astianpesu
                20.40 > astianpesu
                21.00 < hammasvälit
                21.05 > hammasvälit
        """.trimIndent(), EventPrinter(jukka).printPlan(Day.Sunday))
}
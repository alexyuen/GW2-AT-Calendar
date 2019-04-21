import java.time.temporal.Temporal;
import java.util.Arrays;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class ATCalendar {

    // Data structure to store the four daily ATs and their base start time in UTC
    enum Tournament {

        Balthazar(baseTime(2018, 5, 6, 3, 15)),
        Grenth(baseTime(2018, 5, 5, 10, 15)),
        Lyssa(baseTime(2018, 5, 5, 21, 15)),
        Melandru(baseTime(2018, 5, 5, 16, 15));

        private ZonedDateTime startTime;

        Tournament(ZonedDateTime startTime) {
            this.startTime = startTime;
        }
    }

    // create a ZoneDateTime in UTC
    private static ZonedDateTime baseTime(int year, int month, int day, int hour, int minute) {
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.of("Z"));
    }

    // convert from UTC to local time zone
    private ZonedDateTime fromUTC(ZonedDateTime utc) {
        return utc.withZoneSameInstant(ZoneId.systemDefault());
    }

    public void printSchedule() {
        // get current time
        ZonedDateTime now = ZonedDateTime.now();

        // Find the next occurrance of each daily tournament
        double secondsin23hours = 60 * 60 * 23;
        for (Tournament t : Tournament.values()) {
            // calculate the number of seconds between the base time and now
            long difference = ChronoUnit.SECONDS.between(t.startTime, now);
            // calculate the number of 23 hour periods elapsed since base time
            double hours = Math.ceil(difference / secondsin23hours);
            // add one more 23 hour period
            t.startTime = t.startTime.plusSeconds((long) (hours * secondsin23hours));
        }

        // sort tournaments by earliest time
        Tournament[] next = Tournament.values();
        Arrays.sort(next, (Tournament t1, Tournament t2) -> t1.startTime.compareTo(t2.startTime));

        // print upcoming daily tournaments
        System.out.println("Upcoming ATs starting in:");

        for (Tournament t : next) {
            System.out.println(t + ": " + until(now, t.startTime) + " - "
                    + fromUTC(t.startTime).format(DateTimeFormatter.ofPattern("h:mm a EEEE")));
        }

        // find and print the next monthly tournament
        ZonedDateTime monthly = baseTime(2018, 5, 26, 19, 15);
        while (monthly.isBefore(now)) {
            monthly = monthly.plusMonths(1);
        }
        monthly = monthly.with(TemporalAdjusters.lastInMonth(DayOfWeek.SATURDAY));
        System.out.println("Monthly: " + monthly.format(DateTimeFormatter.ofPattern("MMM d uuuu h:mm a EEEE")) + " (" + ChronoUnit.DAYS.between(now, monthly) + " days)");
    }

    // returns a String with the remaining time in hours and minutes
    private String until(Temporal from, Temporal to) {
        long difference = ChronoUnit.MILLIS.between(from, to);

        //long diffSeconds = difference / 1000 % 60;
        long diffMinutes = difference / (60 * 1000) % 60;
        long diffHours = difference / (60 * 60 * 1000) % 24;
        //long diffDays = difference / (24 * 60 * 60 * 1000);

        return String.format("%d hours %d minutes", diffHours, diffMinutes);
    }

    public static void main(String[] args) {
        new ATCalendar().printSchedule();
    }

}
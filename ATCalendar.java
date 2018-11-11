import java.time.temporal.Temporal;
import java.util.Arrays;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class ATCalendar {

    enum Tournament {

        Balthazar(fromUTC(2018, 5, 6, 3, 15)),
        Grenth(fromUTC(2018, 5, 5, 10, 15)),
        Lyssa(fromUTC(2018, 5, 5, 21, 15)),
        Melandru(fromUTC(2018, 5, 5, 16, 15));

        private ZonedDateTime startTime;

        Tournament(ZonedDateTime startTime) {
            this.startTime = startTime;
        }
    }

    // convert from UTC to local time zone
    private static ZonedDateTime fromUTC(int year, int month, int day, int hour, int minute) {
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.of("Z"))
                .withZoneSameInstant(ZoneId.systemDefault());
    }

    public void printSchedule() {
        // get current time
        ZonedDateTime now = ZonedDateTime.now();

        // find the time for the upcoming tournaments by adding 23 hours to each until they're in the future
        for (Tournament t : Tournament.values()) {
            while (t.startTime.isBefore(now)) {
                t.startTime = t.startTime.plusHours(23);
            }
        }

        // sort tournaments by earliest time
        Tournament[] next = Tournament.values();
        Arrays.sort(next, (Tournament t1, Tournament t2) -> t1.startTime.compareTo(t2.startTime));

        // print upcoming daily tournaments
        System.out.println("Upcoming ATs starting in:");

        for (Tournament t : next) {
            System.out.println(t + ": " + until(now, t.startTime) + " - "
                    + t.startTime.format(DateTimeFormatter.ofPattern("h:mm a EEEE")));
        }

        // find and print the next monthly tournament
        ZonedDateTime monthly = fromUTC(2018, 5, 26, 19, 15);
        while (monthly.isBefore(now)) {
            monthly = monthly.plusMonths(1);
        }
        monthly = monthly.with(TemporalAdjusters.lastInMonth(DayOfWeek.SATURDAY));
        System.out.println("Monthly: " + monthly.format(DateTimeFormatter.ofPattern("MMM d uuuu h:mm a EEEE")));
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
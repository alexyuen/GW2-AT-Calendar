import java.util.Arrays;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.ZonedDateTime;
import java.time.ZoneId;

public class ATCalendar {

    enum Tournament {
        
        Balthazar(fromPST(2018, 5, 5, 20, 15)),
        Grenth(fromPST(2018, 5, 5, 3, 15)),
        Lyssa(fromPST(2018, 5, 5, 14, 15)),
        Melandru(fromPST(2018, 5, 5, 9, 15));

        private LocalDateTime startTime;

        Tournament(LocalDateTime startTime) {
            this.startTime = startTime;
        }
    }

    private static LocalDateTime fromPST(int year, int month, int day, int hour, int minute) {
        return ZonedDateTime.of(year, month, day, hour, minute, 0, 0, ZoneId.of("PST", ZoneId.SHORT_IDS)).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void printSchedule() {
        // get current time
        LocalDateTime now = LocalDateTime.now();

        // find the time for the next tournaments by comparing to the current time
        for (Tournament t : Tournament.values()) {
            while (t.startTime.isBefore(now)) {
                t.startTime = t.startTime.plusHours(23);
            }
        }

        // sort tournaments by earliest time
        Tournament[] next = Tournament.values();
        Arrays.sort(next, (Tournament t1, Tournament t2) -> t1.startTime.compareTo(t2.startTime));

        // print
        for (Tournament t : next) {
            System.out.println(t + ": " + until(now, t.startTime) + " - " + t.startTime.format(DateTimeFormatter.ofPattern("h:mm a EEEE")));
        }

        // find the next monthly tournament
        LocalDateTime monthly = fromPST(2018, 5, 26, 12, 15);
        while (monthly.isBefore(now)) {
            monthly = monthly.plusMonths(1);
        }
        monthly = monthly.with(TemporalAdjusters.lastInMonth(DayOfWeek.SATURDAY));
        System.out.println("Monthly: " + monthly.toString());
    }

    private String until(LocalDateTime from, LocalDateTime to) {
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
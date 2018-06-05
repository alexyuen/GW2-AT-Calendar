import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AutomatedCalendar {

    enum Tournament {
        
        Balthazar(LocalDateTime.of(2018, 5, 5, 20, 15)),
        Grenth(LocalDateTime.of(2018, 5, 5, 3, 15)),
        Lyssa(LocalDateTime.of(2018, 5, 5, 14, 15)),
        Melandru(LocalDateTime.of(2018, 5, 5, 9, 15));

        private LocalDateTime startTime;

        Tournament(LocalDateTime startTime) {
            this.startTime = startTime;
        }
    }

    public void printSchedule() {
        LocalDateTime now = LocalDateTime.now();

        for (Tournament t : Tournament.values()) {
            while (t.startTime.isBefore(now)) {
                t.startTime = t.startTime.plusHours(23);
            }
        }

        Tournament[] next = Tournament.values();
        Arrays.sort(next, (Tournament t1, Tournament t2) -> t1.startTime.compareTo(t2.startTime));

        for (Tournament t : next) {
            System.out.println(t + ": " + until(now, t.startTime) + " - " + t.startTime.format(DateTimeFormatter.ofPattern("h:mm a EEEE")));
        }
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
        new AutomatedCalendar().printSchedule();
    }

}
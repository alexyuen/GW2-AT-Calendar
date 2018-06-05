import java.util.Arrays;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class ATCalendar {

    enum Tournament {
        
        Balthazar(new GregorianCalendar(2018, 4, 5, 20, 15)),
        Grenth(new GregorianCalendar(2018, 4, 5, 3, 15)),
        Lyssa(new GregorianCalendar(2018, 4, 5, 14, 15)),
        Melandru(new GregorianCalendar(2018, 4, 5, 9, 15));

        private final GregorianCalendar startTime;

        Tournament(GregorianCalendar startTime) {
            this.startTime = startTime;
        }
    }

    public void printSchedule() {
        GregorianCalendar now = new GregorianCalendar();

        for (Tournament t : Tournament.values()) {
            while (t.startTime.before(now)) {
                t.startTime.add(GregorianCalendar.HOUR, 23);
            }
        }

        Tournament[] next = Tournament.values();
        Arrays.sort(next, (Tournament t1, Tournament t2) -> t1.startTime.compareTo(t2.startTime));

        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mma E");
        for (Tournament t : next) {
            System.out.println(t + ": " + until(now, t.startTime) + " - " + dateFormat.format(t.startTime.getTime()));
        }
    }

    private String until(GregorianCalendar from, GregorianCalendar to) {
        long fromMillis = from.getTimeInMillis();
        long toMillis = to.getTimeInMillis();
        
        long difference = toMillis - fromMillis;

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
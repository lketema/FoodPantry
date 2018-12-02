

public class Pantry extends Registration {
    
    private static final int SUNDAY = 0, MONDAY = 1, TUESDAY = 2, WEDNESDAY = 3,
            THURSDAY = 4, FRIDAY = 5, SATURDAY = 6;
  
    private String timeOpen, timeClosed;
    private boolean[] daysOpen;

    public Pantry(String name, String address, String phoneNumber, String
        emailAddress, String website, String timeOpen, String timeClosed,
        boolean[] daysOpen) {
        super(name, address, phoneNumber, emailAddress, website);
        this.timeOpen = timeOpen;
        this.timeClosed = timeClosed;
        this.daysOpen = daysOpen;
    }

    public String getTimeOpen() {
        return timeOpen;
    }

    public String getTimeClosed() {
        return timeClosed;
    }

    public boolean isOpenOn(int day) {
        return day < 0 || day > 6 ? false : daysOpen[day];
    }

    public void setTimeOpen(String timeOpen) {
        this.timeOpen = timeOpen;
    }

    public void setTimeClosed(String timeClosed) {
        this.timeClosed = timeClosed;
    }

    public void setDayOperational(int day, boolean open) {
        daysOpen[day] = open;
    }
}

package BookLink.BookLink.Domain.Common;

public enum RentStatus {

    RENTING("Renting"),
    WAITING("Waiting"),
    DENIED("Denied"),
    END("End");

    private final String value;

    RentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

package metro;

import java.nio.file.Paths;
import java.util.LinkedHashSet;

public class Station {

    private final String name;
    private final LinkedHashSet<String> prev;
    private final LinkedHashSet<String> next;
    private final LinkedHashSet<Transfer> transfer;
    private final Integer time;
    private String transferName;
    private String lineName;

    public Station(String name, LinkedHashSet<String> prev, LinkedHashSet<String> next, LinkedHashSet<Transfer> transfer, Integer time) {
        this.name = name;
        this.prev = prev;
        this.next = next;
        this.transfer = transfer;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LinkedHashSet<String> getPrev() {
        return prev;
    }

    public LinkedHashSet<String> getNext() {
        return next;
    }

    public LinkedHashSet<Transfer> getTransfer() {
        return transfer;
    }

    public Integer getTime() {
        return time;
    }

    public void connectToAnotherLine(String anotherLineName, String anotherStationName) {
        this.transfer.add(new Transfer(anotherLineName, anotherStationName));
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }
}

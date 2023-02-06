package metro;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;

import static java.lang.System.exit;
import static java.lang.System.setOut;

public class Main {

    private static Lines lines;
    private static final Map<String, Station> transferNames = new HashMap<>();
    private static final List<LinkedHashSet<Station>> linesList = new ArrayList<>();

    enum Command {

        APPEND,
        ADD,
        REMOVE,
        OUTPUT,
        CONNECT,
        ROUTE,
        FASTEST_ROUTE,
        EXIT;

        static Command getCommand(String arg) {
            switch (arg) {
                case "/append":
                    return APPEND;
                case "/add-head":
                    return ADD;
                case "/remove":
                    return REMOVE;
                case "/output":
                    return OUTPUT;
                case "/connect":
                    return CONNECT;
                case "/route":
                    return ROUTE;
                case "/fastest-route":
                    return FASTEST_ROUTE;
                default:
                    return EXIT;
            }
        }
    }

    public static void main(String[] args) {
        String jsonFileName = args[0];
        Gson gson = new Gson(); //route "Victoria line" "Green Park" "Northern line" "Oval"


        try {


            FileInputStream fileInputStream = new FileInputStream(jsonFileName);
            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\BotMachine\\IdeaProjects\\HyperMetro\\HyperMetro\\task\\src\\data\\textJsonFile.json");
            fileOutputStream.write(fileInputStream.readAllBytes());







            lines = gson.fromJson(new JsonReader(new FileReader(jsonFileName)), Lines.class);
        } catch (IOException ignored) {
        }

        loadTransferNames();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            List<String> inputArguments = getArgumentsFromUserInput(scanner.nextLine());

            String commandString = inputArguments.get(0);
            Command command = Command.getCommand(commandString);
            switch (command) {
                case ADD:
                case APPEND: {
                    LinkedHashSet<Station> line = getLineByLineName(inputArguments.get(1));
                    String stationName = inputArguments.get(2);
                    int time;
                    if (inputArguments.size() > 3) {
                        time = Integer.parseInt(inputArguments.get(2));
                    } else {
                        time = 0;
                    }
                    Station newStation = new Station(stationName, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>(), time);
                    line.add(newStation);
                    break;
                }
                case REMOVE: {
                    LinkedHashSet<Station> line = getLineByLineName(inputArguments.get(1));
                    String stationName = inputArguments.get(2);
                    line.removeIf(station -> station.getName().equals(stationName));
                    break;
                }
                case OUTPUT: {
                    LinkedHashSet<Station> line = getLineByLineName(inputArguments.get(1));
                    printLine(line);
                    break;
                }
                case CONNECT: {
                    String lineName = inputArguments.get(1);
                    String stationName = inputArguments.get(2);

                    LinkedHashSet<Station> line = getLineByLineName(lineName);
                    Station station = null;
                    for (Station elem :
                            line) {
                        if (elem.getName().equals(stationName)) {
                            station = elem;
                            break;
                        }
                    }

                    String lineToConnectName = inputArguments.get(3);
                    String stationToConnectName = inputArguments.get(4);

                    LinkedHashSet<Station> lineToConnect = getLineByLineName(lineToConnectName);
                    Station stationToConnect = null;
                    for (Station elem :
                            lineToConnect) {
                        if (elem.getName().equals(stationToConnectName)) {
                            stationToConnect = elem;
                            break;
                        }
                    }

                    assert station != null;
                    station.connectToAnotherLine(lineToConnectName, stationToConnectName);

                    assert stationToConnect != null;
                    stationToConnect.connectToAnotherLine(lineName, stationName);
                    break;
                }

                case ROUTE: {
                    String line1Name = inputArguments.get(1);
                    String station1Name = inputArguments.get(2);

                    LinkedHashSet<Station> line = getLineByLineName(line1Name);
                    Station station1 = null;
                    for (Station elem :
                            line) {
                        if (elem.getName().equals(station1Name)) {
                            station1 = elem;
                            break;
                        }
                    }

                    String line2Name = inputArguments.get(3);
                    String station2Name = inputArguments.get(4);

                    LinkedHashSet<Station> lineToConnect = getLineByLineName(line2Name);
                    Station station2 = null;
                    for (Station elem :
                            lineToConnect) {
                        if (elem.getName().equals(station2Name)) {
                            station2 = elem;
                            break;
                        }
                    }

                    findShortestRoute(station1, station2);
                    break;
                }
                case FASTEST_ROUTE: {
                    String line1Name = inputArguments.get(1);
                    String station1Name = inputArguments.get(2);

                    LinkedHashSet<Station> line = getLineByLineName(line1Name);
                    Station station1 = null;
                    for (Station elem :
                            line) {
                        if (elem.getName().equals(station1Name)) {
                            station1 = elem;
                            break;
                        }
                    }

                    String line2Name = inputArguments.get(3);
                    String station2Name = inputArguments.get(4);

                    LinkedHashSet<Station> lineToConnect = getLineByLineName(line2Name);
                    Station station2 = null;
                    for (Station elem :
                            lineToConnect) {
                        if (elem.getName().equals(station2Name)) {
                            station2 = elem;
                            break;
                        }
                    }

                    findFastestRoute(station1, station2);
                    break;
                }
                default:
                    exit(0);
            }
        }
    }

    private static void printLine(LinkedHashSet<Station> line) {
        for (Station station :
                line) {
            System.out.println(station.getName());
        }
    }

    private static List<String> getArgumentsFromUserInput(String userInput) {
        List<String> arguments = new ArrayList<>();

        StringBuilder argument = new StringBuilder();
        for (int i = 0; i < userInput.length(); i++) {
            if (userInput.charAt(i) == ' ') {
                continue;
            }
            if (userInput.charAt(i) == '\"') {
                i++;
                while (userInput.charAt(i) != '\"') {
                    argument.append(userInput.charAt(i));
                    i++;
                }
            } else {
                while (userInput.charAt(i) != ' ') {
                    argument.append(userInput.charAt(i));
                    i++;
                    if (i == userInput.length()) {
                        break;
                    }
                }
            }
            arguments.add(argument.toString());
            argument = new StringBuilder();
        }
        return arguments;
    }

    private static void findShortestRoute(Station station1, Station station2) {
        List<Station> stationsForConsider = new ArrayList<>();
        LinkedHashSet<String> stationsNamesForConsider = new LinkedHashSet<>();

        List<Station> availableStations = new ArrayList<>();
        availableStations.add(station1);
        Set<String> availableStationNames = new HashSet<>();
        availableStationNames.add(station1.getName());

        Map<Station, Integer> stations = new HashMap<>();
        LinkedHashSet<String> markedStations = new LinkedHashSet<>();

        int numberOfMoves = 0;
        do {
            stationsForConsider.clear();
            stationsNamesForConsider.clear();
            stationsForConsider.addAll(availableStations);
            stationsNamesForConsider.addAll(availableStationNames);
            availableStations.clear();
            availableStationNames.clear();

            for (Station stationForConsider :
                    stationsForConsider) {
                List<Station> neighbours = getNeighbourStations(stationForConsider);
                for (Station neighbour :
                        neighbours) {
                    if (!availableStationNames.contains(neighbour.getName()) && !markedStations.contains(neighbour.getName()) && !stationsNamesForConsider.contains(neighbour.getName())) {
                        availableStationNames.add(neighbour.getName());
                        availableStations.add(neighbour);
                    }
                }

                for (Transfer transfer :
                        stationForConsider.getTransfer()) {
                    String transferLineName = transfer.getLine();
                    String transferStationName = transfer.getStation() + " " + transferLineName;
                    List<Station> transferNeighbours = getNeighbourStations(transferNames.get(transferStationName));
                    for (Station transferNeighbour :
                            transferNeighbours) {
                        if (!availableStationNames.contains(transferNeighbour.getName()) && !markedStations.contains(transferNeighbour.getName()) && !stationsNamesForConsider.contains(transferNeighbour.getName())) {
                            availableStationNames.add(transferNeighbour.getName());
                            availableStations.add(transferNeighbour);
                        }
                    }
                }

                stations.put(stationForConsider, numberOfMoves);
                markedStations.add(stationForConsider.getName());
            }

            numberOfMoves++;

        } while (!markedStations.contains(station2.getName()));
        numberOfMoves -= 2;


        List<Station> shortestRoute = new ArrayList<>();
        Station lastStation = station2;
        String currentLineName = lastStation.getLineName();
        Set<String> availableLineNames;
        boolean flag = true;
        while (flag) {
            for (var entry :
                    stations.entrySet()) {
                Set<String> stationNames = new HashSet<>();
                for (Station neighbour :
                        getNeighbourStations(lastStation)) {
                    stationNames.add(neighbour.getName());
                }

                for (Transfer transfer :
                        lastStation.getTransfer()) {
                    String transferLineName = transfer.getLine();
                    String transferStationName = transfer.getStation() + " " + transferLineName;
                    List<Station> transferNeighbours = getNeighbourStations(transferNames.get(transferStationName));
                    for (Station transferNeighbour :
                            transferNeighbours) {
                        stationNames.add(transferNeighbour.getName());
                    }
                }

                if (entry.getValue() == numberOfMoves && stationNames.contains(entry.getKey().getName())) {
                    availableLineNames = getAvailableLineNames(entry.getKey());
                    if (availableLineNames.contains(currentLineName)) {
                        shortestRoute.add(lastStation);
                    } else {
                        String commonLine = getCommonLine(entry.getKey(), lastStation);
                        assert commonLine != null;
                        shortestRoute.add(lastStation);
                        shortestRoute.add(new Station("Transition to line " + currentLineName, null, null, null,  null));
                        currentLineName = commonLine;
                        shortestRoute.add(lastStation);
                    }
                    lastStation = entry.getKey();
                    numberOfMoves--;
                    break;
                }
                if (numberOfMoves == 0) {
                    shortestRoute.add(lastStation);
                    if (!currentLineName.equals(station1.getLineName())) {
                        shortestRoute.add(station1);
                        shortestRoute.add(new Station("Transition to line " + lastStation.getLineName(), null, null, null,  null));
                        shortestRoute.add(station1);
                    } else {
                        shortestRoute.add(station1);
                    }
                    flag = false;
                    break;
                }
            }
        }

        Collections.reverse(shortestRoute);

        for (Station station :
                shortestRoute) {
            System.out.println(station.getName());
        }
    }

    private static List<Station> getNeighbourStations(Station station) {
        String lineName = station.getLineName();
        List<Station> neighbours = new ArrayList<>();
        for (String prevStationElem :
                station.getPrev()) {
            String transferName = prevStationElem + " " + lineName;
            neighbours.add(transferNames.get(transferName));
        }
        for (String nextStationElem :
                station.getNext()) {
            String transferName = nextStationElem + " " + lineName;
            neighbours.add(transferNames.get(transferName));
        }
        return neighbours;
    }

    private static Set<String> getAvailableLineNames(Station station) {
        Set<String> availableLineNames = new HashSet<>();
        availableLineNames.add(station.getLineName());
        if (station.getTransfer().size() != 0) {
            for (Transfer transfer :
                    station.getTransfer()) {
                availableLineNames.add(transfer.getLine());
            }
        }
        return availableLineNames;
    }

    private static String getCommonLine(Station station1, Station station2) {
        Set<String> lineNames1 = getAvailableLineNames(station1);
        Set<String> lineNames2 = getAvailableLineNames(station2);

        for (String lineName1 :
                lineNames1) {
            for (String lineName2 :
                    lineNames2) {
                if (lineName1.equals(lineName2)) {
                    return lineName1;
                }
            }
        }
        return null;
    }

    private static void findFastestRoute(Station station1, Station station2) {
        Station currentStation = station1;
        Map<String, Integer> stationNamesCost = new HashMap<>();
        LinkedHashSet<String> unknownStations = new LinkedHashSet<>();
        List<Dijkstra> priorityQueue = new ArrayList<>();

        Map<String, String> paths = new HashMap<>();

        Map<String, Station> getStationByTransferName = new HashMap<>();

        for (LinkedHashSet<Station> line :
                linesList) {
            for (Station station :
                    line) {
                stationNamesCost.put(station.getTransferName(), Integer.MAX_VALUE);
                unknownStations.add(station.getTransferName());
                priorityQueue.add(new Dijkstra(station, Integer.MAX_VALUE));
                getStationByTransferName.put(station.getTransferName(), station);
            }
        }

        stationNamesCost.put(currentStation.getTransferName(), 0);
        for (Dijkstra elem :
                priorityQueue) {
            if (elem.getStation().getTransferName().equals(currentStation.getTransferName())) {
                elem.setWeight(0);
                break;
            }
        }

        unknownStations.remove(currentStation.getTransferName());
        for (Dijkstra elem :
                priorityQueue) {
            if (elem.getStation().getTransferName().equals(currentStation.getTransferName())) {
                priorityQueue.remove(elem);
                break;
            }
        }

        while (true) {
            for (Station neighbourStation :
                    getNeighbourStationsForFast(currentStation)) {
                if (unknownStations.contains(neighbourStation.getTransferName())) {
                    int travelTime = getTravelTimeBetweenStations(currentStation, neighbourStation) + stationNamesCost.get(currentStation.getTransferName());
                    if (travelTime < stationNamesCost.get(neighbourStation.getTransferName())) {
                        stationNamesCost.put(neighbourStation.getTransferName(), travelTime);
                        for (Dijkstra elem :
                                priorityQueue) {
                            if (elem.getStation().getTransferName().equals(neighbourStation.getTransferName())) {
                                elem.setWeight(travelTime);
                                break;
                            }
                        }
                        paths.put(neighbourStation.getTransferName(), currentStation.getTransferName());
                    }
                }
            }

            priorityQueue.sort(new DijkstraWeightComparator());
            if (priorityQueue.size() > 0) {
                currentStation = priorityQueue.get(0).getStation();
            } else {
                break;
            }
            if (stationNamesCost.get(currentStation.getTransferName()) == Integer.MAX_VALUE) {
                break;
            }

            unknownStations.remove(currentStation.getTransferName());
            for (Dijkstra elem :
                    priorityQueue) {
                if (elem.getStation().getTransferName().equals(currentStation.getTransferName())) {
                    priorityQueue.remove(elem);
                    break;
                }
            }


        }

        int totalTime = 0;

        List<String> fastestRoad = new ArrayList<>();
        fastestRoad.add(station2.getTransferName());
        String lastStation = station2.getTransferName();
        while (true) {
            if (getStationByTransferName.get(lastStation).getLineName()
                    .equals(getStationByTransferName.get(paths.get(lastStation)).getLineName())) {
                fastestRoad.add(paths.get(lastStation));
            } else {
                fastestRoad.add("Transition to line " + getStationByTransferName.get(lastStation).getLineName());
                fastestRoad.add(paths.get(lastStation));
            }

            totalTime += getTravelTimeBetweenStations(getStationByTransferName.get(lastStation), getStationByTransferName.get(fastestRoad.get(fastestRoad.size() - 1)));
            lastStation = paths.get(lastStation);
            if (paths.get(lastStation).equals(station1.getTransferName())) {
                if (getStationByTransferName.get(lastStation).getLineName()
                        .equals(getStationByTransferName.get(paths.get(lastStation)).getLineName())) {
                    fastestRoad.add(station1.getTransferName());
                } else {
                    fastestRoad.add("Transition to line " + getStationByTransferName.get(lastStation).getLineName());
                    fastestRoad.add(station1.getTransferName());
                }
                totalTime += getTravelTimeBetweenStations(getStationByTransferName.get(lastStation), getStationByTransferName.get(fastestRoad.get(fastestRoad.size() - 1)));
                break;
            }
        }

        Collections.reverse(fastestRoad);
        for (String station :
                fastestRoad) {
            if (getStationByTransferName.containsKey(station)) {
                String lineName = getStationByTransferName.get(station).getLineName();
                station = station.replaceAll(" " + lineName, "");
            }
            System.out.println(station);
        }
        System.out.printf("Total: %d minutes in the way\n", totalTime);
    }

    private static List<Station> getNeighbourStationsForFast(Station station) {
        String lineName = station.getLineName();
        List<Station> neighbours = new ArrayList<>();
        for (String prevStationElem :
                station.getPrev()) {
            String transferName = prevStationElem + " " + lineName;
            neighbours.add(transferNames.get(transferName));
        }
        for (String nextStationElem :
                station.getNext()) {
            String transferName = nextStationElem + " " + lineName;
            neighbours.add(transferNames.get(transferName));
        }
        for (Transfer transfer :
                station.getTransfer()) {
            String transferLineName = transfer.getLine();
            String transferName = transfer.getStation() + " " + transferLineName;
            neighbours.add(transferNames.get(transferName));
        }
        return neighbours;
    }

    private static int getTravelTimeBetweenStations(Station station1, Station station2) {
        if (!station1.getLineName().equals(station2.getLineName())) {
            return 5;
        } else {
            if (station2.getPrev().contains(station1.getName())) {
                return station1.getTime();
            } else {
                return station2.getTime();
            }
        }
    }

    private static int getTravelTimeBetweenStationsForShortestPath(Station station1, Station station2) {
        if (!station1.getLineName().equals(station2.getLineName())) {
            return 0;
        } else {
            return 1;
        }
    }

    private static LinkedHashSet<Station> getLineByLineName(String lineName) {
        switch (lineName) {
            case "Bakerloo line":
                return lines.getBakerlooLine();
            case "Central line":
                return lines.getCentralLine();
            case "Circle line":
                return lines.getCircleLine();
            case "District line":
                return lines.getDistrictLine();
            case "Hammersmith & City line":
                return lines.getHammersmithCityLine();
            case "Jubilee line":
                return lines.getJubileeLine();
            case "Metropolitan line":
                return lines.getMetropolitanLine();
            case "Northern line":
                return lines.getNorthernLine();
            case "Piccadilly line":
                return lines.getPiccadillyLine();
            case "Victoria line":
                return lines.getVictoriaLine();
            case "Waterloo & City line":
                return lines.getWaterlooCityLine();
            case "DLR 1":
                return lines.getDLR1();
            case "DLR 2":
                return lines.getDLR2();
            default:
                return lines.getDLR3();
        }
    }

    private static void loadTransferNames() {
        for (Station station :
                lines.getBakerlooLine()) {
            station.setTransferName(station.getName() + " Bakerloo line");
            station.setLineName("Bakerloo line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getBakerlooLine());
        for (Station station :
                lines.getCentralLine()) {
            station.setTransferName(station.getName() + " Central line");
            station.setLineName("Central line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getCentralLine());
        for (Station station :
                lines.getCircleLine()) {
            station.setTransferName(station.getName() + " Circle line");
            station.setLineName("Circle line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getCircleLine());
        for (Station station :
                lines.getDistrictLine()) {
            station.setTransferName(station.getName() + " District line");
            station.setLineName("District line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getDistrictLine());
        for (Station station :
                lines.getHammersmithCityLine()) {
            station.setTransferName(station.getName() + " Hammersmith & City line");
            station.setLineName("Hammersmith & City line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getHammersmithCityLine());
        for (Station station :
                lines.getJubileeLine()) {
            station.setTransferName(station.getName() + " Jubilee line");
            station.setLineName("Jubilee line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getJubileeLine());
        for (Station station :
                lines.getMetropolitanLine()) {
            station.setTransferName(station.getName() + " Metropolitan line");
            station.setLineName("Metropolitan line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getMetropolitanLine());
        for (Station station :
                lines.getNorthernLine()) {
            station.setTransferName(station.getName() + " Northern line");
            station.setLineName("Northern line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getNorthernLine());
        for (Station station :
                lines.getPiccadillyLine()) {
            station.setTransferName(station.getName() + " Piccadilly line");
            station.setLineName("Piccadilly line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getPiccadillyLine());
        for (Station station :
                lines.getVictoriaLine()) {
            station.setTransferName(station.getName() + " Victoria line");
            station.setLineName("Victoria line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getVictoriaLine());
        for (Station station :
                lines.getWaterlooCityLine()) {
            station.setTransferName(station.getName() + " Waterloo & City line");
            station.setLineName("Waterloo & City line");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getWaterlooCityLine());
        for (Station station :
                lines.getDLR1()) {
            station.setTransferName(station.getName() + " DLR 1");
            station.setLineName("DLR 1");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getDLR1());
        for (Station station :
                lines.getDLR2()) {
            station.setTransferName(station.getName() + " DLR 2");
            station.setLineName("DLR 2");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getDLR2());
        for (Station station :
                lines.getDLR3()) {
            station.setTransferName(station.getName() + " DLR 3");
            station.setLineName("DLR 3");
            transferNames.put(station.getTransferName(), station);
        }
        linesList.add(lines.getDLR3());
    }
}

package metro;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashSet;

public class Lines {
    @SerializedName("Bakerloo line")
    private LinkedHashSet<Station> bakerlooLine;

    @SerializedName("Central line")
    private LinkedHashSet<Station> centralLine;

    @SerializedName("Circle line")
    private LinkedHashSet<Station> circleLine;

    @SerializedName("District line")
    private LinkedHashSet<Station> districtLine;

    @SerializedName("Hammersmith & City line")
    private LinkedHashSet<Station> hammersmithCityLine;

    @SerializedName("Jubilee line")
    private LinkedHashSet<Station> jubileeLine;

    @SerializedName("Metropolitan line")
    private LinkedHashSet<Station> metropolitanLine;

    @SerializedName("Northern line")
    private LinkedHashSet<Station> northernLine;

    @SerializedName("Piccadilly line")
    private LinkedHashSet<Station> piccadillyLine;

    @SerializedName("Victoria line")
    private LinkedHashSet<Station> victoriaLine;

    @SerializedName("Waterloo & City line")
    private LinkedHashSet<Station> waterlooCityLine;

    @SerializedName("DLR 1")
    private LinkedHashSet<Station> DLR1;

    @SerializedName("DLR 2")
    private LinkedHashSet<Station> DLR2;

    @SerializedName("DLR 3")
    private LinkedHashSet<Station> DLR3;

    public LinkedHashSet<Station> getBakerlooLine() {
        return bakerlooLine;
    }

    public LinkedHashSet<Station> getCentralLine() {
        return centralLine;
    }

    public LinkedHashSet<Station> getCircleLine() {
        return circleLine;
    }

    public LinkedHashSet<Station> getDistrictLine() {
        return districtLine;
    }

    public LinkedHashSet<Station> getHammersmithCityLine() {
        return hammersmithCityLine;
    }

    public LinkedHashSet<Station> getJubileeLine() {
        return jubileeLine;
    }

    public LinkedHashSet<Station> getMetropolitanLine() {
        return metropolitanLine;
    }

    public LinkedHashSet<Station> getNorthernLine() {
        return northernLine;
    }

    public LinkedHashSet<Station> getPiccadillyLine() {
        return piccadillyLine;
    }

    public LinkedHashSet<Station> getVictoriaLine() {
        return victoriaLine;
    }

    public LinkedHashSet<Station> getWaterlooCityLine() {
        return waterlooCityLine;
    }

    public LinkedHashSet<Station> getDLR1() {
        return DLR1;
    }

    public LinkedHashSet<Station> getDLR2() {
        return DLR2;
    }

    public LinkedHashSet<Station> getDLR3() {
        return DLR3;
    }
}

package hajutonbot;

public class MapData {
    private String mapName;
    private double numberOfMatches;

    public MapData(String mapName, double numberOfMatches) {
        this.mapName = mapName;
        this.numberOfMatches = numberOfMatches;
    }

    public String getMapName() {
        return mapName;
    }

    public double getNumberOfMatches() {
        return numberOfMatches;
    }

    public String toString() {
        return mapName + ". Matches: " + (int) numberOfMatches + " ---";
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setNumberOfMatches(double numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

}

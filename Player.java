
public class Player {

    private String name;
    private int points;
    private boolean active;

    public Player (String name) {
        this(name, false);
    }

    public Player (String name, boolean setActive) {
        this.name = name;
        this.points = 0;
        this.active = setActive;
    }

    public void addPoint() {
        points++;
    }

    public int getPoints() {
        return points;
    }
    public String getName() {
        return name;
    }
}

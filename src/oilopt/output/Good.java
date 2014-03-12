package oilopt.output;

import java.util.Map;

public class Good
{
    private int id;
    private String name;
    private double price;
    private double volume;
    private double cost;
    private double used;
    private double created;
    private Map<Integer, Double> createdByTool;
    private Map<Integer, Double> usedByTool;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * @return the cost
     */
    public double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * @return the used
     */
    public double getUsed() {
        return used;
    }

    /**
     * @param used the used to set
     */
    public void setUsed(double used) {
        this.used = used;
    }

    /**
     * @return the created
     */
    public double getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(double created) {
        this.created = created;
    }

    /**
     * @return the createdByTool
     */
    public Map<Integer, Double> getCreatedByTool() {
        return createdByTool;
    }

    /**
     * @param createdByTool the createdByTool to set
     */
    public void setCreatedByTool(Map<Integer, Double> createdByTool) {
        this.createdByTool = createdByTool;
    }

    /**
     * @return the usedByTool
     */
    public Map<Integer, Double> getUsedByTool() {
        return usedByTool;
    }

    /**
     * @param usedByTool the usedByTool to set
     */
    public void setUsedByTool(Map<Integer, Double> usedByTool) {
        this.usedByTool = usedByTool;
    }

}

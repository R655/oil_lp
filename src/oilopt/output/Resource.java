package oilopt.output;

import java.util.Map;


public class Resource
{
    protected int id;
    protected String name;
    protected double stock;
    protected double used;
    protected double notused;
    protected double purchased;
    protected double gamma;
    protected double price;

    protected Map<Integer, Double> usedByTools;

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
     * @return the stock
     */
    public double getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(double stock) {
        this.stock = stock;
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
     * @return the notused
     */
    public double getNotused() {
        return notused;
    }

    /**
     * @param notused the notused to set
     */
    public void setNotused(double notused) {
        this.notused = notused;
    }

    /**
     * @return the purchased
     */
    public double getPurchased() {
        return purchased;
    }

    /**
     * @param purchased the purchased to set
     */
    public void setPurchased(double purchased) {
        this.purchased = purchased;
    }

    /**
     * @return the usedByTools
     */
    public Map<Integer, Double> getUsedByTools() {
        return usedByTools;
    }

    /**
     * @param usedByTools the usedByTools to set
     */
    public void setUsedByTools(Map<Integer, Double> usedByTools) {
        this.usedByTools = usedByTools;
    }

    /**
     * @return the gamma
     */
    public double getGamma() {
        return gamma;
    }

    /**
     * @param gamma the gamma to set
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
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
}

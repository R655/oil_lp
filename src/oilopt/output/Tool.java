package oilopt.output;


public class Tool
{
    protected String name;
    protected String abbr;
    protected int id;
    protected Double maxPower;
    protected Double recPower;
    protected Double revenue;
    protected Double relativeRevenue;

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
     * @return the abbr
     */
    public String getAbbr() {
        return abbr;
    }

    /**
     * @param abbr the abbr to set
     */
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

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
     * @return the maxPower
     */
    public Double getMaxPower() {
        return maxPower;
    }

    /**
     * @param maxPower the maxPower to set
     */
    public void setMaxPower(Double maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * @return the recPower
     */
    public Double getRecPower() {
        return recPower;
    }

    /**
     * @param recPower the recPower to set
     */
    public void setRecPower(Double recPower) {
        this.recPower = recPower;
    }

    /**
     * @return the revenue
     */
    public Double getRevenue() {
        return revenue;
    }

    /**
     * @param revenue the revenue to set
     */
    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }

    /**
     * @return the relativeRevenue
     */
    public Double getRelativeRevenue() {
        return relativeRevenue;
    }

    /**
     * @param relativeRevenue the relativeRevenue to set
     */
    public void setRelativeRevenue(Double relativeRevenue) {
        this.relativeRevenue = relativeRevenue;
    }
}

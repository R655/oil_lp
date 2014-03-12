/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.orm;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Eugene
 */

@DatabaseTable
public class Tool {
    @DatabaseField(generatedId = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "Name", unique = true, canBeNull = false)
    private String name;
    @DatabaseField(columnName = "Abbreviation")
    private String abbreviation;
    @DatabaseField(columnName = "Power", canBeNull = false)
    private int power;
    
    @ForeignCollectionField(foreignFieldName = "tool", columnName = "ToodId")
    private ForeignCollection<NeedGoodForTool> needGoodForTool;
    
    @ForeignCollectionField(foreignFieldName = "tool", columnName = "ToodId")
    private ForeignCollection<GoodFromTool> goodFromTool;
    
    @ForeignCollectionField(foreignFieldName = "tool", columnName = "ToodId")
    private ForeignCollection<NeedResourceForTool> needResourceForTool;
    

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public int getPower() {
        return power;
    }

    public Tool() {
    }

    /**
     * @return the needGoodForTool
     */
    public ForeignCollection<NeedGoodForTool> getNeedGoodForTool() {
        return needGoodForTool;
    }

    /**
     * @param needGoodForTool the needGoodForTool to set
     */
    public void setNeedGoodForTool(ForeignCollection<NeedGoodForTool> needGoodForTool) {
        this.needGoodForTool = needGoodForTool;
    }

    /**
     * @return the goodFromTool
     */
    public ForeignCollection<GoodFromTool> getGoodFromTool() {
        return goodFromTool;
    }

    /**
     * @param goodFromTool the goodFromTool to set
     */
    public void setGoodFromTool(ForeignCollection<GoodFromTool> goodFromTool) {
        this.goodFromTool = goodFromTool;
    }

    /**
     * @return the needResourceForTool
     */
    public ForeignCollection<NeedResourceForTool> getNeedResourceForTool() {
        return needResourceForTool;
    }

    /**
     * @param needResourceForTool the needResourceForTool to set
     */
    public void setNeedResourceForTool(ForeignCollection<NeedResourceForTool> needResourceForTool) {
        this.needResourceForTool = needResourceForTool;
    }
}

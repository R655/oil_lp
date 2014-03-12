/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.orm;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigDecimal;

/**
 *
 * @author Eugene
 */

@DatabaseTable
public class Resource {
    @DatabaseField(columnName = "Id", generatedId = true)
    private int id;
    @DatabaseField(columnName = "Name", unique = true, canBeNull = false)
    private String name;
    @DatabaseField(columnName = "Price", canBeNull = false, dataType = DataType.BIG_DECIMAL)
    private BigDecimal price;
    
    @ForeignCollectionField(foreignFieldName = "resource", columnName = "ResourceId")
    private ForeignCollection<ResourceNumber> number;
    
    @ForeignCollectionField(foreignFieldName = "resource", columnName = "ResourceId")
    private ForeignCollection<NeedResourceForTool> needResourceForTool;
    
    public double getNumber()
    {
        double resourceNumber = 0;
        for(ResourceNumber rn : number)
        {
            resourceNumber = rn.getTotalNumber();
            break; // ибо связь один к одному
        }
        return resourceNumber;
    }
    
    public double getNumberDeviation()
    {
        double resourceNumberVariance = 0;
        for(ResourceNumber rn : number)
        {
            resourceNumberVariance = rn.getDeviation();
            break; // ибо связь один к одному
        }
        return resourceNumberVariance;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Resource() {
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
    
    public double getPrice()
    {
        return this.price.doubleValue();
    }
}

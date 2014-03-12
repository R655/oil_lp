/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.orm;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.math.BigDecimal;

/**
 *
 * @author Eugene
 */

@DatabaseTable
public class ResourceNumber {
    @DatabaseField(columnName = "ResourceId", foreign = true, canBeNull = false)
    private Resource resource;
    @DatabaseField(columnName = "TotalNumber", dataType = DataType.DOUBLE, canBeNull = false)
    private double totalNumber;
    @DatabaseField(columnName = "Deviation", dataType = DataType.DOUBLE, canBeNull = false)
    private double deviation;

    public void setResource(Resource resource) {
        this.resource = resource;
    }
    
    public double getDeviation()
    {
        return deviation;//totalNumber*0.1;
    }

    public void setTotalNumber(double totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Resource getResource() {
        return resource;
    }

    public double getTotalNumber() {
        return totalNumber;
    }

    public ResourceNumber() {
    }
}

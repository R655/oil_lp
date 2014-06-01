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
public class NeedResourceForTool {
    @DatabaseField(columnName = "ToolId", canBeNull = false, dataType = DataType.INTEGER)
    private int toolId;
    @DatabaseField(columnName = "ToolId", foreign = true, canBeNull = false)
    private Tool tool;
    @DatabaseField(columnName = "ResourceId", foreign = true, canBeNull = false)
    private Resource resource;
    @DatabaseField(columnName = "RequestNumber", canBeNull = false, dataType = DataType.DOUBLE)
    private double requestNumber;
    @DatabaseField(columnName = "Deviation", dataType = DataType.DOUBLE, canBeNull = false)
    private double deviation;

    public void setTool(Tool tool) {
        this.tool = tool;
    }
    
    public double getDeviation()
    {
        return deviation;//getRequestNumber()*0.1;
    }
    
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public void setRequestNumber(double requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Tool getTool() {
        return tool;
    }

    public Resource getResource() {
        return resource;
    }

    public double getRequestNumber() {
        return requestNumber;
    }

    /**
     * @return the toolId
     */
    public int getToolId() {
        return toolId;
    }

    /**
     * @param toolId the toolId to set
     */
    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

}

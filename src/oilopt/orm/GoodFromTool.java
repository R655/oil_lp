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
public class GoodFromTool {
    @DatabaseField(columnName = "ToolId", canBeNull = false, dataType = DataType.INTEGER)
    private int toolId;
    @DatabaseField(columnName = "ToolId", foreign = true, canBeNull = false)
    private Tool tool;
    @DatabaseField(columnName = "GoodId", foreign = true, canBeNull = false)
    private Good good;
    @DatabaseField(columnName = "ReceiveNumber", canBeNull = false, dataType = DataType.BIG_DECIMAL)
    private BigDecimal receiveNumber;

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public void setReceiveNumber(BigDecimal receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    public Tool getTool() {
        return tool;
    }

    public Good getGood() {
        return good;
    }

    public BigDecimal getReceiveNumber() {
        return receiveNumber;
    }

    public GoodFromTool() {
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

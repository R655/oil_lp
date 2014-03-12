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
public class NeedGoodForTool {
    @DatabaseField(columnName = "ToolId", canBeNull = false)
    private int toolId;
    @DatabaseField(columnName = "ToolId", foreign = true, canBeNull = false)
    private Tool tool;
    @DatabaseField(columnName = "GoodId", foreign = true, canBeNull = false)
    private Good good;
    @DatabaseField(columnName = "RequestNumber", dataType = DataType.BIG_DECIMAL, canBeNull = false)
    private BigDecimal requestNumber;

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public void setRequestNumber(BigDecimal requestNumber) {
        this.requestNumber = requestNumber;
    }

    public Tool getTool() {
        return tool;
    }

    public Good getGood() {
        return good;
    }

    public BigDecimal getRequestNumber() {
        return requestNumber;
    }

    public NeedGoodForTool() {
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

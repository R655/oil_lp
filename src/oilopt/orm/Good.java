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
public class Good {
    @DatabaseField(generatedId = true, columnName = "Id", dataType = DataType.INTEGER)
    private int id;
    @DatabaseField(columnName = "Name", canBeNull = false, dataType = DataType.STRING)
    private String name;
    @DatabaseField(columnName = "Price", canBeNull = false, dataType = DataType.BIG_DECIMAL)
    private BigDecimal price;
    
    @ForeignCollectionField(foreignFieldName = "good", columnName = "GoodId")
    private ForeignCollection<NeedGoodForTool> needGoodForTool;
    
    @ForeignCollectionField(foreignFieldName = "good", columnName = "GoodId")
    private ForeignCollection<GoodFromTool> goodFromTool;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Good() {
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
}

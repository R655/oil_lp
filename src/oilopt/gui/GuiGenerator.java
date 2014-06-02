/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.gui;

import javax.swing.*;
import java.lang.reflect.*;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author r655
 */
public class GuiGenerator 
{
    
    public void reconfigureFor(Class<?> ormClass) throws Exception
    {
        if(!ormClass.isAnnotationPresent(DatabaseTable.class))
            throw new Exception("Need orm class");
        
        JFrame form = new JFrame();
        
        Field[] fields = ormClass.getFields();
        for(Field field: fields)
        {
            if (field.isAnnotationPresent(DatabaseField.class)) 
            {
                DatabaseField fieldParams = field.getAnnotation(DatabaseField.class);
                
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package oilopt.gui;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import javax.swing.*;
import java.util.*;
import oilopt.orm.GoodFromTool;
import oilopt.orm.NeedGoodForTool;

/**
 *
 * @author r655
 */
public class ORMGrid extends javax.swing.JFrame 
{
    static Map<String, String> readableNames  = new HashMap<String, String>()
    {{
        put("Good", "Продукт");
        put("GoodFromTool", "Полученный продукт");
        put("NeedGoodForTool", "Расходуемый продукт");
        put("NeedResourceForTool", "Расходуемый ресурс");
        put("Resource", "Ресурс");
        put("Tool", "Технология");
        put("Id", "Ид");
        put("Name", "Название");
        put("Price", "Цена");
        put("GoodId", "Идентификатор продукта");
        put("ToolId", "Идентификатор технологии");
        put("ResourceId", "Идентификатор ресурса");
        put("ReceiveNumber", "Полученное количество");
        put("RequestNumber", "Требуемое количество");
        put("TotalNumber", "Максимальное количество");
        put("Deviation", "Дисперсия");
        put("Abbreviation", "Аббревиатура");
        put("Power", "Максимальная мощность");
    }};
    
    public ORMGrid(Class<?> ormClass) throws Exception
    {
        
        if(!ormClass.isAnnotationPresent(DatabaseTable.class))
            throw new Exception("Need orm class");
        
        JFrame form = new JFrame();
        
        Field[] fields = ormClass.getFields();
        List<String> readableFieldsNames = new LinkedList<>();
        List<Class> fieldsTypes = new LinkedList<>();
        
        for(Field field: fields)
        {
            if (field.isAnnotationPresent(DatabaseField.class)) 
            {
                DatabaseField fieldParams = field.getAnnotation(DatabaseField.class);
                if(!fieldParams.foreign())
                {
                    if(readableNames.containsKey(field.getName()))
                        readableFieldsNames.add(readableNames.get(field.getName()));
                    else
                        readableFieldsNames.add(field.getName());
                    
                    Class  cl = Integer.class;

                    switch(fieldParams.dataType())
                    {
                        case BOOLEAN:
                            cl = Boolean.class;
                        case BIG_DECIMAL:
                            cl = BigDecimal.class;
                        case UNKNOWN:
                            cl = Integer.class;
                        case INTEGER:
                            cl = Integer.class;
                        case DOUBLE:
                            cl = Double.class;
                        default:
                            cl = Integer.class;

                    }
                    fieldsTypes.add(cl);
                }
            }
        }
        
        initComponents();
        
        
        JTable ormtable = new JTable();

        ormtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                
            },
            readableFieldsNames.toArray()
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Name", "Count", "Lalka"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ORMGrid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ORMGrid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ORMGrid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ORMGrid.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ORMGrid().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

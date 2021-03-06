/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.toedter.calendar.JDateChooser;
import entity.Stock;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.ModelFactory;

/**
 *
 * @author Nsat04
 */
public class StockController extends AbstractController {
    //initialisation

    private static StockController instance;

    public void index(JTable table) {
        // Recherche Tout
        ArrayList<Stock> listStocks = this.stockModel.findAll();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        int numero = 1;

        ArrayList datas = new ArrayList<>();
        Object[][] data;

        try {

            for (Stock listpStock : listStocks) {

                data = new Object[][]{{numero, listpStock.getLibelle(),
                    listpStock.getDate(),
                    listpStock.getQuantiteEntrant(),
                    listpStock.getCuEntrant(),
                    listpStock.getCtEntant(),
                    listpStock.getQuantiteSortant(),
                    listpStock.getCuSortant(),
                    listpStock.getCtSorttant(),
                    listpStock.getStockDisponible()}};
                numero++;
                model.addRow(data);

            }

        } catch (Exception e) {
        }

    }
    
    
public Stock searchStock_Table(JTable clientListTab) {
        //
        Stock stock = this.stock;
        //
        try {
            //
            int ligneSelectionnEeTab = clientListTab.getSelectedRow();
            //
            if (ligneSelectionnEeTab > -1) {
                //
                String idCli = clientListTab.getValueAt(ligneSelectionnEeTab, 3).toString();
                int quantiteEntrant = Integer.valueOf(clientListTab.getValueAt(ligneSelectionnEeTab, 1).toString());
               
                //
              
                stock.setQuantiteEntrant(quantiteEntrant);
               
                //
            }
            //
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erreur survenu lors du clientService.chargerClientTable"
                    + "\n [Détails] : \n" + ex.getMessage(), "Message d'Erreur !",
                    JOptionPane.WARNING_MESSAGE);
        }
        //
        return stock;
    }
//    public void indexAll(JTable stockTable) {
//        try {
//            //
//        
//            //
//            ArrayList <Stock> listStock = this.stockModel.findAll();
//            //
//            int nLigne = listStock.size();
//            //
//            Object[][] dataTab = new Object[nLigne][];
//            //
//            int cpt = 0;
//            for (Stock listpStock : listStock) {
//                //
//                Object[] tabLib = {(cpt + 1), listpStock.getLibelle(),
//                    listpStock.getDate(),
//                    listpStock.getQuantiteEntrant(),
//                    listpStock.getCuEntrant(),
//                    listpStock.getCtEntant(),
//                    listpStock.getQuantiteSortant(),
//                    listpStock.getCuSortant(),
//                    listpStock.getCtSorttant(),
//                    listpStock.getStockDisponible()};
//                //
//                dataTab[cpt] = tabLib;
//                //
//                cpt++;
//            }
//
//            stockTable.setModel(new javax.swing.table.DefaultTableModel(
//                    dataTab,
//                    new String[]{
//                        "N°", "Nom", "Téléphone", "IDClient"
//                    }
//            ) {
//                boolean[] canEdit = new boolean[]{
//                    false, false, false, false
//                };
//
//                public boolean isCellEditable(int rowIndex, int columnIndex) {
//                    return canEdit[columnIndex];
//                }
//            });
//
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Erreur survenu au niveau du StockConrolleur.findAll"
//                    + "\n [Détails] : \n" + ex.getMessage(), "Message d'Erreur !",
//                    JOptionPane.WARNING_MESSAGE);
//        }
//    }

    // Insertion
    public boolean create(JTextField libeleField, JTextField QteEtrantField, JTextField cuEtrantField, JComboBox productIdCmbo, JDateChooser datefield) {
        double cout_unitaire_entrant = 5000;

        int quantite_entrant = 50;

        if (libeleField.getText().equals("") || QteEtrantField.getText().equals("") || cuEtrantField.getText().equals("") || datefield.getDate() == null) {

            JOptionPane.showMessageDialog(null, "Veuiilez remplir tous les champs !", null, JOptionPane.ERROR_MESSAGE);
        } else {

            this.stock.setLibelle(libeleField.getText());

            // ON calcul le stock disponible
            this.stock.setCuEntrant(Double.parseDouble(QteEtrantField.getText().trim()));
            this.stock.setQuantiteEntrant(Integer.valueOf(cuEtrantField.getText().trim()));
            this.stock.setCtEntant(this.stock.getCuEntrant() * this.stock.getQuantiteEntrant());

            this.stock.setStockDisponible(Double.valueOf(quantite_entrant));
            this.product = ModelFactory.getInstance().getProductModel().find(Integer.parseInt(productIdCmbo.getSelectedItem().toString()));
            this.stock.setProduct(product);
            LocalDate localdate = datefield.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.stock.setDate(localdate);

            response = this.stockModel.insert(stock);
            if (response) {
                JOptionPane.showMessageDialog(null, "Insertion a bien reussi", null, JOptionPane.INFORMATION_MESSAGE);
                this.reset(libeleField, QteEtrantField, cuEtrantField, productIdCmbo, datefield);

            } else {
                JOptionPane.showMessageDialog(null, "Insertion a echouée", null, JOptionPane.ERROR_MESSAGE);
                this.reset(libeleField, QteEtrantField, cuEtrantField, productIdCmbo, datefield);
            }
        }
        return false;
    }
    //MISE A JOUR

    public boolean edit(JTextField stockFieldid, JTextField libeleField, JTextField QteEtrantField, JTextField cuEtrantField, JComboBox productIdCmbo, JDateChooser datefield) {
        this.stock = this.stockModel.find(Integer.parseInt(stockFieldid.getText()));
        double cout_unitaire_entrant = Double.parseDouble(cuEtrantField.getText());

        int quantite_entrant = Integer.parseInt(QteEtrantField.getText());

        this.stock.setLibelle(libeleField.getText());

        // ON calcul le stock disponible
        //this.stock.setStockDisponible(Double.valueOf(quantite_entrant - quantite_sorant));
        this.stock.setCtEntant(quantite_entrant * cout_unitaire_entrant);

        this.product = ModelFactory.getInstance().getProductModel().find(Integer.parseInt(productIdCmbo.getSelectedItem().toString()));
        this.stock.setProduct(product);
        LocalDate localdate = datefield.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        this.stock.setDate(localdate);

        response = this.stockModel.update(stock);
        if (response) {
            JOptionPane.showMessageDialog(null, "Mise a jour  a bien reussi", null, JOptionPane.INFORMATION_MESSAGE);
            this.reset(libeleField, QteEtrantField, cuEtrantField, productIdCmbo, datefield);
        } else {
            JOptionPane.showMessageDialog(null, "Mise a jour a echouée", null, JOptionPane.ERROR_MESSAGE);
            this.reset(libeleField, QteEtrantField, cuEtrantField, productIdCmbo, datefield);
        }
        return true;
    }

    //SUPPRESSION
    public boolean delete(JTextField FielId) {
        this.stock = ModelFactory.getInstance().getStockModel().find(Integer.parseInt(FielId.getText()));

        response = ModelFactory.getInstance().getProductModel().delete(this.stock.getId());
        if (response) {
            JOptionPane.showMessageDialog(null, "La Supression  a bien reussi");
        } else {
            JOptionPane.showMessageDialog(null, "La suppression a bien reussi");
        }
        return true;
    }

    // INSTANCE 
    public static StockController getInstance() {
        if (instance == null) {
            instance = new StockController();

        }
        return instance;
    }
}

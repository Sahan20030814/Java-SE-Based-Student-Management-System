/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.sahan.adyapana.gui;

import com.sahan.adyapana.model.MySQL;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Sahan Liyanage
 */
public class AddressView extends javax.swing.JDialog {

    private static String nic;
    private static String type_id;

    private static HashMap<String, String> provinceMap = new HashMap<>();
    private static HashMap<String, String> districtMap = new HashMap<>();

    public AddressView(java.awt.Frame parent, boolean modal, String fname, String lname, String nic, String type_id) {
        super(parent, modal);
        initComponents();
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/com/sahan/adyapana/recources/logo.jpg")));

        jLabel2.setText(fname + " " + lname + " (" + nic + ")");
        this.nic = nic;
        this.type_id = type_id;

        LoadProvince();
        LoadDistrict();
        LoadAddress();
    }

    private void LoadProvince() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `province` ORDER BY `name` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select Province");

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                provinceMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadDistrict() {

        if (jComboBox1.getSelectedItem().equals("Select Province")) {

            Vector<String> vector = new Vector<>();
            vector.add("Select District");

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `district` "
                        + "WHERE `province_id`='" + provinceMap.get(jComboBox1.getSelectedItem()) + "' ORDER BY `name` ASC");

                Vector<String> vector = new Vector<>();
                vector.add("Select District");

                while (resultSet.next()) {
                    vector.add(resultSet.getString("name"));
                    districtMap.put(resultSet.getString("name"), resultSet.getString("id"));
                }

                DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
                jComboBox2.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void LoadAddress() {

        try {

            if (type_id == "3") {

                ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `students` INNER JOIN `address` "
                        + "ON students.address_id=address.id INNER JOIN `district` "
                        + "ON address.district_id=district.id INNER JOIN `province` "
                        + "ON district.province_id=province.id WHERE students.`nic`='" + nic + "'");

                if (resultSet1.next()) {
                    jTextField1.setText(resultSet1.getString("address.line1"));
                    jTextField2.setText(resultSet1.getString("address.line2"));
                    jComboBox1.setSelectedItem(resultSet1.getString("province.name"));
                    jComboBox2.setSelectedItem(resultSet1.getString("district.name"));
                    jTextField4.setText(resultSet1.getString("address.city"));
                    jTextField3.setText(resultSet1.getString("address.postal_code"));
                    jButton1.setEnabled(false);
                    jButton2.setEnabled(true);
                } else {
                    Reset1();
                }

            } else if (type_id == "2") {

                ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `teachers` INNER JOIN `address` "
                        + "ON teachers.address_id=address.id INNER JOIN `district` "
                        + "ON address.district_id=district.id INNER JOIN `province` "
                        + "ON district.province_id=province.id WHERE teachers.`nic`='" + nic + "'");

                if (resultSet1.next()) {
                    jTextField1.setText(resultSet1.getString("address.line1"));
                    jTextField2.setText(resultSet1.getString("address.line2"));
                    jComboBox1.setSelectedItem(resultSet1.getString("province.name"));
                    jComboBox2.setSelectedItem(resultSet1.getString("district.name"));
                    jTextField4.setText(resultSet1.getString("address.city"));
                    jTextField3.setText(resultSet1.getString("address.postal_code"));
                    jButton1.setEnabled(false);
                    jButton2.setEnabled(true);
                } else {
                    Reset1();
                }

            } else {
                Reset1();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Reset1() {
        jTextField1.setText("");
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
        jTextField4.setText("");
        jTextField3.setText("");
        jButton1.setEnabled(true);
        jButton2.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Address View");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Address View");

        jLabel2.setFont(new java.awt.Font("Poppins SemiBold", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 102, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Full Name");

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Address Line 1");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("Address Line 2");

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel5.setText("Province");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("District");

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setText("City");

        jLabel8.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel8.setText("Postal Code");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField1.setToolTipText("Type Address Line 1");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField2.setToolTipText("Type Address Line 2");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField3.setToolTipText("Type Postal Code");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jTextField4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField4.setToolTipText("Type Employee's City");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 153, 0));
        jButton1.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 153, 0));
        jButton2.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("UPDATE");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(153, 153, 153));
        jButton4.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("CLEAR ALL");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.LEADING, 0, 435, Short.MAX_VALUE)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
                                .addGap(1, 1, 1))
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the Address Line 1.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField1.getText().length() > 100) {

            JOptionPane.showMessageDialog(this, "Address Line 1 must contain less than 100 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField2.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the Address Line 2.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (jTextField2.getText().length() > 100) {

            JOptionPane.showMessageDialog(this, "Address Line 2 must contain less than 100 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (String.valueOf(jComboBox1.getSelectedItem()).equals("Select Province")) {

            JOptionPane.showMessageDialog(this, "Please Select the Province.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox1.grabFocus();

        } else if (String.valueOf(jComboBox2.getSelectedItem()).equals("Select District")) {

            JOptionPane.showMessageDialog(this, "Please Select the District.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox2.grabFocus();

        } else if (jTextField4.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the City.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField4.grabFocus();

        } else if (jTextField4.getText().length() > 30) {

            JOptionPane.showMessageDialog(this, "City must contain less than 30 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField4.grabFocus();

        } else if (jTextField3.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the City Postal Code.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField3.grabFocus();

        } else if (jTextField3.getText().length() != 5) {

            JOptionPane.showMessageDialog(this, "Postal Code must contain only 5 characters.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTextField3.grabFocus();

        } else {

            String line1 = jTextField1.getText();
            String line2 = jTextField2.getText();
            String district = String.valueOf(jComboBox2.getSelectedItem());
            String city = jTextField4.getText();
            String postal_code = jTextField3.getText();

            try {

                ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `address` WHERE `line1`='" + line1 + "' AND `line2`='" + line2 + "' "
                        + "AND `city`='" + city + "' AND `postal_code`='" + postal_code + "' AND `district_id`='" + districtMap.get(district) + "'");

                if (resultSet1.next()) {

                    if (type_id == "3") {
                        MySQL.executeIUD("UPDATE `students` SET `address_id`='" + resultSet1.getString("id") + "' WHERE `nic`='" + nic + "'");
                    } else if (type_id == "2") {
                        MySQL.executeIUD("UPDATE `teachers` SET `address_id`='" + resultSet1.getString("id") + "' WHERE `nic`='" + nic + "'");
                    }

                    LoadDistrict();
                    LoadAddress();

                    JOptionPane.showMessageDialog(this, "Address registered successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    MySQL.executeIUD("INSERT INTO `address` (`line1`,`line2`,`city`,`postal_code`,`district_id`) "
                            + "VALUES ('" + line1 + "','" + line2 + "','" + city + "','" + postal_code + "','" + districtMap.get(district) + "')");

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `address` WHERE `line1`='" + line1 + "' AND `line2`='" + line2 + "' "
                            + "AND `city`='" + city + "' AND `postal_code`='" + postal_code + "' AND `district_id`='" + districtMap.get(district) + "'");

                    if (resultSet2.next()) {

                        if (type_id == "3") {
                            MySQL.executeIUD("UPDATE `students` SET `address_id`='" + resultSet2.getString("id") + "' WHERE `nic`='" + nic + "'");
                        } else if (type_id == "2") {
                            MySQL.executeIUD("UPDATE `teachers` SET `address_id`='" + resultSet2.getString("id") + "' WHERE `nic`='" + nic + "'");
                        }

                        LoadDistrict();
                        LoadAddress();

                        JOptionPane.showMessageDialog(this, "Address registered successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        LoadDistrict();

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        LoadDistrict();
        LoadAddress();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (jTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the Address Line 1.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField1.getText().length() > 100) {

            JOptionPane.showMessageDialog(this, "Address Line 1 must contain less than 100 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField2.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the Address Line 2.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (jTextField2.getText().length() > 100) {

            JOptionPane.showMessageDialog(this, "Address Line 2 must contain less than 100 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (String.valueOf(jComboBox1.getSelectedItem()).equals("Select Province")) {

            JOptionPane.showMessageDialog(this, "Please Select the Province.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox1.grabFocus();

        } else if (String.valueOf(jComboBox2.getSelectedItem()).equals("Select District")) {

            JOptionPane.showMessageDialog(this, "Please Select the District.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox2.grabFocus();

        } else if (jTextField4.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the City.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField4.grabFocus();

        } else if (jTextField4.getText().length() > 30) {

            JOptionPane.showMessageDialog(this, "City must contain less than 30 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField4.grabFocus();

        } else if (jTextField3.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter the City Postal Code.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField3.grabFocus();

        } else if (jTextField3.getText().length() != 5) {

            JOptionPane.showMessageDialog(this, "Postal Code must contain only 5 characters.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTextField3.grabFocus();

        } else {

            String line1 = jTextField1.getText();
            String line2 = jTextField2.getText();
            String district = String.valueOf(jComboBox2.getSelectedItem());
            String city = jTextField4.getText();
            String postal_code = jTextField3.getText();

            try {

                ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `address` WHERE `line1`='" + line1 + "' AND `line2`='" + line2 + "' "
                        + "AND `city`='" + city + "' AND `postal_code`='" + postal_code + "' AND `district_id`='" + districtMap.get(district) + "'");

                if (resultSet1.next()) {

                    if (type_id == "3") {
                        MySQL.executeIUD("UPDATE `students` SET `address_id`='" + resultSet1.getString("id") + "' WHERE `nic`='" + nic + "'");
                    } else if (type_id == "2") {
                        MySQL.executeIUD("UPDATE `teachers` SET `address_id`='" + resultSet1.getString("id") + "' WHERE `nic`='" + nic + "'");
                    }

                    LoadDistrict();
                    LoadAddress();

                    JOptionPane.showMessageDialog(this, "Address updated successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    MySQL.executeIUD("INSERT INTO `address` (`line1`,`line2`,`city`,`postal_code`,`district_id`) "
                            + "VALUES ('" + line1 + "','" + line2 + "','" + city + "','" + postal_code + "','" + districtMap.get(district) + "')");

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `address` WHERE `line1`='" + line1 + "' AND `line2`='" + line2 + "' "
                            + "AND `city`='" + city + "' AND `postal_code`='" + postal_code + "' AND `district_id`='" + districtMap.get(district) + "'");

                    if (resultSet2.next()) {

                        if (type_id == "3") {
                            MySQL.executeIUD("UPDATE `students` SET `address_id`='" + resultSet2.getString("id") + "' WHERE `nic`='" + nic + "'");
                        } else if (type_id == "2") {
                            MySQL.executeIUD("UPDATE `teachers` SET `address_id`='" + resultSet2.getString("id") + "' WHERE `nic`='" + nic + "'");
                        }

                        LoadDistrict();
                        LoadAddress();

                        JOptionPane.showMessageDialog(this, "Address Details updated successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed


    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed


    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed


    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed


    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed


    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed


    }//GEN-LAST:event_jTextField3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables

}

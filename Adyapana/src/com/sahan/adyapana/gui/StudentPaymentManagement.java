/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.sahan.adyapana.gui;

import com.sahan.adyapana.model.MySQL;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Sahan Liyanage
 */
public class StudentPaymentManagement extends javax.swing.JPanel {

    private static HashMap<String, String> teacherMap = new HashMap<>();
    private static HashMap<String, String> subjectMap = new HashMap<>();
    private static HashMap<String, String> yearMap = new HashMap<>();
    private static HashMap<String, String> monthMap = new HashMap<>();
    private static HashMap<String, String> priceIdMap = new HashMap<>();

    public StudentPaymentManagement() {
        initComponents();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, renderer);
        jTable2.setDefaultRenderer(Object.class, renderer);

        LoadTeachers();
        LoadSubjects();
        LoadYears();
        LoadMonths();
        LoadClasses();
        LoadInvoices();
    }

    private void LoadTeachers() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `teachers` WHERE `status_id`='1' ORDER BY `fname` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {

                String gender = "";

                if (resultSet.getString("gender_id").equals("1")) {
                    gender = "Mr.";
                } else if (resultSet.getString("gender_id").equals("2")) {
                    gender = "Miss.";
                }

                vector.add(gender + " " + resultSet.getString("fname") + " " + resultSet.getString("lname"));
                teacherMap.put(gender + " " + resultSet.getString("fname") + " " + resultSet.getString("lname"), resultSet.getString("tno"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadSubjects() {

        Vector<String> vector = new Vector<>();

        if (jComboBox4.getSelectedItem().equals("Select")) {
            vector.add("Select");
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `teachers` INNER JOIN `class` "
                        + "ON teachers.tno=class.teachers_tno INNER JOIN `subject` ON "
                        + "class.subject_sub_no=subject.sub_no WHERE class.`teachers_tno`='" + teacherMap.get(jComboBox4.getSelectedItem()) + "' "
                        + "ORDER BY subject.`name` ASC");

                vector.add("Select");

                while (resultSet.next()) {
                    if (!vector.contains(resultSet.getString("subject.name"))) {
                        vector.add(resultSet.getString("subject.name"));
                        subjectMap.put(resultSet.getString("subject.name"), resultSet.getString("subject.sub_no"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
        jComboBox5.setModel(model);

    }

    private void LoadYears() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `year` "
                    + "WHERE `year`>='" + new SimpleDateFormat("yyyy").format(new Date()) + "' ORDER BY `id` ASC");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("year"));
                yearMap.put(resultSet.getString("year"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox6.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadMonths() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `month` ORDER BY `id` ASC");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("month"));
                monthMap.put(resultSet.getString("month"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadClasses() {

        if (jTable1.getSelectedRow() == -1) {

            try {

                String query = "SELECT * FROM `class` INNER JOIN `teachers` ON class.teachers_tno=teachers.tno "
                        + "INNER JOIN `subject` ON class.subject_sub_no=subject.sub_no WHERE `status_id`='1' AND ";

                if (!jComboBox4.getSelectedItem().equals("Select")) {
                    query += "class.`teachers_tno`='" + teacherMap.get(jComboBox4.getSelectedItem()) + "' AND ";
                }

                if (!jComboBox5.getSelectedItem().equals("Select")) {
                    query += "class.`subject_sub_no`='" + subjectMap.get(jComboBox5.getSelectedItem()) + "' AND ";
                }

                query += "ORDER BY ";

                query = query.replace("WHERE ORDER BY", "ORDER BY");
                query = query.replace("AND ORDER BY", "ORDER BY");

                String sort = String.valueOf(jComboBox3.getSelectedItem());

                if (sort == "Class No ASC") {
                    query += "class.`class_no` ASC";
                } else if (sort == "Class No DESC") {
                    query += "class.`class_no` DESC";
                } else if (sort == "Teacher ASC") {
                    query += "teachers.`fname` ASC";
                } else if (sort == "Teacher DESC") {
                    query += "teachers.`fname` DESC";
                } else if (sort == "Subject ASC") {
                    query += "subject.`name` ASC";
                } else if (sort == "Subject DESC") {
                    query += "subject.`name` DESC";
                }

                ResultSet resultSet = MySQL.executeSearch(query);

                String year = String.valueOf(jComboBox6.getSelectedItem());
                String month = "";

                if (year.equals(new SimpleDateFormat("yyyy").format(new Date()))) {

                    if (Integer.parseInt(monthMap.get(jComboBox2.getSelectedItem())) < Integer.parseInt(new SimpleDateFormat("MM").format(new Date()))) {

                        jComboBox2.setSelectedIndex(1 - Integer.parseInt(new SimpleDateFormat("MM").format(new Date())));
                    }

                }

                month = new DecimalFormat("00").format(Integer.parseInt(monthMap.get(jComboBox2.getSelectedItem())));

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `class_schedule` "
                            + "WHERE `class_class_no`='" + resultSet.getString("class.class_no") + "' AND "
                            + "`date` LIKE '" + year + "-" + month + "%'");

                    if (resultSet2.next()) {

                        Vector<String> vector = new Vector<>();
                        vector.add(resultSet.getString("class.class_no"));

                        String gender = "";

                        if (resultSet.getString("teachers.gender_id").equals("1")) {
                            gender = "Mr.";
                        } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                            gender = "Miss.";
                        }

                        vector.add(gender + " " + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname"));

                        vector.add(resultSet.getString("subject.name"));
                        vector.add(String.valueOf(jComboBox6.getSelectedItem()));
                        vector.add(String.valueOf(jComboBox2.getSelectedItem()));

                        ResultSet resultSet3 = MySQL.executeSearch("SELECT * FROM `invoice` INNER JOIN `price` "
                                + "ON invoice.price_id=price.id WHERE invoice.`class_class_no`='" + resultSet.getString("class.class_no") + "' "
                                + "AND invoice.`year_id`='" + yearMap.get(jComboBox6.getSelectedItem()) + "' AND "
                                + "invoice.`month_id`='" + monthMap.get(jComboBox2.getSelectedItem()) + "'");

                        if (resultSet3.next()) {
                            vector.add(resultSet3.getString("price.price"));
                            priceIdMap.put(resultSet.getString("class.class_no"), resultSet3.getString("price.id"));
                        } else {

                            ResultSet resultSet4 = MySQL.executeSearch("SELECT * FROM `price` "
                                    + "WHERE `subject_sub_no`='" + resultSet.getString("subject.sub_no") + "' AND `registered_date` IN "
                                    + "(SELECT MAX(`registered_date`) FROM `price` "
                                    + "WHERE `subject_sub_no`='" + resultSet.getString("subject.sub_no") + "')");

                            if (resultSet4.next()) {
                                vector.add(resultSet4.getString("price.price"));
                                priceIdMap.put(resultSet.getString("class.class_no"), resultSet4.getString("price.id"));
                            } else {
                                vector.add("Not scheduled yet.");
                            }

                        }

                        model.addRow(vector);

                    }

                }

                jTable1.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void LoadInvoices() {

        try {

            String query = "SELECT * FROM `invoice` INNER JOIN `students` "
                    + "ON invoice.students_sno=students.sno INNER JOIN `year` ON "
                    + "invoice.year_id=year.id INNER JOIN `month` ON "
                    + "invoice.month_id=month.id INNER JOIN `class` ON "
                    + "invoice.class_class_no=class.class_no INNER JOIN `teachers` ON "
                    + "class.teachers_tno=teachers.tno "
                    + "INNER JOIN `subject` ON class.subject_sub_no=subject.sub_no "
                    + "WHERE students.`nic` LIKE '" + jTextField1.getText() + "%' ORDER BY ";

            String sort = String.valueOf(jComboBox1.getSelectedItem());

            if (sort == "INV_ID ASC") {
                query += "invoice.`id` ASC";
            } else if (sort == "INV_ID DESC") {
                query += "invoice.`id` DESC";
            } else if (sort == "Student NIC ASC") {
                query += "students.`nic` ASC";
            } else if (sort == "Student NIC DESC") {
                query += "students.`nic` DESC";
            } else if (sort == "Teacher Name ASC") {
                query += "teachers.`fname` ASC";
            } else if (sort == "Teacher Name DESC") {
                query += "teachers.`fname` DESC";
            } else if (sort == "Subject ASC") {
                query += "subject.`name` ASC";
            } else if (sort == "Subject DESC") {
                query += "subject.`name` DESC";
            } else if (sort == "Year ASC") {
                query += "year.`year` ASC";
            } else if (sort == "Year DESC") {
                query += "year.`year` DESC";
            } else if (sort == "Month ASC") {
                query += "month.`id` ASC";
            } else if (sort == "Month DESC") {
                query += "month.`id` DESC";
            } else if (sort == "Payment ASC") {
                query += "invoice.`value` ASC";
            } else if (sort == "Payment DESC") {
                query += "invoice.`value` DESC";
            } else if (sort == "Payment Date ASC") {
                query += "invoice.`dateTime` ASC";
            } else if (sort == "Payment Date DESC") {
                query += "invoice.`dateTime` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("invoice.id"));
                vector.add(resultSet.getString("students.nic"));

                String gender = "";

                if (resultSet.getString("teachers.gender_id").equals("1")) {
                    gender = "Mr.";
                } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                    gender = "Miss.";
                }

                vector.add(gender + " " + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname"));

                vector.add(resultSet.getString("subject.name"));
                vector.add(resultSet.getString("year.year"));
                vector.add(resultSet.getString("month.month"));
                vector.add(resultSet.getString("invoice.value"));
                vector.add(resultSet.getString("invoice.dateTime"));

                model.addRow(vector);
            }

            jTable2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Reset() {
        jFormattedTextField1.setText("");
        jButton4.setEnabled(false);
        jTable1.clearSelection();
        jTable2.clearSelection();
        jTextField1.setText("");
        jComboBox4.setEnabled(true);
        jComboBox5.setEnabled(true);
        jComboBox6.setEnabled(true);
        jComboBox2.setEnabled(true);
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jComboBox6.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
    }

    private void Reset2() {
        jFormattedTextField1.setText("");
        jButton4.setEnabled(false);
        jTable1.clearSelection();
        jTable2.clearSelection();
        jComboBox4.setEnabled(true);
        jComboBox5.setEnabled(true);
        jComboBox6.setEnabled(true);
        jComboBox2.setEnabled(true);
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jComboBox6.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(1110, 681));

        jPanel4.setPreferredSize(new java.awt.Dimension(1110, 152));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Student NIC");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("Subject");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Month");

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Student Payment Management");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jComboBox2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setText("Teacher");

        jComboBox4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jComboBox5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel7.setText("Year");

        jComboBox6.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, 0, 176, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox5, 0, 173, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox6, 0, 90, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, 164, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox2)
                        .addComponent(jComboBox4))
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(1110, 529));

        jButton4.setBackground(new java.awt.Color(51, 153, 0));
        jButton4.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Pay");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(153, 153, 153));
        jButton5.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Clear All");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jButton7.setBackground(new java.awt.Color(255, 0, 102));
        jButton7.setFont(new java.awt.Font("Poppins SemiBold", 0, 16)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Print Report");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jComboBox3.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(255, 0, 102));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Class No ASC", "Class No DESC", "Teacher ASC", "Teacher DESC", "Subject ASC", "Subject DESC" }));
        jComboBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 102));
        jLabel11.setText("Sort By :");

        jTable1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Class No", "Teacher", "Subject", "Year", "Month", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setMinimumSize(new java.awt.Dimension(150, 254));
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jFormattedTextField1.setEditable(false);
        jFormattedTextField1.setForeground(new java.awt.Color(255, 0, 102));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextField1.setFont(new java.awt.Font("Poppins", 1, 22)); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "INV_ID", "Student NIC", "Teacher  Name", "Subject", "Year", "Month", "Payment", "Payment Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        jSeparator3.setForeground(new java.awt.Color(51, 51, 51));

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 102));
        jLabel5.setText("Sort By :");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 0, 102));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "INV_ID ASC", "INV_ID DESC", "Student NIC ASC", "Student NIC DESC", "Teacher Name ASC", "Teacher Name DESC", "Subject ASC", "Subject DESC", "Year ASC", "Year DESC", "Month ASC", "Month DESC", "Payment ASC", "Payment DESC", "Payment Date ASC", "Payment Date DESC" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.getSelectedRow() != -1) {

            int row = jTable1.getSelectedRow();

            jButton4.setEnabled(true);

            jComboBox4.setSelectedItem(jTable1.getValueAt(row, 1));
            LoadSubjects();
            jComboBox5.setSelectedItem(jTable1.getValueAt(row, 2));
            jComboBox6.setSelectedItem(jTable1.getValueAt(row, 3));
            jComboBox2.setSelectedItem(jTable1.getValueAt(row, 4));
            jFormattedTextField1.setText(String.valueOf(jTable1.getValueAt(row, 5)));

            jComboBox4.setEnabled(false);
            jComboBox5.setEnabled(false);
            jComboBox6.setEnabled(false);
            jComboBox2.setEnabled(false);

            if (jTable1.getValueAt(row, 5).equals("Not scheduled yet.")) {
                jButton4.setEnabled(false);
            } else {
                jButton4.setEnabled(true);
            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (jTable2.getRowCount() > 0) {

            try {

                InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_students_payments.jasper");

                HashMap<String, Object> param = new HashMap<>();

                param.put("Parameter1", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                param.put("Parameter2", new SimpleDateFormat("hh:mm:ss a").format(new Date()));

                JRTableModelDataSource dataSources = new JRTableModelDataSource(jTable2.getModel());

                JasperPrint report = JasperFillManager.fillReport(s, param, dataSources);

                JasperViewer.viewReport(report, false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            JOptionPane.showMessageDialog(this, "No data in the table.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTable2.grabFocus();

        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged

        LoadSubjects();
        LoadClasses();

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        LoadInvoices();

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        LoadInvoices();

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Reset();
        LoadSubjects();
        LoadClasses();
        LoadInvoices();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (jTable1.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(this, "Please Select a class to pay.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTable1.grabFocus();

        } else {

            if (jTextField1.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter the Student's Nic Number first.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField1.grabFocus();

            } else {

                try {

                    int row = jTable1.getSelectedRow();

                    String student_nic = jTextField1.getText();

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `students` WHERE `nic`='" + student_nic + "' AND `status_id`='1'");

                    if (resultSet.next()) {

                        String sno = resultSet.getString("sno");
                        String fname = resultSet.getString("fname");
                        String lname = resultSet.getString("lname");
                        String year = String.valueOf(jComboBox6.getSelectedItem());
                        String month = String.valueOf(jComboBox2.getSelectedItem());
                        String class_no = String.valueOf(jTable1.getValueAt(row, 0));
                        String price = jFormattedTextField1.getText();
                        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        String teacher = String.valueOf(jComboBox4.getSelectedItem());
                        String subject = String.valueOf(jComboBox5.getSelectedItem());

                        ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `invoice` "
                                + "WHERE `students_sno`='" + sno + "' AND `class_class_no`='" + class_no + "' AND "
                                + "`year_id`='" + yearMap.get(year) + "' AND `month_id`='" + monthMap.get(month) + "'");

                        if (resultSet2.next()) {

                            JOptionPane.showMessageDialog(this, "Selected Student (" + student_nic + ") has already paid for " + month + " month of selected class.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);

                            jTable2.grabFocus();

                        } else {

                            if (!price.equals("Not scheduled yet.")) {

                                int option = JOptionPane.showConfirmDialog(this, "Data is correct? "
                                        + "\nStudent Name : " + fname + " " + lname + " \nStudent NIC : " + student_nic, "Confirm",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                if (option == JOptionPane.YES_OPTION) {

                                    String invoice_id = "";

                                    MySQL.executeIUD("INSERT INTO `invoice` (`value`,`students_sno`,`class_class_no`,`year_id`,`month_id`,`price_id`,`dateTime`) "
                                            + "VALUES ('" + price + "','" + sno + "','" + class_no + "','" + yearMap.get(year) + "','" + monthMap.get(month) + "',"
                                            + "'" + priceIdMap.get(class_no) + "','" + dateTime + "')");

                                    ResultSet resultSet3 = MySQL.executeSearch("SELECT * FROM `invoice` WHERE `value`='" + price + "' AND `students_sno`='" + sno + "' "
                                            + "AND `class_class_no`='" + class_no + "' AND `year_id`='" + yearMap.get(year) + "' AND `month_id`='" + monthMap.get(month) + "' "
                                            + "AND `price_id`='" + priceIdMap.get(class_no) + "' AND `dateTime`='" + dateTime + "'");

                                    if (resultSet3.next()) {
                                        invoice_id = resultSet3.getString("id");
                                    }

                                    InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_invoice.jasper");

                                    HashMap<String, Object> param = new HashMap<>();

                                    param.put("Parameter1", invoice_id);
                                    param.put("Parameter2", student_nic);
                                    param.put("Parameter3", fname + " " + lname);
                                    param.put("Parameter4", teacher);
                                    param.put("Parameter5", subject);
                                    param.put("Parameter6", dateTime);
                                    param.put("Parameter7", month);
                                    param.put("Parameter8", price);

                                    JREmptyDataSource dataSources = new JREmptyDataSource();

                                    JasperPrint report = JasperFillManager.fillReport(s, param, dataSources);

                                    Reset2();
                                    LoadSubjects();
                                    LoadClasses();
                                    LoadInvoices();

                                    JOptionPane.showMessageDialog(this, "Payment has been successful.",
                                            "Success", JOptionPane.INFORMATION_MESSAGE);

                                    JasperPrintManager.printReport(report, false);
                                    JasperViewer.viewReport(report, false);

                                }

                            } else {

                                JOptionPane.showMessageDialog(this, "Something went wrong.",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(this, "Incorrect NIC Number.",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        jTextField1.grabFocus();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

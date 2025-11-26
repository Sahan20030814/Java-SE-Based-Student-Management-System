/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.sahan.adyapana.gui;

import com.sahan.adyapana.model.MySQL;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;
import raven.toast.Notifications;

/**
 *
 * @author Sahan Liyanage
 */
public class ClassRegistration extends javax.swing.JPanel {

    private static HashMap<String, String> teacherMap = new HashMap<>();
    private static HashMap<String, String> subjectMap = new HashMap<>();
    private static HashMap<String, String> timeslotMap = new HashMap<>();

    public ClassRegistration() {
        initComponents();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, renderer);

        LoadTeachers();
        LoadSubjects();
        LoadTimeslots();
        LoadClasses();
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
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadSubjects() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `subject` ORDER BY `name` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                subjectMap.put(resultSet.getString("name"), resultSet.getString("sub_no"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadTimeslots() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `timeslot` ORDER BY `id` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("timeslot"));
                timeslotMap.put(resultSet.getString("timeslot"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadClasses() {

        if (jButton3.isEnabled()) {

            try {

                String query = "SELECT * FROM `class` INNER JOIN `teachers` ON class.teachers_tno=teachers.tno "
                        + "INNER JOIN `subject` ON class.subject_sub_no=subject.sub_no INNER JOIN `class_schedule` "
                        + "ON class.class_no=class_schedule.class_class_no INNER JOIN `timeslot` ON class_schedule.timeslot_id=timeslot.id "
                        + "WHERE class_schedule.`date` >= '" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' AND `status_id`='1' AND ";

                if (!jComboBox1.getSelectedItem().equals("Select")) {
                    query += "class.`teachers_tno`='" + teacherMap.get(jComboBox1.getSelectedItem()) + "' AND ";
                }

                if (!jComboBox4.getSelectedItem().equals("Select")) {
                    query += "class.`subject_sub_no`='" + subjectMap.get(jComboBox4.getSelectedItem()) + "' AND ";
                }

                if (!jComboBox2.getSelectedItem().equals("Select")) {
                    query += "class_schedule.`timeslot_id`='" + timeslotMap.get(jComboBox2.getSelectedItem()) + "' ";
                }

                query += "ORDER BY ";

                query = query.replace("WHERE ORDER BY", "ORDER BY");
                query = query.replace("AND ORDER BY", "ORDER BY");

                String sort = String.valueOf(jComboBox3.getSelectedItem());

                if (sort == "Class Scheduled ID ASC") {
                    query += "class_schedule.`id` ASC";
                } else if (sort == "Class Scheduled ID DESC") {
                    query += "class_schedule.`id` DESC";
                } else if (sort == "Class No ASC") {
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
                } else if (sort == "Scheduled Date ASC") {
                    query += "class_schedule.`date` ASC";
                } else if (sort == "Scheduled Date DESC") {
                    query += "class_schedule.`date` DESC";
                } else if (sort == "Scheduled Timeslot ASC") {
                    query += "timeslot.`id` ASC";
                } else if (sort == "Scheduled Timeslot DESC") {
                    query += "timeslot.`id` DESC";
                }

                ResultSet resultSet = MySQL.executeSearch(query);

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet.getString("class_schedule.id"));
                    vector.add(resultSet.getString("class.class_no"));

                    String gender = "";

                    if (resultSet.getString("teachers.gender_id").equals("1")) {
                        gender = "Mr.";
                    } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                        gender = "Miss.";
                    }

                    vector.add(gender + " " + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname"));

                    vector.add(resultSet.getString("subject.name"));
                    vector.add(resultSet.getString("class_schedule.date"));
                    vector.add(resultSet.getString("timeslot.timeslot"));

                    model.addRow(vector);
                }

                jTable1.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void Reset() {
        jComboBox1.setEnabled(true);
        jComboBox4.setEnabled(true);
        jDateChooser1.setEnabled(true);
        jComboBox2.setEnabled(true);
        jComboBox1.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jDateChooser1.setDate(null);
        jComboBox2.setSelectedIndex(0);
        jButton3.setEnabled(true);
        jButton4.setEnabled(false);
        jTable1.clearSelection();
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
        jComboBox1 = new javax.swing.JComboBox<>();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1047, 669));

        jPanel4.setPreferredSize(new java.awt.Dimension(1047, 273));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Teacher ");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("Date");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Timeslot");

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Class Registration");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N

        jComboBox2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setText("Subject");

        jComboBox4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox1)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox2)
                    .addComponent(jComboBox4))
                .addGap(10, 10, 10))
        );

        jButton3.setBackground(new java.awt.Color(51, 153, 0));
        jButton3.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Register");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 153, 0));
        jButton4.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Update");
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
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Class Scheduled ID ASC", "Class Scheduled ID DESC", "Class No ASC", "Class No DESC", "Teacher ASC", "Teacher DESC", "Subject ASC", "Subject DESC", "Scheduled Date ASC", "Scheduled Date DESC", "Scheduled Timeslot ASC", "Scheduled Timeslot DESC" }));
        jComboBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 102));
        jLabel11.setText("Sort By :");

        jTable1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Class Schedled ID", "Class No", "Teacher", "Subject", "Scheduled Date", "Scheduled Timeslot"
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                        .addGap(60, 60, 60)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.getSelectedRow() != -1) {

            int row = jTable1.getSelectedRow();

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `invoice` "
                        + "WHERE `class_class_no`='" + String.valueOf(jTable1.getValueAt(row, 1)) + "'");

                if (resultSet.next()) {

                    jComboBox1.setEnabled(false);
                    jComboBox4.setEnabled(false);

                    if (String.valueOf(jTable1.getValueAt(row, 4)).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {

                        String time = "";

                        if (String.valueOf(jTable1.getValueAt(row, 5)).equals("08.00 AM - 12.00 PM")) {
                            time = "08:00:00";
                        } else if (String.valueOf(jTable1.getValueAt(row, 5)).equals("12.30 PM - 04.30 PM")) {
                            time = "12:30:00";
                        } else if (String.valueOf(jTable1.getValueAt(row, 5)).equals("05.00 PM - 09.00 PM")) {
                            time = "17:00:00";
                        }

                        if (!LocalTime.parse(time).isAfter(LocalTime.now())) {

                            jDateChooser1.setEnabled(false);
                            jComboBox2.setEnabled(false);
                            jButton4.setEnabled(false);

                        } else {
                            jDateChooser1.setEnabled(true);
                            jComboBox2.setEnabled(true);
                            jButton4.setEnabled(true);
                        }

                    } else {
                        jDateChooser1.setEnabled(true);
                        jComboBox2.setEnabled(true);
                        jButton4.setEnabled(true);
                    }

                } else {

                    if (String.valueOf(jTable1.getValueAt(row, 4)).equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))) {

                        String time = "";

                        if (String.valueOf(jTable1.getValueAt(row, 5)).equals("08.00 AM - 12.00 PM")) {
                            time = "08:00:00";
                        } else if (String.valueOf(jTable1.getValueAt(row, 5)).equals("12.30 PM - 04.30 PM")) {
                            time = "12:30:00";
                        } else if (String.valueOf(jTable1.getValueAt(row, 5)).equals("05.00 PM - 09.00 PM")) {
                            time = "17:00:00";
                        }

                        if (!LocalTime.parse(time).isAfter(LocalTime.now())) {

                            jComboBox1.setEnabled(false);
                            jComboBox4.setEnabled(false);
                            jDateChooser1.setEnabled(false);
                            jComboBox2.setEnabled(false);
                            jButton4.setEnabled(false);

                        } else {
                            jComboBox1.setEnabled(true);
                            jComboBox4.setEnabled(true);
                            jDateChooser1.setEnabled(true);
                            jComboBox2.setEnabled(true);
                            jButton4.setEnabled(true);
                        }

                    } else {
                        jComboBox1.setEnabled(true);
                        jComboBox4.setEnabled(true);
                        jDateChooser1.setEnabled(true);
                        jComboBox2.setEnabled(true);
                        jButton4.setEnabled(true);
                    }

                }

                jButton3.setEnabled(false);

                jComboBox1.setSelectedItem(jTable1.getValueAt(row, 2));
                jComboBox4.setSelectedItem(jTable1.getValueAt(row, 3));
                jDateChooser1.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(jTable1.getValueAt(row, 4))));
                jComboBox2.setSelectedItem(jTable1.getValueAt(row, 5));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (jTable1.getRowCount() > 0) {

            try {

                InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_classes.jasper");

                HashMap<String, Object> param = new HashMap<>();

                param.put("Parameter1", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                param.put("Parameter2", new SimpleDateFormat("hh:mm:ss a").format(new Date()));

                JRTableModelDataSource dataSources = new JRTableModelDataSource(jTable1.getModel());

                JasperPrint report = JasperFillManager.fillReport(s, param, dataSources);

                JasperViewer.viewReport(report, false);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            JOptionPane.showMessageDialog(this, "No data in the table.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTable1.grabFocus();

        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        LoadClasses();

    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (jComboBox1.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the teacher first.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox1.grabFocus();

        } else if (jComboBox4.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the subject.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox4.grabFocus();

        } else if (jDateChooser1.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Please Select the date.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jDateChooser1.grabFocus();

        } else if (LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate())).isBefore(LocalDate.now())) {

            JOptionPane.showMessageDialog(this, "Entered date must not be earlier than today.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jDateChooser1.grabFocus();

        } else if (jComboBox2.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the Timeslot.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox2.grabFocus();

        } else {

            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

            if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("08:00:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("08.00 AM - 12.00 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("12:30:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("12.30 PM - 04.30 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("17:00:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("05.00 PM - 09.00 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else {

                String teacher = String.valueOf(jComboBox1.getSelectedItem());
                String subject = String.valueOf(jComboBox4.getSelectedItem());
                String date = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate());
                String timeslot = String.valueOf(jComboBox2.getSelectedItem());

                try {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `class` INNER JOIN `teachers` ON class.teachers_tno=teachers.tno "
                            + "INNER JOIN `subject` ON class.subject_sub_no=subject.sub_no INNER JOIN `class_schedule` "
                            + "ON class.class_no=class_schedule.class_class_no WHERE class_schedule.`date`='" + date + "' "
                            + "AND class_schedule.`timeslot_id`='" + timeslotMap.get(timeslot) + "' AND `status_id`='1'");

                    if (resultSet.next()) {

                        String gender = "";

                        if (resultSet.getString("teachers.gender_id").equals("1")) {
                            gender = "Mr.";
                        } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                            gender = "Miss.";
                        }

                        JOptionPane.showMessageDialog(this, "There is a class already scheduled in the selected timeslot."
                                + "\nClass No : " + resultSet.getString("class.class_no") + "\nTeacher : "
                                + gender + " " + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname")
                                + "\nSubject : " + resultSet.getString("subject.name"),
                                "Error", JOptionPane.ERROR_MESSAGE);

                        jComboBox2.grabFocus();

                    } else {

                        String class_no = "";

                        ResultSet resultset2 = MySQL.executeSearch("SELECT * FROM `class` WHERE `teachers_tno`='" + teacherMap.get(teacher) + "' AND "
                                + "`subject_sub_no`='" + subjectMap.get(subject) + "'");

                        if (resultset2.next()) {
                            class_no = resultset2.getString("class_no");
                        } else {

                            MySQL.executeIUD("INSERT INTO `class` (`teachers_tno`,`subject_sub_no`) "
                                    + "VALUES('" + teacherMap.get(teacher) + "','" + subjectMap.get(subject) + "')");

                            ResultSet resultset3 = MySQL.executeSearch("SELECT * FROM `class` WHERE `teachers_tno`='" + teacherMap.get(teacher) + "' AND "
                                    + "`subject_sub_no`='" + subjectMap.get(subject) + "'");

                            if (resultset3.next()) {
                                class_no = resultset3.getString("class_no");
                            }

                        }

                        if (class_no != null) {

                            MySQL.executeIUD("INSERT INTO `class_schedule` (`date`,`class_class_no`,`timeslot_id`) "
                                    + "VALUES('" + date + "','" + class_no + "','" + timeslotMap.get(timeslot) + "')");

                            Reset();
                            LoadClasses();

                            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "New Class registered Successfully!");

                        } else {

                            JOptionPane.showMessageDialog(this, "Something went wrong. Please try again.",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Reset();
        LoadClasses();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (jTable1.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(this, "Please Select a class to update.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTable1.grabFocus();

        } else if (jComboBox1.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the teacher first.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox1.grabFocus();

        } else if (jComboBox4.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the subject.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox4.grabFocus();

        } else if (jDateChooser1.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Please Select the date.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jDateChooser1.grabFocus();

        } else if (LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate())).isBefore(LocalDate.now())) {

            JOptionPane.showMessageDialog(this, "Entered date must not be earlier than today.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jDateChooser1.grabFocus();

        } else if (jComboBox2.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the Timeslot.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox2.grabFocus();

        } else {

            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

            if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("08:00:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("08.00 AM - 12.00 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("12:30:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("12.30 PM - 04.30 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else if ((new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()).
                    equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())))
                    && (!LocalTime.parse("17:00:00").isAfter(LocalTime.now()))
                    && jComboBox2.getSelectedItem().equals("05.00 PM - 09.00 PM")) {

                JOptionPane.showMessageDialog(this, "Selected Timeslot has expired.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jComboBox2.grabFocus();

            } else {

                String class_schedule_id = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                String class_no = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                String teacher = String.valueOf(jComboBox1.getSelectedItem());
                String subject = String.valueOf(jComboBox4.getSelectedItem());
                String date = new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate());
                String timeslot = String.valueOf(jComboBox2.getSelectedItem());

                String selected_teacher = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 2));
                String selected_subject = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3));
                String selected_date = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4));
                String selected_timeslot = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5));

                if (teacher.equals(selected_teacher) && subject.equals(selected_subject) && date.equals(selected_date)
                        && timeslot.equals(selected_timeslot)) {

                    JOptionPane.showMessageDialog(this, "Same data in database",
                            "Warning", JOptionPane.WARNING_MESSAGE);

                    jTable1.grabFocus();

                } else {

                    try {

                        ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `class` INNER JOIN `teachers` ON class.teachers_tno=teachers.tno "
                                + "INNER JOIN `subject` ON class.subject_sub_no=subject.sub_no INNER JOIN `class_schedule` "
                                + "ON class.class_no=class_schedule.class_class_no WHERE class_schedule.`date`='" + date + "' "
                                + "AND class_schedule.`timeslot_id`='" + timeslotMap.get(timeslot) + "' "
                                + "AND NOT class_schedule.`class_class_no`='" + class_no + "' AND `status_id`='1'");

                        if (resultSet.next()) {

                            String gender = "";

                            if (resultSet.getString("teachers.gender_id").equals("1")) {
                                gender = "Mr.";
                            } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                                gender = "Miss.";
                            }

                            JOptionPane.showMessageDialog(this, "There is a class already scheduled in the selected timeslot."
                                    + "\nClass No : " + resultSet.getString("class.class_no") + "\nTeacher : "
                                    + gender + " " + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname")
                                    + "\nSubject : " + resultSet.getString("subject.name"),
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            jComboBox2.grabFocus();

                        } else {

                            if (class_schedule_id == null) {

                                JOptionPane.showMessageDialog(this, "Something went wrong. Please select the class again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                                jTable1.grabFocus();

                            } else {

                                int option = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                if (option == JOptionPane.YES_OPTION) {

                                    MySQL.executeIUD("UPDATE `class` SET `teachers_tno`='" + teacherMap.get(teacher) + "',"
                                            + "`subject_sub_no`='" + subjectMap.get(subject) + "' WHERE `class_no`='" + class_no + "'");

                                    MySQL.executeIUD("UPDATE `class_schedule` SET `date`='" + date + "',`timeslot_id`='" + timeslotMap.get(timeslot) + "' "
                                            + "WHERE `id`='" + class_schedule_id + "'");

                                    Reset();
                                    LoadClasses();

                                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "Selected Class(" + class_no + ") Details updated Successfully!");

                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

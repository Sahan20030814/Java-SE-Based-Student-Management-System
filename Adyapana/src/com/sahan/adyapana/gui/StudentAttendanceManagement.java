/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.sahan.adyapana.gui;

import com.sahan.adyapana.model.MySQL;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class StudentAttendanceManagement extends javax.swing.JPanel {

    private static HashMap<String, String> classMap = new HashMap<>();
    private static HashMap<String, String> studentMap = new HashMap<>();

    public StudentAttendanceManagement() {
        initComponents();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, renderer);

        LoadClasses();
        LoadStudents();
        setNIC();
        setJButton1Enable();
        LoadAttendance();
    }

    private void LoadClasses() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `class` INNER JOIN `teachers` "
                    + "ON class.teachers_tno=teachers.tno INNER JOIN `subject` "
                    + "ON class.subject_sub_no=subject.sub_no ORDER BY class.`class_no` ASC");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {

                ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `class_schedule` INNER JOIN `timeslot` "
                        + "ON class_schedule.timeslot_id=timeslot.id "
                        + "WHERE `class_class_no`='" + resultSet.getString("class.class_no") + "' AND "
                        + "`date`='" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "'");

                if (resultSet2.next()) {

                    String gender = "";

                    if (resultSet.getString("teachers.gender_id").equals("1")) {
                        gender = "Mr.";
                    } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                        gender = "Miss.";
                    }

                    vector.add("(" + resultSet.getString("class.class_no") + ") " + gender + " "
                            + resultSet.getString("teachers.fname") + " "
                            + resultSet.getString("teachers.lname") + " / "
                            + resultSet.getString("subject.name") + " (" + resultSet2.getString("timeslot.timeslot") + ")");

                    classMap.put("(" + resultSet.getString("class.class_no") + ") " + gender + " "
                            + resultSet.getString("teachers.fname") + " " + resultSet.getString("teachers.lname")
                            + " / " + resultSet.getString("subject.name")
                            + " (" + resultSet2.getString("timeslot.timeslot") + ")", resultSet.getString("class.class_no"));

                }

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox4.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadStudents() {

        try {

            Vector<String> vector = new Vector<>();

            if (jComboBox4.getSelectedItem().equals("Select")) {
                vector.add("Select");
            } else {

                vector.add("Select");

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `invoice` INNER JOIN `students` "
                        + "ON invoice.students_sno=students.sno INNER JOIN `year` ON invoice.year_id=year.id "
                        + "WHERE invoice.`class_class_no`='" + classMap.get(jComboBox4.getSelectedItem()) + "' AND "
                        + "year.`year`='" + new SimpleDateFormat("yyyy").format(new Date()) + "' AND "
                        + "invoice.`month_id`='" + new SimpleDateFormat("MM").format(new Date()) + "' "
                        + "AND students.`nic` LIKE '" + jTextField1.getText() + "%' AND students.`status_id`='1'");

                while (resultSet.next()) {
                    vector.add(resultSet.getString("students.fname") + " " + resultSet.getString("students.lname")
                            + " / " + resultSet.getString("students.nic"));
                    studentMap.put(resultSet.getString("students.fname") + " " + resultSet.getString("students.lname")
                            + " / " + resultSet.getString("students.nic"), resultSet.getString("students.sno"));
                }

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setNIC() {

        if (jComboBox2.getSelectedItem().equals("Select")) {
            jTextField1.setText("");
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `students` "
                        + "WHERE `sno`='" + studentMap.get(jComboBox2.getSelectedItem()) + "' AND `status_id`='1'");

                if (resultSet.next()) {
                    jTextField1.setText(resultSet.getString("nic"));
                } else {
                    jTextField1.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void setJButton1Enable() {

        try {

            if (jComboBox4.getSelectedItem().equals("Select")) {
                jButton1.setEnabled(false);
            } else {

                if (jTextField1.getText().isEmpty()) {
                    jButton1.setEnabled(false);
                } else {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `invoice` INNER JOIN `students` "
                            + "ON invoice.students_sno=students.sno INNER JOIN `year` ON invoice.year_id=year.id "
                            + "WHERE invoice.`class_class_no`='" + classMap.get(jComboBox4.getSelectedItem()) + "' AND "
                            + "year.`year`='" + new SimpleDateFormat("yyyy").format(new Date()) + "' AND "
                            + "invoice.`month_id`='" + new SimpleDateFormat("MM").format(new Date()) + "' "
                            + "AND students.`nic`='" + jTextField1.getText() + "' AND students.`status_id`='1'");

                    if (resultSet.next()) {
                        jButton1.setEnabled(true);
                    } else {
                        jButton1.setEnabled(false);
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadAttendance() {

        String class_no = "0";

        if (!jComboBox4.getSelectedItem().equals("Select")) {
            class_no = classMap.get(jComboBox4.getSelectedItem());
        }

        String timeslot_id = "";

        if (LocalTime.now().isAfter(LocalTime.parse("06.00 AM", DateTimeFormatter.ofPattern("hh.mm a")))
                && LocalTime.now().isBefore(LocalTime.parse("10.00 AM", DateTimeFormatter.ofPattern("hh.mm a")))) {
            timeslot_id = "1";
        } else if (LocalTime.now().isAfter(LocalTime.parse("10.30 AM", DateTimeFormatter.ofPattern("hh.mm a")))
                && LocalTime.now().isBefore(LocalTime.parse("02.30 PM", DateTimeFormatter.ofPattern("hh.mm a")))) {
            timeslot_id = "2";
        } else if (LocalTime.now().isAfter(LocalTime.parse("03.00 PM", DateTimeFormatter.ofPattern("hh.mm a")))
                && LocalTime.now().isBefore(LocalTime.parse("07.00 PM", DateTimeFormatter.ofPattern("hh.mm a")))) {
            timeslot_id = "3";
        }

        try {

            String query = "SELECT * FROM `attendance` INNER JOIN `students` "
                    + "ON attendance.students_sno=students.sno INNER JOIN `class_schedule` "
                    + "ON attendance.class_schedule_id=class_schedule.id INNER JOIN `timeslot` "
                    + "ON class_schedule.timeslot_id=timeslot.id INNER JOIN `class` "
                    + "ON class_schedule.class_class_no=class.class_no INNER JOIN `teachers` "
                    + "ON class.teachers_tno=teachers.tno INNER JOIN `subject` "
                    + "ON class.subject_sub_no=subject.sub_no WHERE class.`class_no`='" + class_no + "' "
                    + "AND class_schedule.`timeslot_id` LIKE '" + timeslot_id + "%' "
                    + "AND students.`nic` LIKE '" + jTextField1.getText() + "%' ORDER BY ";

            String sort = String.valueOf(jComboBox3.getSelectedItem());

            if (sort == "Attendance ID ASC") {
                query += "attendance.`id` ASC";
            } else if (sort == "Attendance ID DESC") {
                query += "attendance.`id` DESC";
            } else if (sort == "Student NIC ASC") {
                query += "students.`nic` ASC";
            } else if (sort == "Student NIC DESC") {
                query += "students.`nic` DESC";
            } else if (sort == "Class ASC") {
                query += "teachers.`fname` ASC";
            } else if (sort == "Class DESC") {
                query += "teachers.`fname` DESC";
            } else if (sort == "Timeslot ASC") {
                query += "class_schedule.`timeslot_id` ASC";
            } else if (sort == "Timeslot DESC") {
                query += "class_schedule.`timeslot_id` DESC";
            } else if (sort == "Marked Time ASC") {
                query += "attendance.`dateTime` ASC";
            } else if (sort == "Marked Time DESC") {
                query += "attendance.`dateTime` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {

                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("attendance.id"));
                vector.add(resultSet.getString("students.nic"));

                String gender = "";

                if (resultSet.getString("teachers.gender_id").equals("1")) {
                    gender = "Mr.";
                } else if (resultSet.getString("teachers.gender_id").equals("2")) {
                    gender = "Miss.";
                }

                vector.add(gender + " " + resultSet.getString("teachers.fname") + " "
                        + resultSet.getString("teachers.lname") + " / " + resultSet.getString("subject.name"));

                vector.add(resultSet.getString("timeslot.timeslot"));
                vector.add(resultSet.getString("attendance.dateTime"));

                model.addRow(vector);
            }

            jTable1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Reset() {
        jTextField1.setText("");
        jComboBox2.setSelectedIndex(0);
        jButton1.setEnabled(false);
        jTable1.clearSelection();
    }

    private void Reset2() {
        jComboBox4.setSelectedIndex(0);
        jTextField1.setText("");
        jComboBox2.setSelectedIndex(0);
        jButton1.setEnabled(false);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1110, 563));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Class :");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Student NIC :");

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Student Attendance Management");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        jComboBox2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jComboBox4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
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

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(51, 153, 0));
        jButton1.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Mark");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1058, Short.MAX_VALUE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox4, 0, 260, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)))
                .addGap(26, 26, 26))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addGap(10, 10, 10)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox4)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );

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
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Attendance ID ASC", "Attendance ID DESC", "Student NIC ASC", "Student NIC DESC", "Class ASC", "Class DESC", "Timeslot ASC", "Timeslot DESC", "Marked Time ASC", "Marked Time DESC" }));
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
                "Attendance ID", "Student NIC", "Class", "Timeslot", "Marked Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1058, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addGap(26, 26, 26))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
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
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked


    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (jTable1.getRowCount() > 0) {

            try {

                InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_students_attendance.jasper");

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

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged

        LoadStudents();
        setJButton1Enable();
        LoadAttendance();

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        LoadStudents();
        setJButton1Enable();
        LoadAttendance();

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

        setNIC();
        setJButton1Enable();
        LoadAttendance();

    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jComboBox4.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select the class first.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox4.grabFocus();

        } else if (jTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter the Student's NIC first.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `students` "
                        + "WHERE `nic`='" + jTextField1.getText() + "' AND `status_id`='1'");

                if (resultSet.next()) {

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `invoice` INNER JOIN `students` "
                            + "ON invoice.students_sno=students.sno INNER JOIN `year` ON invoice.year_id=year.id "
                            + "WHERE invoice.`class_class_no`='" + classMap.get(jComboBox4.getSelectedItem()) + "' AND "
                            + "year.`year`='" + new SimpleDateFormat("yyyy").format(new Date()) + "' AND "
                            + "invoice.`month_id`='" + new SimpleDateFormat("MM").format(new Date()) + "' "
                            + "AND students.`nic`='" + jTextField1.getText() + "' AND students.`status_id`='1'");

                    if (resultSet2.next()) {

                        String sno = resultSet2.getString("students.sno");
                        String fname = resultSet2.getString("students.fname");
                        String lname = resultSet2.getString("students.lname");
                        String nic = resultSet2.getString("students.nic");
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                        String class_no = classMap.get(jComboBox4.getSelectedItem());

                        String timeslot_id = "";

                        if (LocalTime.now().isAfter(LocalTime.parse("06.00 AM", DateTimeFormatter.ofPattern("hh.mm a")))
                                && LocalTime.now().isBefore(LocalTime.parse("10.00 AM", DateTimeFormatter.ofPattern("hh.mm a")))) {
                            timeslot_id = "1";
                        } else if (LocalTime.now().isAfter(LocalTime.parse("10.30 AM", DateTimeFormatter.ofPattern("hh.mm a")))
                                && LocalTime.now().isBefore(LocalTime.parse("02.30 PM", DateTimeFormatter.ofPattern("hh.mm a")))) {
                            timeslot_id = "2";
                        } else if (LocalTime.now().isAfter(LocalTime.parse("03.00 PM", DateTimeFormatter.ofPattern("hh.mm a")))
                                && LocalTime.now().isBefore(LocalTime.parse("07.00 PM", DateTimeFormatter.ofPattern("hh.mm a")))) {
                            timeslot_id = "3";
                        }

                        if (timeslot_id == null) {
                            JOptionPane.showMessageDialog(this, "Attendance mark time has not come yet. \nPlease try again after 30 minutes.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
                        } else {

                            ResultSet resultSet3 = MySQL.executeSearch("SELECT * FROM `class_schedule` "
                                    + "WHERE `date`='" + date + "' AND `class_class_no`='" + class_no + "' AND `timeslot_id`='" + timeslot_id + "'");

                            if (resultSet3.next()) {

                                String class_schedule_id = resultSet3.getString("id");

                                if (class_schedule_id == null) {

                                    JOptionPane.showMessageDialog(this, "Something went wrong.",
                                            "Error", JOptionPane.ERROR_MESSAGE);

                                } else {

                                    ResultSet resultSet4 = MySQL.executeSearch("SELECT * FROM `attendance` "
                                            + "WHERE `students_sno`='" + sno + "' AND `class_schedule_id`='" + class_schedule_id + "'");

                                    if (resultSet4.next()) {

                                        JOptionPane.showMessageDialog(this, "Entered Student's attendance has been already marked.",
                                                "Info", JOptionPane.WARNING_MESSAGE);

                                        jTextField1.getText();

                                    } else {

                                        int option = JOptionPane.showConfirmDialog(this, "Entered Student is correct? "
                                                + "\nStudent Name : " + fname + " " + lname + "\nStudent NIC : " + nic, "Confirm",
                                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                        if (option == JOptionPane.YES_OPTION) {

                                            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                                            MySQL.executeIUD("INSERT INTO `attendance` (`students_sno`,`class_schedule_id`,`dateTime`) "
                                                    + "VALUES('" + sno + "','" + class_schedule_id + "','" + dateTime + "')");

                                            Reset();
                                            LoadStudents();
                                            setNIC();
                                            setJButton1Enable();
                                            LoadAttendance();

                                            Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "Attendance marked Successfully!");

                                        }
                                    }

                                }

                            } else {

                                JOptionPane.showMessageDialog(this, "Can't mark. The selected class is not being held at this time.",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                                jComboBox4.grabFocus();

                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(this, "The enrolled student has not made a class payment for this month.",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        jTextField1.grabFocus();

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

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Reset2();
        LoadStudents();
        setNIC();
        setJButton1Enable();
        LoadAttendance();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        LoadAttendance();

    }//GEN-LAST:event_jComboBox3ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}

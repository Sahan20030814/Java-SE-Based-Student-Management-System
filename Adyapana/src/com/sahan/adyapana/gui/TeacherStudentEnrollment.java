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

/**
 *
 * @author Sahan Liyanage
 */
public class TeacherStudentEnrollment extends javax.swing.JPanel {

    private static HashMap<String, String> teacherMap = new HashMap<>();
    private static HashMap<String, String> subjectMap = new HashMap<>();
    private static HashMap<String, String> timeslotMap = new HashMap<>();

    public TeacherStudentEnrollment() {
        initComponents();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, renderer);

        LoadTeachers();
        LoadSubjects();
        LoadDates();
        LoadTimeslots();
        LoadEnrollement();
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
                    vector.add(resultSet.getString("subject.name"));
                    subjectMap.put(resultSet.getString("subject.name"), resultSet.getString("subject.sub_no"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
        jComboBox5.setModel(model);

    }

    private void LoadDates() {

        Vector<String> vector = new Vector<>();

        if (jComboBox4.getSelectedItem().equals("Select") || jComboBox5.getSelectedItem().equals("Select")) {
            vector.add("Select");
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `class` INNER JOIN `class_schedule` "
                        + "ON class.class_no=class_schedule.class_class_no "
                        + "WHERE class.`teachers_tno`='" + teacherMap.get(jComboBox4.getSelectedItem()) + "' AND "
                        + "class.`subject_sub_no`='" + subjectMap.get(jComboBox5.getSelectedItem()) + "' AND "
                        + "class_schedule.`date`<='" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' "
                        + "ORDER BY class_schedule.`date` DESC");

                vector.add("Select");

                while (resultSet.next()) {

                    if (!vector.contains(resultSet.getString("class_schedule.date"))) {
                        vector.add(resultSet.getString("class_schedule.date"));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
        jComboBox1.setModel(model);

    }

    private void LoadTimeslots() {

        Vector<String> vector = new Vector<>();

        if (jComboBox4.getSelectedItem().equals("Select")
                || jComboBox5.getSelectedItem().equals("Select") || jComboBox1.getSelectedItem().equals("Select")) {

            vector.add("Select");
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `class` INNER JOIN `class_schedule` "
                        + "ON class.class_no=class_schedule.class_class_no INNER JOIN `timeslot` ON class_schedule.timeslot_id=timeslot.id "
                        + "WHERE class.`teachers_tno`='" + teacherMap.get(jComboBox4.getSelectedItem()) + "' AND "
                        + "class.`subject_sub_no`='" + subjectMap.get(jComboBox5.getSelectedItem()) + "' AND "
                        + "class_schedule.`date`='" + String.valueOf(jComboBox1.getSelectedItem()) + "' "
                        + "ORDER BY timeslot.`timeslot` ASC");

                vector.add("Select");

                while (resultSet.next()) {
                    vector.add(resultSet.getString("timeslot.timeslot"));
                    timeslotMap.put(resultSet.getString("timeslot.timeslot"), resultSet.getString("timeslot.id"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
        jComboBox2.setModel(model);

    }

    private void LoadEnrollement() {

        if (!(jComboBox4.getSelectedItem().equals("Select")
                || jComboBox5.getSelectedItem().equals("Select") || jComboBox1.getSelectedItem().equals("Select")
                || jComboBox2.getSelectedItem().equals("Select"))) {

            try {

                String tno = "0";
                String sub_no = "0";
                String date = "";
                String timeslot_id = "0";
                String year = "";
                String month_id = "";

                if (!jComboBox4.getSelectedItem().equals("Select")) {
                    tno = teacherMap.get(jComboBox4.getSelectedItem());
                }
                if (!jComboBox5.getSelectedItem().equals("Select")) {
                    sub_no = subjectMap.get(jComboBox5.getSelectedItem());
                }

                if (!jComboBox1.getSelectedItem().equals("Select")) {
                    date = String.valueOf(jComboBox1.getSelectedItem());
                    year = String.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).getYear());
                    month_id = String.valueOf(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).getMonthValue());
                }

                if (!jComboBox2.getSelectedItem().equals("Select")) {
                    timeslot_id = timeslotMap.get(jComboBox2.getSelectedItem());
                }

                String query = "SELECT * FROM `invoice` INNER JOIN `students` ON "
                        + "invoice.students_sno=students.sno INNER JOIN `year` ON invoice.year_id=year.id INNER JOIN `class` ON "
                        + "invoice.class_class_no=class.class_no INNER JOIN `class_schedule` "
                        + "ON class.class_no=class_schedule.class_class_no INNER JOIN `timeslot` "
                        + "ON class_schedule.timeslot_id=timeslot.id WHERE class.`teachers_tno`='" + tno + "' AND "
                        + "class.`subject_sub_no`='" + sub_no + "' AND class_schedule.`date`='" + date + "' AND "
                        + "class_schedule.`timeslot_id`='" + timeslot_id + "' AND year.`year`='" + year + "' "
                        + "AND invoice.`month_id`='" + month_id + "' ORDER BY ";

                String sort = String.valueOf(jComboBox3.getSelectedItem());

                if (sort == "Enroll ID ASC") {
                    query += "invoice.`id` ASC";
                } else if (sort == "Enroll ID DESC") {
                    query += "invoice.`id` DESC";
                } else if (sort == "Student NIC ASC") {
                    query += "students.`nic` ASC";
                } else if (sort == "Student NIC DESC") {
                    query += "students.`nic` DESC";
                } else if (sort == "First Name ASC") {
                    query += "students.`fname` ASC";
                } else if (sort == "First Name DESC") {
                    query += "students.`fname` DESC";
                } else if (sort == "Last Name ASC") {
                    query += "students.`lname` ASC";
                } else if (sort == "Last Name DESC") {
                    query += "students.`lname` DESC";
                } else if (sort == "Enroll Time ASC") {
                    query += "invoice.`dateTime` ASC";
                } else if (sort == "Enroll Time DESC") {
                    query += "invoice.`dateTime` DESC";
                }

                ResultSet resultSet = MySQL.executeSearch(query);

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {
                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet.getString("invoice.id"));
                    vector.add(resultSet.getString("students.nic"));
                    vector.add(resultSet.getString("students.fname"));
                    vector.add(resultSet.getString("students.lname"));
                    vector.add(resultSet.getString("invoice.dateTime"));

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `attendance` "
                            + "WHERE `students_sno`='" + resultSet.getString("students.sno") + "' AND "
                            + "`class_schedule_id`='" + resultSet.getString("class_schedule.id") + "'");

                    if (resultSet2.next()) {
                        vector.add("Present");
                    } else {
                        vector.add("Absent");
                    }

                    model.addRow(vector);
                }

                jTable1.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            Vector<String> vector = new Vector<>();
            vector.add("");
            vector.add("");
            vector.add("");
            vector.add("");
            vector.add("");

            model.addRow(vector);
            jTable1.setModel(model);
        }

    }

    private void Reset() {
        jComboBox4.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jComboBox1.setSelectedIndex(0);
        jComboBox2.setSelectedIndex(0);
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
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton5 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1110, 563));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("Teacher");

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("Subject");

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Timeslot");

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Teacher & Student Enrollment");

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

        jComboBox5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox5ItemStateChanged(evt);
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

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setText("Date");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
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
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2)
                            .addComponent(jComboBox4)
                            .addComponent(jComboBox5)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Enroll ID ASC", "Enroll ID DESC", "Student NIC ASC", "Student NIC DESC", "First Name ASC", "First Name DESC", "Last Name ASC", "Last Name DESC", "Enroll Time ASC", "Enroll Time DESC" }));
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
                "Enroll ID", "Student NIC", "First Name", "Last Name", "Enroll Time", "Attendance Status"
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
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

            if (String.valueOf(jTable1.getValueAt(0, 0)).isEmpty()) {

                JOptionPane.showMessageDialog(this, "No data in the table.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTable1.grabFocus();

            } else {

                try {

                    InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_enrollment.jasper");

                    HashMap<String, Object> param = new HashMap<>();

                    param.put("Parameter1", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    param.put("Parameter2", new SimpleDateFormat("hh:mm:ss a").format(new Date()));

                    JRTableModelDataSource dataSources = new JRTableModelDataSource(jTable1.getModel());

                    JasperPrint report = JasperFillManager.fillReport(s, param, dataSources);

                    JasperViewer.viewReport(report, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } else {

            JOptionPane.showMessageDialog(this, "No data in the table.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTable1.grabFocus();

        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged

        LoadSubjects();
        LoadDates();
        LoadTimeslots();
        LoadEnrollement();

    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox5ItemStateChanged

        LoadDates();
        LoadTimeslots();
        LoadEnrollement();

    }//GEN-LAST:event_jComboBox5ItemStateChanged

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged

        LoadEnrollement();

    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Reset();
        LoadSubjects();
        LoadDates();
        LoadTimeslots();
        LoadEnrollement();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        LoadTimeslots();
        LoadEnrollement();

    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        LoadEnrollement();

    }//GEN-LAST:event_jComboBox3ItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.sahan.adyapana.gui;

import com.sahan.adyapana.model.MySQL;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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
public class StudentRegistration extends javax.swing.JPanel {

    private static HashMap<String, String> genderMap = new HashMap<>();

    String filepath = null;
    String filename = null;
    String fileExtension = null;

    public StudentRegistration() {
        initComponents();

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.setDefaultRenderer(Object.class, renderer);

        LoadGender();
        LoadStudents();
    }

    private void LoadGender() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `gender`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                genderMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void LoadStudents() {

        if (!jButton4.isEnabled()) {

            try {

                String query = "SELECT * FROM `students` INNER JOIN `gender` ON students.gender_id = gender.id INNER JOIN `status` ON "
                        + "students.status_id=status.id WHERE students.`nic` LIKE '" + jTextField1.getText() + "%' ORDER BY ";

                String sort = String.valueOf(jComboBox3.getSelectedItem());

                if (sort == "S_No ASC") {
                    query += "students.`sno` ASC";
                } else if (sort == "S_No DESC") {
                    query += "students.`sno` DESC";
                } else if (sort == "NIC ASC") {
                    query += "students.`nic` ASC";
                } else if (sort == "NIC DESC") {
                    query += "students.`nic` DESC";
                } else if (sort == "First Name ASC") {
                    query += "students.`fname` ASC";
                } else if (sort == "First Name DESC") {
                    query += "students.`fname` DESC";
                } else if (sort == "Last Name ASC") {
                    query += "students.`lname` ASC";
                } else if (sort == "Last Name DESC") {
                    query += "students.`lname` DESC";
                } else if (sort == "Contact Number ASC") {
                    query += "students.`contact_no` ASC";
                } else if (sort == "Contact Number DESC") {
                    query += "students.`contact_no` DESC";
                } else if (sort == "Birthday ASC") {
                    query += "students.`dob` ASC";
                } else if (sort == "Birthday DESC") {
                    query += "students.`dob` DESC";
                } else if (sort == "Gender ASC") {
                    query += "gender.`name` ASC";
                } else if (sort == "Gender DESC") {
                    query += "gender.`name` DESC";
                } else if (sort == "Password ASC") {
                    query += "students.`password` ASC";
                } else if (sort == "Password DESC") {
                    query += "students.`password` DESC";
                } else if (sort == "Email Address ASC") {
                    query += "students.`email` ASC";
                } else if (sort == "Email Address DESC") {
                    query += "students.`email` DESC";
                } else if (sort == "Registered Date ASC") {
                    query += "students.`registered_date` ASC";
                } else if (sort == "Registered Date DESC") {
                    query += "students.`registered_date` DESC";
                } else if (sort == "Status ASC") {
                    query += "status.`name` ASC";
                } else if (sort == "Status DESC") {
                    query += "status.`name` DESC";
                }

                ResultSet resultSet = MySQL.executeSearch(query);

                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                model.setRowCount(0);

                while (resultSet.next()) {

                    Vector<String> vector = new Vector<>();
                    vector.add(resultSet.getString("students.sno"));
                    vector.add(resultSet.getString("students.nic"));
                    vector.add(resultSet.getString("students.fname"));
                    vector.add(resultSet.getString("students.lname"));
                    vector.add(resultSet.getString("students.contact_no"));
                    vector.add(resultSet.getString("students.dob"));
                    vector.add(resultSet.getString("gender.name"));
                    vector.add(resultSet.getString("students.password"));
                    vector.add(resultSet.getString("students.email"));
                    vector.add(resultSet.getString("students.registered_date"));
                    vector.add(resultSet.getString("status.name"));

                    model.addRow(vector);

                }

                jTable1.setModel(model);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void Reset() {

        this.filepath = null;
        this.filename = null;
        this.fileExtension = null;

        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jDateChooser1.setDate(null);
        jComboBox1.setSelectedIndex(0);
        jPasswordField1.setText("");
        jTextField5.setText("");
        jTable1.clearSelection();

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/man.jpg")));
        jButton3.setEnabled(true);
        jButton4.setEnabled(false);
        jButton6.setEnabled(false);
        jButton6.setText("Status");
        jButton6.setBackground(Color.WHITE);
        jButton6.setForeground(Color.BLACK);

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
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

        setPreferredSize(new java.awt.Dimension(1226, 639));

        jPanel4.setPreferredSize(new java.awt.Dimension(1226, 639));

        jLabel3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel3.setText("NIC Number");

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setToolTipText("Type employee's email Address");
        jTextField1.setMaximumSize(new java.awt.Dimension(64, 34));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel4.setText("First Name");

        jTextField2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setToolTipText("Type employee's first name");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel5.setText("Last Name");

        jTextField3.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setToolTipText("Type employee's last name");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel6.setText("Contact Number");

        jTextField4.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setToolTipText("Type employee's nic number");
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel7.setText("Birthday");

        jComboBox1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jComboBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel2.setText("Gender");

        jTextField5.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setToolTipText("Type employee's mobile number");
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel9.setText("Password");

        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jLabel10.setText("Email Address");

        jPasswordField1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jPasswordField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPasswordField1.setToolTipText("Type the password");
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Poppins", 0, 18)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/hide.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/man.jpg"))); // NOI18N
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 153, 0));
        jButton3.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Create Account");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 153, 0));
        jButton4.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Update Account");
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

        jButton6.setFont(new java.awt.Font("Poppins SemiBold", 0, 20)); // NOI18N
        jButton6.setText("Status");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "S_No", "NIC", "First Name", "Last Name", "Contact Number", "Birthday", "Gender", "Password", "Email Address", "Registered Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
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

        jLabel11.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 102));
        jLabel11.setText("Sort By :");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jComboBox3.setFont(new java.awt.Font("Poppins", 0, 16)); // NOI18N
        jComboBox3.setForeground(new java.awt.Color(255, 0, 102));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "S_No ASC", "S_No DESC", "NIC ASC", "NIC DESC", "First Name ASC", "First Name DESC", "Last Name ASC", "Last Name DESC", "Contact Number ASC", "Contact Number DESC", "Birthday ASC", "Birthday DESC", "Gender ASC", "Gender DESC", "Password ASC", "Password DESC", "Email Address ASC", "Email Address DESC", "Registered Date ASC", "Registered Date DESC", "Status ASC", "Status DESC" }));
        jComboBox3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

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

        jLabel1.setFont(new java.awt.Font("Poppins", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Student Registration");

        jSeparator2.setForeground(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPasswordField1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField4)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(65, 65, 65)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jTextField5))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2))
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

        LoadStudents();

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed

    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed

    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed

    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed

    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed

    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (jPasswordField1.getEchoChar() == '\u2022') {
            jPasswordField1.setEchoChar('\u0000');
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/show.png")));
        } else if (jPasswordField1.getEchoChar() == '\u0000') {
            jPasswordField1.setEchoChar('\u2022');
            jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/hide.png")));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        if (jTable1.getSelectedRow() != -1) {

            int row = jTable1.getSelectedRow();

            try {

                jTextField1.setText(String.valueOf(jTable1.getValueAt(row, 1)));
                jTextField2.setText(String.valueOf(jTable1.getValueAt(row, 2)));
                jTextField3.setText(String.valueOf(jTable1.getValueAt(row, 3)));
                jTextField4.setText(String.valueOf(jTable1.getValueAt(row, 4)));

                jDateChooser1.setDate(new SimpleDateFormat("yyyy-MM-dd").
                        parse(String.valueOf(jTable1.getValueAt(row, 5))));

                jComboBox1.setSelectedItem(jTable1.getValueAt(row, 6));
                jPasswordField1.setText(String.valueOf(jTable1.getValueAt(row, 7)));
                jTextField5.setText(String.valueOf(jTable1.getValueAt(row, 8)));

                jButton6.setEnabled(true);

                if (String.valueOf(jTable1.getValueAt(row, 10)).equals("Active")) {
                    jButton6.setText("Active");
                    jButton6.setBackground(new Color(51, 204, 0));
                    jButton6.setForeground(Color.WHITE);
                } else if (String.valueOf(jTable1.getValueAt(row, 10)).equals("Inactive")) {
                    jButton6.setText("Inactive");
                    jButton6.setBackground(Color.red);
                    jButton6.setForeground(Color.WHITE);
                }

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `students` "
                        + "WHERE `sno`='" + String.valueOf(jTable1.getValueAt(row, 0)) + "'");

                if (resultSet.next()) {

                    filename = resultSet.getString("image_path");
                    filepath = "Institute_Images/Student_Images/" + filename;

                    if (new File(filepath).exists()) {

                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(jButton2.getWidth(), jButton2.getHeight(), Image.SCALE_SMOOTH));
                        jButton2.setIcon(imageIcon);

                    } else {

                        this.filepath = null;
                        this.filename = null;
                        this.fileExtension = null;

                        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sahan/adyapana/recources/man.jpg")));
                    }

                }

                jButton3.setEnabled(false);
                jButton4.setEnabled(true);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (evt.getClickCount() == 2) {

                new AddressView(null, true, String.valueOf(jTable1.getValueAt(row, 2)),
                        String.valueOf(jTable1.getValueAt(row, 3)),
                        String.valueOf(jTable1.getValueAt(row, 1)), "3").setVisible(true);

            }

        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        if (jTable1.getRowCount() > 0) {

            try {

                InputStream s = this.getClass().getResourceAsStream("/com/sahan/adyapana/reports/ai_students.jasper");

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

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged

        LoadStudents();

    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (jTextField1.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's NIC Number.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField1.getText().length() > 14) {

            JOptionPane.showMessageDialog(this, "NIC Number must contain less than 14 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField1.grabFocus();

        } else if (jTextField2.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's First Name.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (jTextField2.getText().length() > 20) {

            JOptionPane.showMessageDialog(this, "First Name must contain less than 20 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField2.grabFocus();

        } else if (jTextField3.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's Last Name.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField3.grabFocus();

        } else if (jTextField3.getText().length() > 20) {

            JOptionPane.showMessageDialog(this, "Last Name must contain less than 20 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField3.grabFocus();

        } else if (jTextField4.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's Mobile Number.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField4.grabFocus();

        } else if (!jTextField4.getText().matches("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$")) {

            JOptionPane.showMessageDialog(this, "Invalid Contact Number!",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTextField4.grabFocus();

        } else if (jDateChooser1.getDate() == null) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's Birthday.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jDateChooser1.grabFocus();

        } else if (jComboBox1.getSelectedItem().equals("Select")) {

            JOptionPane.showMessageDialog(this, "Please Select Student's Gender.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jComboBox1.grabFocus();

        } else if (String.valueOf(jPasswordField1.getPassword()).isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter a Password.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jPasswordField1.grabFocus();

        } else if (!String.valueOf(jPasswordField1.getPassword()).matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])"
                + "[A-Za-z\\d@$!%*#?&]{8,}$") | String.valueOf(jPasswordField1.getPassword()).length() < 8
                | String.valueOf(jPasswordField1.getPassword()).length() > 20) {

            JOptionPane.showMessageDialog(this, "Password must be contained "
                    + "minimum 8 characters and maximum 20 characters, at least one letter, one "
                    + "number and one special character.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jPasswordField1.grabFocus();

        } else if (jTextField5.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please Enter Student's Email Address.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField5.grabFocus();

        } else if (!jTextField5.getText().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {

            JOptionPane.showMessageDialog(this, "Invalid Email Address!",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTextField5.grabFocus();

        } else if (jTextField5.getText().length() > 50) {

            JOptionPane.showMessageDialog(this, "Email Address must contain less than 50 characters.",
                    "Warning", JOptionPane.WARNING_MESSAGE);

            jTextField5.grabFocus();

        } else if (filepath == null || filename == null) {

            JOptionPane.showMessageDialog(this, "Please add a student image.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
            jButton2.grabFocus();

        } else if (!new File(filepath).exists()) {

            JOptionPane.showMessageDialog(this, "Image Uploading Error! \nPlease select the student image again.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jButton2.grabFocus();

        } else {

            String nic = jTextField1.getText();
            String firstName = jTextField2.getText();
            String lastName = jTextField3.getText();
            String contact_no = jTextField4.getText();
            String birthday = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()));
            String gender = String.valueOf(jComboBox1.getSelectedItem());
            String password = String.valueOf(jPasswordField1.getPassword());
            String email = jTextField5.getText();

            try {

                ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `students` WHERE `nic`='" + nic + "'");

                if (resultSet1.next()) {

                    JOptionPane.showMessageDialog(this, "This NIC Number already registered. Please Check NIC Number.",
                            "Error", JOptionPane.ERROR_MESSAGE);

                    jTextField1.grabFocus();

                } else {

                    ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `students` WHERE `email`='" + email + "'");

                    if (resultSet2.next()) {

                        JOptionPane.showMessageDialog(this, "This Email Address already registered. "
                                + "\nPlease Change Email Address and Try again.",
                                "Error", JOptionPane.ERROR_MESSAGE);

                        jTextField5.grabFocus();

                    } else {

                        String reg_date = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

                        if (!new File("Institute_Images/Student_Images").exists()) {
                            new File("Institute_Images/Student_Images").mkdirs();
                        }

                        // Create Path objects
                        Path sourcePath = Paths.get(filepath);
                        String copy_file_path = "Institute_Images/Student_Images/" + filename;
                        Path destinationPath = Paths.get(copy_file_path);

                        // Ensure the destination directory exists
                        File destinationDir = new File("Institute_Images/Student_Images");
                        if (!destinationDir.exists()) {
                            destinationDir.mkdirs(); // Create directories if not exist
                        }

                        // Copy the file to the destination
                        Files.copy(sourcePath, destinationPath);

                        ResultSet resultSet3 = MySQL.executeSearch("SELECT COUNT(`sno`) AS 'c' FROM `students`");

                        if (resultSet3.next()) {

                            int count = resultSet3.getInt("c");

                            count += 1;

                            filename = String.valueOf(count) + String.valueOf(10000 + new Random().nextInt(90000)) + "." + fileExtension;

                            // File renamed
                            if (new File(copy_file_path).renameTo(new File("Institute_Images/Student_Images/" + filename))) {

                                MySQL.executeIUD("INSERT INTO `students` (`fname`,`lname`,`email`,`dob`,`nic`,`contact_no`,`password`,"
                                        + "`registered_date`,`gender_id`,`status_id`,`image_path`) VALUES('" + firstName + "','" + lastName + "','" + email + "',"
                                        + "'" + birthday + "','" + nic + "','" + contact_no + "','" + password + "','" + reg_date + "','" + genderMap.get(gender) + "',"
                                        + "'1','" + filename + "')");

                                Reset();
                                LoadStudents();

                                Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "New Student registered Successfully!");

                            } else {
                                JOptionPane.showMessageDialog(this, "Student Image uploading fail. \nPlease try again!",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Something went wrong!",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);

        File f = chooser.getSelectedFile();

        if (f != null) {
            filepath = f.getAbsolutePath();

            fileExtension = filepath.substring(filepath.lastIndexOf(".") + 1).toLowerCase();

            if (fileExtension.equals("jpg") || fileExtension.equals("png") || fileExtension.equals("jpeg")) {

                ImageIcon imageIcon = new ImageIcon(new ImageIcon(filepath).getImage().getScaledInstance(jButton2.getWidth(), jButton2.getHeight(), Image.SCALE_SMOOTH));

                jButton2.setIcon(imageIcon);

                filepath = filepath.replace("\\", "/");
                filename = f.getName();

            } else {
                JOptionPane.showMessageDialog(this, "Invalid File Format! \n Please select a JPG, PNG, or JPEG image.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        Reset();
        LoadStudents();

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (jTable1.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(this, "Please Select a student to update.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTable1.grabFocus();

        } else {

            if (jTextField1.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's NIC Number.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField1.grabFocus();

            } else if (jTextField1.getText().length() > 14) {

                JOptionPane.showMessageDialog(this, "NIC Number must contain less than 14 characters.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField1.grabFocus();

            } else if (jTextField2.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's First Name.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField2.grabFocus();

            } else if (jTextField2.getText().length() > 20) {

                JOptionPane.showMessageDialog(this, "First Name must contain less than 20 characters.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField2.grabFocus();

            } else if (jTextField3.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's Last Name.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField3.grabFocus();

            } else if (jTextField3.getText().length() > 20) {

                JOptionPane.showMessageDialog(this, "Last Name must contain less than 20 characters.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField3.grabFocus();

            } else if (jTextField4.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's Mobile Number.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField4.grabFocus();

            } else if (!jTextField4.getText().matches("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$")) {

                JOptionPane.showMessageDialog(this, "Invalid Contact Number!",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jTextField4.grabFocus();

            } else if (jDateChooser1.getDate() == null) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's Birthday.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jDateChooser1.grabFocus();

            } else if (jComboBox1.getSelectedItem().equals("Select")) {

                JOptionPane.showMessageDialog(this, "Please Select Student's Gender.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jComboBox1.grabFocus();

            } else if (String.valueOf(jPasswordField1.getPassword()).isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter a Password.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jPasswordField1.grabFocus();

            } else if (!String.valueOf(jPasswordField1.getPassword()).matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])"
                    + "[A-Za-z\\d@$!%*#?&]{8,}$") | String.valueOf(jPasswordField1.getPassword()).length() < 8
                    | String.valueOf(jPasswordField1.getPassword()).length() > 20) {

                JOptionPane.showMessageDialog(this, "Password must be contained "
                        + "minimum 8 characters and maximum 20 characters, at least one letter, one "
                        + "number and one special character.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jPasswordField1.grabFocus();

            } else if (jTextField5.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Please Enter Student's Email Address.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField5.grabFocus();

            } else if (!jTextField5.getText().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {

                JOptionPane.showMessageDialog(this, "Invalid Email Address!",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jTextField5.grabFocus();

            } else if (jTextField5.getText().length() > 50) {

                JOptionPane.showMessageDialog(this, "Email Address must contain less than 50 characters.",
                        "Warning", JOptionPane.WARNING_MESSAGE);

                jTextField5.grabFocus();

            } else if (filepath == null || filename == null) {

                JOptionPane.showMessageDialog(this, "Please add a student image.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                jButton2.grabFocus();

            } else if (!new File(filepath).exists()) {

                JOptionPane.showMessageDialog(this, "Image Uploading Error! \nPlease select the student image again.",
                        "Error", JOptionPane.ERROR_MESSAGE);

                jButton2.grabFocus();

            } else {

                String sno = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                String nic = jTextField1.getText();
                String firstName = jTextField2.getText();
                String lastName = jTextField3.getText();
                String contact_no = jTextField4.getText();
                String birthday = String.valueOf(new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser1.getDate()));
                String gender = String.valueOf(jComboBox1.getSelectedItem());
                String password = String.valueOf(jPasswordField1.getPassword());
                String email = jTextField5.getText();

                String selected_nic = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 1));
                String selected_firstName = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 2));
                String selected_lastName = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 3));
                String selected_contact_no = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 4));
                String selected_birthday = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 5));
                String selected_gender = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 6));
                String selected_password = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 7));
                String selected_email = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 8));

                if (nic.equals(selected_nic) && firstName.equals(selected_firstName) && lastName.equals(selected_lastName)
                        && contact_no.equals(selected_contact_no) && birthday.equals(selected_birthday)
                        && gender.equals(selected_gender) && password.equals(selected_password)
                        && email.equals(selected_email) && filepath.equals("Institute_Images/Student_Images/" + filename)) {

                    JOptionPane.showMessageDialog(this, "Same data in database.",
                            "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    try {

                        ResultSet resultSet1 = MySQL.executeSearch("SELECT * FROM `students` WHERE `nic`='" + nic + "' AND NOT `sno`='" + sno + "'");

                        if (resultSet1.next()) {

                            JOptionPane.showMessageDialog(this, "This NIC Number already registered. Please Check NIC Number.",
                                    "Error", JOptionPane.ERROR_MESSAGE);

                            jTextField1.grabFocus();

                        } else {

                            ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `students` WHERE `email`='" + email + "' AND NOT `sno`='" + sno + "'");

                            if (resultSet2.next()) {

                                JOptionPane.showMessageDialog(this, "This Email Address already registered. "
                                        + "\nPlease Change Email Address and Try again.",
                                        "Error", JOptionPane.ERROR_MESSAGE);

                                jTextField5.grabFocus();

                            } else {

                                int option = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm",
                                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                                if (option == JOptionPane.YES_OPTION) {

                                    boolean canUpdate1 = false;
                                    boolean canUpdate2 = false;

                                    if (!filepath.equals("Institute_Images/Student_Images/" + filename)) {

                                        if (!new File("Institute_Images/Student_Images").exists()) {
                                            new File("Institute_Images/Student_Images").mkdirs();
                                        }

                                        // Create Path objects
                                        Path sourcePath = Paths.get(filepath);
                                        String copy_file_path = "Institute_Images/Student_Images/" + filename;
                                        Path destinationPath = Paths.get(copy_file_path);

                                        // Ensure the destination directory exists
                                        File destinationDir = new File("Institute_Images/Student_Images");
                                        if (!destinationDir.exists()) {
                                            destinationDir.mkdirs(); // Create directories if not exist
                                        }

                                        // Copy the file to the destination
                                        Files.copy(sourcePath, destinationPath);

                                        ResultSet resultSet3 = MySQL.executeSearch("SELECT COUNT(`sno`) AS 'c' FROM `students`");

                                        if (resultSet3.next()) {

                                            int count = resultSet3.getInt("c");

                                            count += 1;

                                            filename = String.valueOf(count) + String.valueOf(10000 + new Random().nextInt(90000)) + "." + fileExtension;

                                            // File renamed
                                            if (new File(copy_file_path).renameTo(new File("Institute_Images/Student_Images/" + filename))) {

                                                ResultSet rs = MySQL.executeSearch("SELECT * FROM `students` WHERE `sno`='" + sno + "'");

                                                if (rs.next()) {

                                                    if (new File("Institute_Images/Student_Images/" + rs.getString("image_path")).exists()) {

                                                        new File("Institute_Images/Student_Images/" + rs.getString("image_path")).delete();

                                                    }

                                                }

                                                canUpdate2 = true;

                                            } else {
                                                canUpdate2 = false;
                                            }

                                            canUpdate1 = true;

                                        } else {
                                            canUpdate1 = false;
                                        }

                                    } else {
                                        canUpdate1 = true;
                                        canUpdate2 = true;
                                    }

                                    if (!canUpdate1) {

                                        JOptionPane.showMessageDialog(this, "Someting went wrong.", "Error", JOptionPane.ERROR_MESSAGE);

                                    } else if (!canUpdate2) {

                                        JOptionPane.showMessageDialog(this, "Student Image uploading fail. \nPlease try again!",
                                                "Error", JOptionPane.ERROR_MESSAGE);

                                    } else {

                                        MySQL.executeIUD("UPDATE `students` SET `fname`='" + firstName + "',`lname`='" + lastName + "',`email`='" + email + "',"
                                                + "`dob`='" + birthday + "',`nic`='" + nic + "',`contact_no`='" + contact_no + "',`password`='" + password + "',"
                                                + "`gender_id`='" + genderMap.get(gender) + "',`image_path`='" + filename + "' "
                                                + "WHERE `sno`='" + sno + "'");

                                        Reset();
                                        LoadStudents();

                                        Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "Student Details updated Successfully!");

                                    }

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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (jTable1.getSelectedRow() == -1) {

            JOptionPane.showMessageDialog(this, "Please Select a student to update.",
                    "Error", JOptionPane.ERROR_MESSAGE);

            jTable1.grabFocus();

        } else {

            int option = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {

                String sno = String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                String status_id = "";

                if (String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 10)).equals("Active")) {
                    status_id = "2";
                } else if (String.valueOf(jTable1.getValueAt(jTable1.getSelectedRow(), 10)).equals("Inactive")) {
                    status_id = "1";
                }

                try {

                    MySQL.executeIUD("UPDATE `students` SET `status_id`='" + status_id + "' WHERE `sno`='" + sno + "'");

                    Reset();
                    LoadStudents();

                    Notifications.getInstance().show(Notifications.Type.SUCCESS, Notifications.Location.TOP_CENTER, 2500L, "Student's status updated Successfully!");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    // End of variables declaration//GEN-END:variables
}

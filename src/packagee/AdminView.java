package packagee;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author jjlora
 * @author edangulo
 */
public class AdminView extends javax.swing.JFrame implements Observer {

    private int x, y;
    private String adminIdStr;
    private DoctorController doctorController;

    public AdminView(String adminIdStr) {
        initComponents();
        this.adminIdStr = adminIdStr;
        this.doctorController = new DoctorController();
        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);

        Database.getInstance().attach(this);
        update();
    }

    // Legacy constructor for compatibility
    public AdminView(User user, ArrayList<User> users, ArrayList<Hospitalization> hospitalizations, ArrayList<Appointment> appointments) {
        this(user != null ? String.valueOf(user.getId()) : "0");
    }

    @Override
    public void update() {
        cbDoctorSelect.removeAllItems();
        cbDoctorSelect.addItem("Select one");
        cbPatientSelect.removeAllItems();
        cbPatientSelect.addItem("Select one");

        for (User u : Database.getInstance().getUsers()) {
            if (u instanceof Doctor) {
                cbDoctorSelect.addItem(String.valueOf(u.getId()));
            } else if (u instanceof Patient) {
                cbPatientSelect.addItem(String.valueOf(u.getId()));
            }
        }

        // Populate Specialties
        if (cbDocSpecialty.getItemCount() <= 1) {
            cbDocSpecialty.removeAllItems();
            cbDocSpecialty.addItem("Select one");
            for (Specialty s : Specialty.values()) {
                cbDocSpecialty.addItem(s.toString());
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new packagee.PanelRound();
        panelRound2 = new packagee.PanelRound();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        panelRound3 = new packagee.PanelRound();
        btnDoctorView = new javax.swing.JButton();
        btnPatientView = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtDocFirstname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDocLastname = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtDocId = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtDocLicense = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDocOffice = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtDocUser = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtDocPass = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtDocPassConfirm = new javax.swing.JTextField();
        cbDocSpecialty = new javax.swing.JComboBox<>();
        btnRegisterDoctor = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        cbDoctorSelect = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cbPatientSelect = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setRadius(50);

        panelRound2.setRadius(50);
        panelRound2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelRound2MouseDragged(evt);
            }
        });
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRound2MousePressed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnExit.setText("X");
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnExit.setFocusable(false);
        btnExit.setRequestFocusEnabled(false);
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N
        jLabel1.setText("ADMIN VIEW");

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addGap(19, 19, 19))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnExit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1))
        );

        btnDoctorView.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnDoctorView.setText("DOCTOR VIEW");
        btnDoctorView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnPatientView.setFont(new java.awt.Font("Yu Gothic UI", 1, 18)); // NOI18N
        btnPatientView.setText("PATIENT VIEW");
        btnPatientView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel4.setText("Firstname");

        txtDocFirstname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel5.setText("Lastname");

        txtDocLastname.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel6.setText("ID");

        txtDocId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel7.setText("Specialty");

        jLabel8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel8.setText("License");

        txtDocLicense.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel9.setText("Office");

        txtDocOffice.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel10.setText("User");

        txtDocUser.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel11.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel11.setText("Password");

        txtDocPass.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        jLabel12.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel12.setText("Password confirmation");

        txtDocPassConfirm.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        cbDocSpecialty.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbDocSpecialty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        btnRegisterDoctor.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRegisterDoctor.setText("Save");
        btnRegisterDoctor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        cbDoctorSelect.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbDoctorSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        jLabel13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel13.setText("Doctor ID");

        jLabel14.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        jLabel14.setText("Patient ID");

        cbPatientSelect.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbPatientSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select one" }));

        btnLogout.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(txtDocFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(txtDocLastname, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(txtDocId, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(cbDocSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 83, Short.MAX_VALUE))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtDocLicense, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtDocOffice, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(450, 450, 450))))
            .addComponent(jSeparator1)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(450, 450, 450)
                        .addComponent(jLabel11))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(434, 434, 434)
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDocUser, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDocPass, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(473, 473, 473)
                        .addComponent(jLabel10))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(432, 432, 432)
                        .addComponent(txtDocPassConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(456, 456, 456)
                        .addComponent(btnRegisterDoctor))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(396, 396, 396)
                        .addComponent(jLabel12))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(cbDoctorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(btnDoctorView)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(38, 38, 38))
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(203, 203, 203)
                .addComponent(jLabel14)
                .addGap(18, 18, 18)
                .addComponent(cbPatientSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnPatientView)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRound3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jSeparator2)
                    .addContainerGap()))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtDocFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtDocLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtDocId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cbDocSpecialty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtDocLicense, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtDocOffice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtDocUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(txtDocPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addGap(18, 18, 18)
                .addComponent(txtDocPassConfirm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(btnRegisterDoctor)
                .addGap(42, 42, 42)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(cbDoctorSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDoctorView)))
                    .addGroup(panelRound3Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(btnLogout)))
                .addGap(25, 25, 25)
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cbPatientSelect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPatientView))
                .addGap(38, 38, 38))
            .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelRound3Layout.createSequentialGroup()
                    .addGap(397, 397, 397)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(203, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout panelRound1Layout = new javax.swing.GroupLayout(panelRound1);
        panelRound1.setLayout(panelRound1Layout);
        panelRound1Layout.setHorizontalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelRound1Layout.setVerticalGroup(
            panelRound1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound1Layout.createSequentialGroup()
                .addComponent(panelRound2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void panelRound2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void panelRound2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        Response response = doctorController.registerDoctor(
            txtDocFirstname.getText(),
            txtDocLastname.getText(),
            txtDocId.getText(),
            (String) cbDocSpecialty.getSelectedItem(),
            txtDocLicense.getText(),
            txtDocOffice.getText(),
            txtDocUser.getText(),
            txtDocPass.getText(),
            txtDocPassConfirm.getText()
        );

        if (response.getStatusCode() == 201) {
            JOptionPane.showMessageDialog(this, response.getMessage());
            txtDocFirstname.setText("");
            txtDocLastname.setText("");
            txtDocId.setText("");
            txtDocLicense.setText("");
            txtDocOffice.setText("");
            txtDocUser.setText("");
            txtDocPass.setText("");
            txtDocPassConfirm.setText("");
            cbDocSpecialty.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String selectedDoctorId = (String) cbDoctorSelect.getSelectedItem();
        if (selectedDoctorId == null || "Select one".equals(selectedDoctorId)) {
            JOptionPane.showMessageDialog(this, "Please select a doctor", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.setVisible(false);
        DoctorView doctorView = new DoctorView(selectedDoctorId, true, adminIdStr);
        doctorView.setVisible(true);
    }

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {                                          
        this.setVisible(false);
        LoginView login = new LoginView();
        login.setVisible(true);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        String selectedPatientId = (String) cbPatientSelect.getSelectedItem();
        if (selectedPatientId == null || "Select one".equals(selectedPatientId)) {
            JOptionPane.showMessageDialog(this, "Please select a patient", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.setVisible(false);
        PatientView patientView = new PatientView(selectedPatientId, true);
        patientView.adminIdStr = this.adminIdStr;
        patientView.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnDoctorView;
    private javax.swing.JButton btnPatientView;
    private javax.swing.JButton btnRegisterDoctor;
    private javax.swing.JComboBox<String> cbDocSpecialty;
    private javax.swing.JComboBox<String> cbDoctorSelect;
    private javax.swing.JComboBox<String> cbPatientSelect;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField txtDocPassConfirm;
    private javax.swing.JTextField txtDocFirstname;
    private javax.swing.JTextField txtDocLastname;
    private javax.swing.JTextField txtDocId;
    private javax.swing.JTextField txtDocLicense;
    private javax.swing.JTextField txtDocOffice;
    private javax.swing.JTextField txtDocUser;
    private javax.swing.JTextField txtDocPass;
    private packagee.PanelRound panelRound1;
    private packagee.PanelRound panelRound2;
    private packagee.PanelRound panelRound3;
    // End of variables declaration//GEN-END:variables
}

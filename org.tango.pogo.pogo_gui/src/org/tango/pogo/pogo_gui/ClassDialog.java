//+======================================================================
//
// Project:   Tango
//
// Description:  Basic Dialog Class to edit device class.
//
// $Author: verdier $
//
// Copyright (C) :      2004,2005,2006,2007,2008,2009,2009,2010,2011,2012,2013,2014
//						European Synchrotron Radiation Facility
//                      BP 220, Grenoble 38043
//                      FRANCE
//
// This file is part of Tango.
//
// Tango is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// Tango is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Tango.  If not, see <http://www.gnu.org/licenses/>.
//
// $Revision: $
// $Date:  $
//
// $HeadURL: $
//
//-======================================================================


package org.tango.pogo.pogo_gui;

import fr.esrf.tango.pogo.pogoDsl.ClassDescription;
import fr.esrf.tango.pogo.pogoDsl.PogoDeviceClass;
import fr.esrf.tangoatk.widget.util.ATKGraphicsUtils;
import org.tango.pogo.pogo_gui.tools.PogoException;
import org.tango.pogo.pogo_gui.tools.PogoFileFilter;
import org.tango.pogo.pogo_gui.tools.PogoProperty;
import org.tango.pogo.pogo_gui.tools.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.StringTokenizer;

//===============================================================

/**
 * A Dialog Class to get the Class definitions.
 */
//===============================================================
@SuppressWarnings("MagicConstant")
public class ClassDialog extends JDialog {

    private static int returnStatus;
    private DeviceClass deviceClass;
    private String origClassName = null;
    private InheritancePanel inheritancePanel;
    private JRadioButton[] langBtn;
    private boolean hasForcedGenerate = false;
    static private JFileChooser chooser = null;


    /**
     * It is kept as JDialog to be edited with netbeans,
     * but only centerPanel and data nmanagement are used
     */
    private DeviceIdDialog IDdialog;

    /**
     * Get ClassTree instance to do save in case of class name changed
     */
    private ClassTree classTree;

    //===================================================================
    /**
     * Initializes the ClassDialog object
     *
     * @param parent           The parent frame object
     * @param classTree        instance to do save in case of class name changed
     * @param deviceClass               The device class object to be edited
     * @param isInheritedClass true if this class is an inherited one
     */
    //===================================================================
    public ClassDialog(JFrame parent, ClassTree classTree, DeviceClass deviceClass, boolean isInheritedClass) {
        super(parent, true);
        this.classTree = classTree;
        initComponents();
        langBtn = new JRadioButton[4];
        langBtn[PogoConst.Cpp] = cppBtn;
        langBtn[PogoConst.Java] = javaBtn;
        langBtn[PogoConst.Python] = pythonBtn;
        langBtn[PogoConst.PythonHL] = pythonHLBtn;

        licenseComboBox.addItem("GPL");
        licenseComboBox.addItem("LGPL");
        licenseComboBox.addItem("APACHE");
        licenseComboBox.addItem("MIT");
        licenseComboBox.addItem("none");

        if (deviceClass == null) {  //  Creating a new class
            this.deviceClass = new DeviceClass("", null);
            revisionLabel.setVisible(false);
        }
        else {
            //	Edit the specified class
            this.deviceClass = deviceClass;
            origClassName = deviceClass.getPogoDeviceClass().getName();
            //  remove the add inheritance class button
            addInheritanceBtn.setVisible(false);
            ClassDescription desc = deviceClass.getPogoDeviceClass().getDescription();
            if (desc != null)
                setLanguage(desc.getLanguage());
            String  revision = deviceClass.getPogoDeviceClass().getPogoRevision();
            if (revision==null) {
                revisionLabel.setVisible(false);
            }
            else {
                revisionLabel.setText("Code has been generated by:   Pogo " + revision + ".x");
            }
        }

        //	Fill fields with data if any
        PogoDeviceClass pogo_class = this.deviceClass.getPogoDeviceClass();
        nameText.setText(pogo_class.getName());
        descText.setText(pogo_class.getDescription().getDescription());
        descText.setToolTipText(Utils.buildToolTip("Class Description",
                "Description for device server documentation."));
        if (deviceClass==null)  //  New class --> then default from property
            copyrightText.setText(PogoProperty.copyright);
        else
            copyrightText.setText(pogo_class.getDescription().getCopyright());
        copyrightText.setToolTipText(Utils.buildToolTip("Copyright",
                "Copyright for the current class."));
        projectText.setText(pogo_class.getDescription().getTitle());
        projectText.setToolTipText(Utils.buildToolTip(
                "Short description for documentation header"));

        IDdialog = new DeviceIdDialog(parent, pogo_class.getDescription().getIdentification());
        horizontalPanel.setLeftComponent(IDdialog.getCenterPanel());

        //	Build a panel to display inheritance
        if (this.deviceClass.getPogoDeviceClass().getName().length() == 0)
            this.deviceClass.getPogoDeviceClass().setName("New Tango Class");
        inheritancePanel = new InheritancePanel(this.deviceClass);
        inheritanceScrollPane.setViewportView(inheritancePanel);

        if (isInheritedClass)
            nameText.setEditable(false);

        String  license = pogo_class.getDescription().getLicense();
        if (license!=null)
            licenseComboBox.setSelectedItem(license);
        licenseComboBox.setToolTipText( Utils.buildToolTip(
                "WARNING:",
                "The license will be written in header file\n" +
                "The header file will be generated in protected area.\n" +
                "That means, it will NOT be overwritten in case of change !!!!"));

        pack();
        ATKGraphicsUtils.centerDialog(this);
        nameText.requestFocus();
    }
    //===================================================================
    /**
     * Initializes the ClassDialog object
     *
     * @param parent The parent frame object
     */
    //===================================================================
    public ClassDialog(JFrame parent) {
        this(parent, null, null, false);
    }

    //===================================================================
    //===================================================================
    private void setLanguage(String lang) {
        int langCode = Utils.getLanguage(lang);
        if (langCode < 0)
            langCode = PogoConst.Cpp;
        for (int i = 0; i < langBtn.length; i++) {
            langBtn[i].setSelected(i == langCode);
        }
    }

    //===================================================================
    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    //===================================================================
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        javax.swing.JPanel bottomPanel = new javax.swing.JPanel();
        javax.swing.JLabel dummyLabel = new javax.swing.JLabel();
        javax.swing.JButton okBtn = new javax.swing.JButton();
        javax.swing.JButton cancelBtn = new javax.swing.JButton();
        horizontalPanel = new javax.swing.JSplitPane();
        javax.swing.JPanel centerPanel = new javax.swing.JPanel();
        addInheritanceBtn = new javax.swing.JButton();
        javax.swing.JLabel nameLbl = new javax.swing.JLabel();
        nameText = new javax.swing.JTextField();
        javax.swing.JLabel titleLbl = new javax.swing.JLabel();
        projectText = new javax.swing.JTextField();
        javax.swing.JLabel descLbl = new javax.swing.JLabel();
        javax.swing.JScrollPane descScrollPane = new javax.swing.JScrollPane();
        descText = new javax.swing.JTextArea();
        javax.swing.JLabel languageLbl = new javax.swing.JLabel();
        javax.swing.JPanel languagePanel = new javax.swing.JPanel();
        cppBtn = new javax.swing.JRadioButton();
        javaBtn = new javax.swing.JRadioButton();
        pythonBtn = new javax.swing.JRadioButton();
        pythonHLBtn = new javax.swing.JRadioButton();
        javax.swing.JLabel licenseLbl = new javax.swing.JLabel();
        licenseComboBox = new javax.swing.JComboBox<>();
        javax.swing.JLabel copyrightLbl = new javax.swing.JLabel();
        javax.swing.JScrollPane copyrightScrollPane = new javax.swing.JScrollPane();
        copyrightText = new javax.swing.JTextArea();
        javax.swing.JButton editButton = new javax.swing.JButton();
        revisionLabel = new javax.swing.JLabel();
        inheritanceScrollPane = new javax.swing.JScrollPane();

        setTitle("Class Definition Window");
        setBackground(new java.awt.Color(198, 178, 168));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        bottomPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        dummyLabel.setText("                                 ");
        bottomPanel.add(dummyLabel);

        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(okBtn);

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        bottomPanel.add(cancelBtn);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        centerPanel.setLayout(new java.awt.GridBagLayout());

        addInheritanceBtn.setText("Add Inheritance Class");
        addInheritanceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInheritanceBtnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 5);
        centerPanel.add(addInheritanceBtn, gridBagConstraints);

        nameLbl.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        nameLbl.setText("Class Name :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        centerPanel.add(nameLbl, gridBagConstraints);

        nameText.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        nameText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextActionPerformed(evt);
            }
        });
        nameText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameTextKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        centerPanel.add(nameText, gridBagConstraints);

        titleLbl.setText("Project Title :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        centerPanel.add(titleLbl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
        centerPanel.add(projectText, gridBagConstraints);

        descLbl.setText("Class Description:  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 0);
        centerPanel.add(descLbl, gridBagConstraints);

        descScrollPane.setPreferredSize(new java.awt.Dimension(500, 250));

        descText.setColumns(80);
        descText.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        descText.setMinimumSize(new java.awt.Dimension(0, 100));
        descText.setPreferredSize(new java.awt.Dimension(880, 400));
        descScrollPane.setViewportView(descText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
        centerPanel.add(descScrollPane, gridBagConstraints);

        languageLbl.setText("Language :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 3, 10, 0);
        centerPanel.add(languageLbl, gridBagConstraints);

        cppBtn.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        cppBtn.setSelected(true);
        cppBtn.setText("Cpp");
        cppBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageActionPerformed(evt);
            }
        });
        languagePanel.add(cppBtn);

        javaBtn.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        javaBtn.setText("Java");
        javaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageActionPerformed(evt);
            }
        });
        languagePanel.add(javaBtn);

        pythonBtn.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        pythonBtn.setText("Python");
        pythonBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageActionPerformed(evt);
            }
        });
        languagePanel.add(pythonBtn);

        pythonHLBtn.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        pythonHLBtn.setText("PythonHL");
        pythonHLBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                languageActionPerformed(evt);
            }
        });
        languagePanel.add(pythonHLBtn);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 10, 0);
        centerPanel.add(languagePanel, gridBagConstraints);

        licenseLbl.setText("License :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 10, 0);
        centerPanel.add(licenseLbl, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        centerPanel.add(licenseComboBox, gridBagConstraints);

        copyrightLbl.setText("Class Copyright:  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 0);
        centerPanel.add(copyrightLbl, gridBagConstraints);

        copyrightScrollPane.setPreferredSize(new java.awt.Dimension(400, 100));

        copyrightText.setColumns(40);
        copyrightText.setFont(new java.awt.Font("Monospaced", 1, 12)); // NOI18N
        copyrightText.setRows(5);
        copyrightScrollPane.setViewportView(copyrightText);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        centerPanel.add(copyrightScrollPane, gridBagConstraints);

        editButton.setText("...");
        editButton.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        centerPanel.add(editButton, gridBagConstraints);

        revisionLabel.setText("pogo-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 0);
        centerPanel.add(revisionLabel, gridBagConstraints);

        horizontalPanel.setRightComponent(centerPanel);

        getContentPane().add(horizontalPanel, java.awt.BorderLayout.CENTER);

        inheritanceScrollPane.setPreferredSize(new java.awt.Dimension(230, 250));
        getContentPane().add(inheritanceScrollPane, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    //===================================================================
    //===================================================================
    private void manageOK() {
        String classname = checkClassName();
        if (classname == null ||
                classname.length() == 0)
            return;
        try {
            IDdialog.checkInputs();
        } catch (PogoException e) {
            e.popup(this);
            return;
        }
        nameText.setText(classname);
        //  Check if class name has changed
        if (checkClassNameChanged(classname))
            doClose(JOptionPane.OK_OPTION);
    }

    //===================================================================
    //===================================================================
    private boolean checkClassNameChanged(String className) {
        //  Check if it has changed
        if (classTree == null)        //  it is a new one
            return true;
        if (origClassName == null)    //  it is a new one
            return true;
        if (deviceClass.getPogoDeviceClass().getDescription().getSourcePath() == null)
            return true;    //  Not already saved
        if (className.equals(origClassName))    //   no change
            return true;


        //  Ask to choose.
        Object[] options = {"Change Class name",
                "Create new class files",
                "Cancel"};
        int choice = JOptionPane.showOptionDialog(this,
                "Class name has changed",
                "Confirmation Window",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        switch (choice) {
            case 0:
                System.out.println("Will generate");
                return manageClassNameChanged(className);
            case 1:
                return true;
        }
        return false;
    }

    //===================================================================
    //===================================================================
    private boolean manageClassNameChanged(String className) {
        if (JOptionPane.showConfirmDialog(this,
                "The " + className + " files (xmi and code) will be generated",
                "Confirmation Window",
                JOptionPane.YES_NO_OPTION) != JOptionPane.OK_OPTION)
            return false;

        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Utils.getInstance().startSplashRefresher(
                "Generate class: " + className);

        try {
            DeviceClass deviceClass = classTree.getDeviceClass();
            deviceClass.generateWithNewName(className, classTree.getModified(),
                    classTree.getDeletedObjects(), classTree.getRenamedObjects());

            classTree.setModified(false);
            hasForcedGenerate = true;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            Utils.getInstance().stopSplashRefresher();
        } catch (Exception e) {
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            Utils.getInstance().stopSplashRefresher();
            PogoException.popup(this, e);
            return false;
        }
        return true;
    }

    //===================================================================
    //===================================================================
    public boolean hasForcedToGenerate() {
        return hasForcedGenerate;
    }

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void nameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextActionPerformed

        manageOK();
    }//GEN-LAST:event_nameTextActionPerformed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed

        manageOK();
    }//GEN-LAST:event_okBtnActionPerformed


    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        doClose(JOptionPane.CANCEL_OPTION);
    }//GEN-LAST:event_cancelBtnActionPerformed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(JOptionPane.CANCEL_OPTION);
    }//GEN-LAST:event_closeDialog

    //===================================================================
    //===================================================================
    private void languageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_languageActionPerformed

        JRadioButton btn = (JRadioButton) evt.getSource();

        //	If action performed to reset -> force it
        if (btn.getSelectedObjects() == null)
            btn.setSelected(true);

        //  Check if inheritance -> cannot have another language.
        if (deviceClass.getAncestors().size() > 0) {
            btn.setSelected(false);
            JOptionPane.showMessageDialog(this,
                    deviceClass.getPogoDeviceClass().getName() + " inherits  for " +
                            deviceClass.getAncestors().get(0).getPogoDeviceClass().getName() +
                            ".\n It must be generated in same language !",
                    "Error Window",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            //	Check the language
            setLanguage(btn.getText());
        }
    }//GEN-LAST:event_languageActionPerformed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void addInheritanceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInheritanceBtnActionPerformed


        //	Initialize chooser if not already done.
        if (chooser == null) {
            String path = System.getenv("SOURCE_PATH");
            if (path == null) {
                path = System.getProperty("SOURCE_PATH");
                if (path == null)
                    path = new File("").getAbsolutePath();
            }
            chooser = new JFileChooser(new File(path).getAbsolutePath());

            PogoFileFilter filter = new PogoFileFilter("xmi", "Tango Classes");
            filter.setExtensionListInDescription(false);
            chooser.setFileFilter(filter);
        }

        //	Start the file chooser
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (file != null) {
                if (!file.isDirectory()) {
                    try {
                        //	Try to load class
                        DeviceClass dc = new DeviceClass(file.getAbsolutePath());
                        deviceClass.addAncestor(dc);

                        //	Then Remove old inheritance panel
                        inheritanceScrollPane.remove(inheritancePanel);

                        //  Check if Class name has been typed
                        String name = nameText.getText();
                        if (name.length() > 0)
                            deviceClass.getPogoDeviceClass().setName(name);

                        //  Then build a new panel and display
                        inheritancePanel = new InheritancePanel(deviceClass);
                        inheritanceScrollPane.setViewportView(inheritancePanel);
                        addInheritanceBtn.setVisible(false);

                        //  Set the language as inherited one
                        setLanguage(dc.getPogoDeviceClass().getDescription().getLanguage());

                    } catch (PogoException e) {
                        if (!e.toString().equals("CANCEL"))
                            e.popup(this);
                    }
                }
            }
        }

    }//GEN-LAST:event_addInheritanceBtnActionPerformed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void nameTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameTextKeyPressed
        if (evt.getKeyCode() == 27) {  // Escape
            doClose(JOptionPane.CANCEL_OPTION);
        }
    }//GEN-LAST:event_nameTextKeyPressed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO  Edit in dialog.
        EditDialog editDialog = new EditDialog(this, descText.getText(), new Dimension(640, 480));
        if (editDialog.showDialog() == JOptionPane.OK_OPTION) {
            //	Put new text in text area
            descText.setText(editDialog.getText());
        }
    }//GEN-LAST:event_editButtonActionPerformed

    //===================================================================
    //===================================================================
    @SuppressWarnings({"UnusedDeclaration"})
    //===================================================================
    //===================================================================
    public int showDialog() {
        setVisible(true);
        return returnStatus;
    }

    //===========================================================
    /**
     * Close the dialog and set the return status.
     *
     * @param retStatus value to b used to set the return status.
     */
    //===========================================================
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    //===========================================================
    //===========================================================
    DeviceClass getInputs() {
        PogoDeviceClass pogo_class = deviceClass.getPogoDeviceClass();
        pogo_class.setName(nameText.getText());
        String title = Utils.strReplaceSpecialCharToCode(projectText.getText());
        pogo_class.getDescription().setTitle(title);
        String desc = Utils.strReplaceSpecialCharToCode(descText.getText());
        pogo_class.getDescription().setDescription(desc);
        String copyright = Utils.strReplaceSpecialCharToCode(copyrightText.getText());
        pogo_class.getDescription().setCopyright(copyright);
        pogo_class.getDescription().setIdentification(IDdialog.getInputs());
        pogo_class.getDescription().setLicense(licenseComboBox.getSelectedItem().toString());
        if (pythonHLBtn.getSelectedObjects() != null)
            pogo_class.getDescription().setLanguage(PogoConst.strLang[PogoConst.PythonHL]);
        else if (pythonBtn.getSelectedObjects() != null)
            pogo_class.getDescription().setLanguage(PogoConst.strLang[PogoConst.Python]);
        else if (javaBtn.getSelectedObjects() != null)
            pogo_class.getDescription().setLanguage(PogoConst.strLang[PogoConst.Java]);
        else
            pogo_class.getDescription().setLanguage(PogoConst.strLang[PogoConst.Cpp]);
        return deviceClass;
    }
    //===========================================================

    /**
     * Read class name text field.
     *
     * @return the String read in class name text field.
     */
    //===========================================================
    private String checkClassName() {
        if (nameText.getText().length() == 0)
            return null;

        //  Check if char are OK
        String name = nameText.getText().toLowerCase();
        if (name.charAt(0) < 'a' || name.charAt(0) > 'z') {
            Utils.popupError(this, "First char of class name must be a letter");
            return null;
        }
        for (int i = 0; i < name.length(); i++) {
            if ((name.charAt(i) < 'a' || name.charAt(i) > 'z') &&
                    (name.charAt(i) < '0' || name.charAt(i) > '9') &&
                    name.charAt(i) != '_') {
                Utils.popupError(this, "Char \'" + name.charAt(i) + "\' is not authorized in class name");
                return null;
            }
        }

        //	Take off space char if exist
        //--------------------------------------
        StringTokenizer stk = new StringTokenizer(nameText.getText());
        name = "";
        while (stk.hasMoreTokens()) {
            String tmp = stk.nextToken();
            //	Check if first char is upcase else set it
            if (tmp.length() > 1)
                name += tmp.substring(0, 1).toUpperCase() + tmp.substring(1);
            else
                name += tmp.toUpperCase();
        }
        if (name.length() == 0)
            return null;
        else
            return name;
    }

    //===================================================================
    //===================================================================

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addInheritanceBtn;
    private javax.swing.JTextArea copyrightText;
    private javax.swing.JRadioButton cppBtn;
    private javax.swing.JTextArea descText;
    private javax.swing.JSplitPane horizontalPanel;
    private javax.swing.JScrollPane inheritanceScrollPane;
    private javax.swing.JRadioButton javaBtn;
    private javax.swing.JComboBox<String> licenseComboBox;
    private javax.swing.JTextField nameText;
    private javax.swing.JTextField projectText;
    private javax.swing.JRadioButton pythonBtn;
    private javax.swing.JRadioButton pythonHLBtn;
    private javax.swing.JLabel revisionLabel;
    // End of variables declaration//GEN-END:variables
}
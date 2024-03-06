/*
 *  Copyright (C) 2011 Eros Zanchetta <eros@sslmit.unibo.it>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package gui;

import com.formdev.flatlaf.FlatLightLaf;
import core.About;
import core.Converter;
import core.ErrorCode;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * TODO: ripensare un po' al meccanismo per cui un unsupported encoding viene considerato
 * warning, se non ci sono altri file da convertire.
 * 
 * Inoltre quando un file viene erroneamente rilevato come file di testo in una particolare
 * codifica, la conversione fallisce miseramente senza che venga rilevato nessun errore
 * dal programma (il sistema dà uno stacktrace però, riprova a converire la cartella delle
 * ricette dei secondi per far saltare fuori l'errore).
 * 
 * La soluzione potrebbe essere cercare una libreria che identifichi il mimetype e salti
 * a priori i file non di testo binari.
 */

/**
 *
 * @author Eros Zanchetta
 */
public class MainWindow extends javax.swing.JFrame {
    
    private enum ConversionType {
        FILE,
        FOLDER
    }

    private ConversionType  conversionType;
    
    private String  inputFileLabelStringFile    = "The file you want to convert:";
    private String  inputFileLabelStringFolder  = "The folder whose contents you want to convert:";
    private String  outputFileLabelStringFile   = "The name of the converted file:";
    private String  outputFileLabelStringFolder = "<html>The folder where the converted files will be put <span style='color: red; font-weight: bold;'>(folder must be empty)</span>:</html>";
    private String  convertButtonLabel1         = "Convert";
    private String  convertButtonLabel2         = "Converting...";
    
    /** Creates new form MainWindow */
    public MainWindow() {

        FlatLightLaf.setup();        
        

            //         String defaultLookAndFeel = UIManager.getSystemLookAndFeelClassName();
            // String defaultLookAndFeel = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
            
//        String defaultLookAndFeel = flatLightLaf.getName();

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());        
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        initComponents();
        postInit();
        
        // work out current screen resolution and center main window
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int initialXPosition = (dim.width - this.getSize().width) / 2;
        int initialYPosition = (dim.height - this.getSize().height) / 2;
        this.setLocation(initialXPosition, initialYPosition);
        
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        
        verifyIfConversionIsPossible();
    }

    private void resetInputParams() {
        inputTextField.setText("");
        verifyIfConversionIsPossible();
    }

    private void resetOutputParams() {
        outputTextField.setText("");
        verifyIfConversionIsPossible();
    }
        
    private void postInit() {
        conversionType  = ConversionType.FILE;
        
        inputFileLabel.setText(inputFileLabelStringFile);
        outputFileLabel.setText(outputFileLabelStringFile);
        convertButton.setText(convertButtonLabel1);
        
        this.setJMenuBar(mainMenuBar);
    }
    
    private File findSuitableName(File file) {
        File path = new File(file.getParent());
        
        String prefix = "utf8_";
        String tempPrefix = prefix;
        
        int n = 0;
        while (new File(path + File.separator + tempPrefix + file.getName()).isFile()) {
            tempPrefix = prefix + ++n + "_";
        }
                
        return new File(path + File.separator + tempPrefix + file.getName());
    }
    
    private void verifyIfConversionIsPossible() {
        if (!inputTextField.getText().trim().equals("") &
                !outputTextField.getText().trim().equals("")) {
            
            convertButton.setEnabled(true);
        }
        else convertButton.setEnabled(false);
    }
    
    private void convert() {
        convertButton.setText(convertButtonLabel2);
        convertButton.setEnabled(false);
        
        File inFile = new File(inputTextField.getText());
        File outFile = new File(outputTextField.getText());
        
        Converter converter = new Converter();
        
        ErrorCode errorCode = null;
        
        switch (conversionType) {
            case FILE:
                errorCode = converter.convertFile(inFile, outFile, addBomCheckBox.isSelected());
                break;
                
            case FOLDER:
                errorCode = converter.convertDir(inFile, outFile, addBomCheckBox.isSelected());
                break;
        }
        
        boolean ok = errorCode.equals(ErrorCode.OK);
        
        boolean warning = converter.getWarning().size() > 0;
        
        String details = converter.getErrorLog() +
                converter.getWarningLog() +
                converter.getLog();
        
        ConversionDone conversionDone = new ConversionDone(this, true, ok, warning, details);
        
        convertButton.setText(convertButtonLabel1);
        convertButton.setEnabled(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        inputTextField = new javax.swing.JTextField();
        inputSelectorButton = new javax.swing.JButton();
        outputTextField = new javax.swing.JTextField();
        outputSelectorButton = new javax.swing.JButton();
        inputFileLabel = new javax.swing.JLabel();
        convertFileRadioButton = new javax.swing.JRadioButton();
        convertFolderRadioButton = new javax.swing.JRadioButton();
        outputFileLabel = new javax.swing.JLabel();
        convertButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        addBomCheckBox = new javax.swing.JCheckBox();
        mainMenuBar = new javax.swing.JMenuBar();
        helpMenu = new javax.swing.JMenu();
        onlineHelpMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Any2UTF8");

        inputTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputTextFieldActionPerformed(evt);
            }
        });
        inputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                inputTextFieldKeyTyped(evt);
            }
        });

        inputSelectorButton.setText("Browse");
        inputSelectorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputSelectorButtonActionPerformed(evt);
            }
        });

        outputTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                outputTextFieldKeyTyped(evt);
            }
        });

        outputSelectorButton.setText("Browse");
        outputSelectorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputSelectorButtonActionPerformed(evt);
            }
        });

        inputFileLabel.setText("inputFileLabel");

        buttonGroup1.add(convertFileRadioButton);
        convertFileRadioButton.setSelected(true);
        convertFileRadioButton.setText("Convert single file");
        convertFileRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertFileRadioButtonActionPerformed(evt);
            }
        });

        buttonGroup1.add(convertFolderRadioButton);
        convertFolderRadioButton.setText("Convert all files in a folder (and its subfolders)");
        convertFolderRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertFolderRadioButtonActionPerformed(evt);
            }
        });

        outputFileLabel.setText("outputFileLabel");

        convertButton.setText("convertButton");
        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("<html>This tool converts any plain text file (in any encoding) to UTF8</html>");

        addBomCheckBox.setText("Add BOM (not recommended)");
        addBomCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBomCheckBoxActionPerformed(evt);
            }
        });

        helpMenu.setText("Help");

        onlineHelpMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        onlineHelpMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/res/help.png"))); // NOI18N
        onlineHelpMenuItem.setText("Online help");
        onlineHelpMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onlineHelpMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(onlineHelpMenuItem);

        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/res/about.png"))); // NOI18N
        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
                            .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(convertFileRadioButton)
                        .add(18, 18, 18)
                        .add(convertFolderRadioButton)
                        .addContainerGap(77, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jSeparator2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                        .add(26, 26, 26))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(inputTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(inputSelectorButton))
                            .add(inputFileLabel)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, outputFileLabel)
                                    .add(layout.createSequentialGroup()
                                        .add(outputTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                        .add(outputSelectorButton)))
                                .add(3, 3, 3)))
                        .add(29, 29, 29))))
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(addBomCheckBox))
                    .add(layout.createSequentialGroup()
                        .add(228, 228, 228)
                        .add(convertButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(convertFileRadioButton)
                    .add(convertFolderRadioButton))
                .add(18, 18, 18)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(inputFileLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(inputTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(inputSelectorButton))
                .add(18, 18, 18)
                .add(outputFileLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(outputTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(outputSelectorButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(addBomCheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(convertButton)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void convertFileRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertFileRadioButtonActionPerformed
        if (conversionType.equals(conversionType.FILE)) return;
        
        inputFileLabel.setText(inputFileLabelStringFile);
        outputFileLabel.setText(outputFileLabelStringFile);
        conversionType = ConversionType.FILE;
        resetInputParams();
        resetOutputParams();
    }//GEN-LAST:event_convertFileRadioButtonActionPerformed

    private void convertFolderRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertFolderRadioButtonActionPerformed
        if (conversionType.equals(conversionType.FOLDER)) return;
        
        inputFileLabel.setText(inputFileLabelStringFolder);
        outputFileLabel.setText(outputFileLabelStringFolder);
        conversionType = ConversionType.FOLDER;
        resetInputParams();
        resetOutputParams();
    }//GEN-LAST:event_convertFolderRadioButtonActionPerformed

    private void outputSelectorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputSelectorButtonActionPerformed
                
        JFileChooser fc = new JFileChooser();

	fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        fc.setDialogTitle("Folder for converted files");
        fc.showDialog(this, "Choose this folder");
        
        File outputFile = fc.getSelectedFile();
                
        if (outputFile == null) return;
        
        // if converting a single file, try to assign the output filename the same name
        // as the input file name
        File inputFile = new File(inputTextField.getText());
        if (inputFile.isFile())
            outputFile = new File(outputFile.getPath() + File.separator + inputFile.getName());
        
        // if output file has the same path as the input file
        // find a suitable name for it
        if (inputFile.getPath().equals(outputFile.getPath()))
            outputFile = findSuitableName(outputFile);
        
        if (outputFile != null) {
            outputTextField.setText(outputFile.getPath());
        }
        
        verifyIfConversionIsPossible();
    }//GEN-LAST:event_outputSelectorButtonActionPerformed

    private void inputSelectorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputSelectorButtonActionPerformed
        resetOutputParams();
        
        File inputFile = null;

        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);
        
        switch (conversionType) {
            default:
            case FILE:
                fc.setDialogTitle("File to convert");
                fc.setFileFilter(new FileNameExtensionFilter("Text files", "txt", "TXT"));                
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.showDialog(this, "OK");
                break;
            
            case FOLDER:
                fc.setDialogTitle("Folder to convert");
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showDialog(this, "Choose this folder");
                break;
        }


        inputFile = fc.getSelectedFile();

        if (inputFile == null) return;
        
        inputTextField.setText(inputFile.getPath());
        
        // propose a name for output file
        if (inputFile.isFile())
            outputTextField.setText(findSuitableName(inputFile).getPath());
        
        verifyIfConversionIsPossible();
    }//GEN-LAST:event_inputSelectorButtonActionPerformed

    private void convertButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertButtonActionPerformed
        convert();
    }//GEN-LAST:event_convertButtonActionPerformed

    private void onlineHelpMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onlineHelpMenuItemActionPerformed
        Browser.browse(About.getHelpUrl());
//        BareBonesBrowserLaunch.openURL(About.getHelpUrl());
    }//GEN-LAST:event_onlineHelpMenuItemActionPerformed

    private void inputTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputTextFieldActionPerformed

    private void inputTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_inputTextFieldKeyTyped
        verifyIfConversionIsPossible();
    }//GEN-LAST:event_inputTextFieldKeyTyped

    private void outputTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_outputTextFieldKeyTyped
        verifyIfConversionIsPossible();
    }//GEN-LAST:event_outputTextFieldKeyTyped

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        AboutBox aboutBox = new AboutBox(this, true);
        
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int initialXPosition = (dim.width - aboutBox.getSize().width) / 2;
        int initialYPosition = (dim.height - aboutBox.getSize().height) / 2;

        aboutBox.setLocation(initialXPosition, initialYPosition);
        aboutBox.setVisible(true);        
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void addBomCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBomCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addBomCheckBoxActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JCheckBox addBomCheckBox;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton convertButton;
    private javax.swing.JRadioButton convertFileRadioButton;
    private javax.swing.JRadioButton convertFolderRadioButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel inputFileLabel;
    private javax.swing.JButton inputSelectorButton;
    private javax.swing.JTextField inputTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem onlineHelpMenuItem;
    private javax.swing.JLabel outputFileLabel;
    private javax.swing.JButton outputSelectorButton;
    private javax.swing.JTextField outputTextField;
    // End of variables declaration//GEN-END:variables
}

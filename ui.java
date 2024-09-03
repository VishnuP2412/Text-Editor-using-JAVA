import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class ui implements ActionListener, MouseMotionListener
{
    String text;
    JFrame mainframe;
    JMenuBar menubar;
    JMenu File,Edit,Help;
    JMenuItem New,Open,Save,SaveAs,Exit,Cut,Copy,Paste,Mode,fontAttributes,undo,redo;
    JFileChooser fileChooser;
    JTextArea textArea;
    JScrollPane scroll;
    Timer timer;
    Color color;
    UndoManager undoManager=new UndoManager();

    /* TODO:
     *    Add the keyboard shortcuts to the menu items
     *    Add the functionality to the menu items
     *    Add the status bar
     *    Add the functionality to the status bar
     *    Simplify the UI of fontDialog JDialog box (Especially the Font_Family)
     *    Clear the spaghetti code and unnecessary variables
     */


    //contains the mainframe that holds the Text Editor
    ui()
    {
        mainframe=new JFrame();
        mainframe.setTitle("Text Editor");
        mainframe.setSize(720,720);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setIconImage(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Text Editor.png").getImage(),150,150).getImage());
        mainframe.setResizable(true);
        Area(); header();
        mainframe.add(scroll);
        mainframe.setJMenuBar(menubar);
        mainframe.addMouseMotionListener(this);
        timer=new Timer(150,e -> checkMousePosition());
        timer.start();
        mainframe.setVisible(true);
    }

    //Contains the elements for the Header(JMenuBar) of the Text Editor
    void header()
    {
        menubar=new JMenuBar();
        menubar.setPreferredSize(new Dimension(715,30));

        File=new JMenu("File");
        File.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\File.png").getImage(),15,15));
        File.setMnemonic('F');

        Edit=new JMenu("Edit");
        Edit.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Edit.png").getImage(),15,15));
        Edit.setMnemonic('E');

        Help=new JMenu("Help");
        Help.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Help.png").getImage(),15,15));
        Help.setMnemonic('H');

        New=new JMenuItem("New File");
        New.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\New file.png").getImage(),15,15));
        New.addActionListener(this);

        Open=new JMenuItem("Open File");
        Open.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Open file.png").getImage(),15,15));
        Open.addActionListener(this);

        Save=new JMenuItem("Save");
        Save.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Save.png").getImage(),15,15));
        Save.addActionListener(this);

        SaveAs=new JMenuItem("SaveAs");
        SaveAs.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\SaveAs.png").getImage(),15,15));
        SaveAs.addActionListener(this);

        Exit=new JMenuItem("Exit");
        Exit.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Exit.png").getImage(),15,15));
        Exit.addActionListener(this);

        Cut=new JMenuItem("Cut");
        Cut.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Cut.png").getImage(),15,15));
        Cut.addActionListener(this);

        Copy=new JMenuItem("Copy");
        Copy.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Copy.png").getImage(),15,15));
        Copy.addActionListener(this);


        Paste=new JMenuItem("Paste");
        Paste.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Paste.png").getImage(),15,15));
        Paste.addActionListener(this);

        Mode=new JMenuItem("View Mode");
        if(textArea.getBackground()==Color.DARK_GRAY)
            Mode.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Dark_mode.png").getImage(),15,15));
        else
            Mode.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Light_mode.png").getImage(),15,15));
        Mode.addActionListener(this);

        undo=new JMenuItem("Undo");
        undo.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Undo.png").getImage(),15,15));
        undo.addActionListener(this);

        redo=new JMenuItem("Redo");
        redo.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Redo.png").getImage(),15,15));
        redo.addActionListener(this);

        fontAttributes=new JMenuItem("Font Attributes");
        fontAttributes.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Font_Attribute.png").getImage(),15,15));
        fontAttributes.addActionListener(new ActionListener() {

            //Contains the font attributes dialog box
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog fontDialog =new JDialog();
                fontDialog.setTitle("Set the font Attributes");
                fontDialog.setVisible(true);
                //fontDialog.setSize(400,400);
                fontDialog.setLocationRelativeTo(mainframe);
                fontDialog.setLayout(new BorderLayout());

                //Text Color
                textArea.setForeground(Color.BLACK);
                JColorChooser colorPicker=new JColorChooser();
                fontDialog.add(colorPicker,BorderLayout.CENTER);

                //Font Size
                JSpinner Font_Size=new JSpinner(new SpinnerNumberModel(15,1,100,1));
                JPanel fontPanel=new JPanel();
                fontPanel.add(new JLabel("Font Size"));
                fontPanel.add(Font_Size);
                fontDialog.add(fontPanel,BorderLayout.NORTH);

                //Font Family
                JPanel Font_Family=new JPanel();
                Font_Family.add(new JLabel("Font Family"));
                String[] fonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
                JComboBox<String> FontCombo=new JComboBox<String>(fonts);
                Font_Family.add(FontCombo);
                fontDialog.add(Font_Family,BorderLayout.EAST);

                // Apply button
                JButton applyButton = new JButton("Apply");
                applyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        color = colorPicker.getColor();
                        textArea.setForeground(color);
                        textArea.setFont(new Font((String) FontCombo.getSelectedItem(), Font.PLAIN, (int) Font_Size.getValue()));
                        fontDialog.dispose();
                    }
                });
                fontDialog.add(applyButton, BorderLayout.SOUTH);
                fontDialog.pack();
            }
        });


        menubar.add(File);
        menubar.add(Box.createRigidArea(new Dimension(5,0)));
        menubar.add(Edit);
        menubar.add(Box.createRigidArea(new Dimension(3,0)));
        menubar.add(Help);

        File.add(New);
        File.add(Open);
        File.add(Save);
        File.add(SaveAs);
        File.add(Exit);

        Edit.add(Copy);
        Edit.add(Cut);
        Edit.add(Paste);
        Edit.add(fontAttributes);
        Edit.add(undo);
        Edit.add(redo);

        Help.add(Mode);

        menubar.setBackground(Color.WHITE);
    }

    //Used to resize the image
    ImageIcon resize(Image image, int i, int j)
    {
        Image resized=image.getScaledInstance(i,j,java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resized);
    }

    //Contains the text area where the user can write the text
    void Area()
    {
        textArea=new JTextArea();
        textArea.setBounds(0,40,715,640);
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN,15));
        scroll=new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(715,715));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });
    }

    //Contains the functionality of the Text Editor
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==New)
        {
            textArea.setText("");
        }
        else if(e.getSource()==Open)
        {
            fileChooser =new JFileChooser();
            int result = fileChooser.showOpenDialog(mainframe);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    textArea.read(reader, null);
                } catch (IOException ex) {
                    System.out.println("File cannot be opened");
                }
            }
        }
        else if(e.getSource()==Save)
        {
            fileChooser =new JFileChooser();
            fileChooser.showSaveDialog(null);
            if(fileChooser.getSelectedFile()!=null)
            {
                File file = fileChooser.getSelectedFile();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    textArea.write(writer);
                } catch (IOException ex) {
                    System.out.println("File cannot be saved");
                }
            }
            else
            {
                int result = fileChooser.showSaveDialog(mainframe);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        textArea.write(writer);
                    } catch (IOException ex) {
                        System.out.println("File cannot be saved");
                    }
                }
            }
        }
        else if(e.getSource()==SaveAs)
        {
            fileChooser =new JFileChooser();
            fileChooser.showSaveDialog(null);
        }
        else if(e.getSource()==Exit)
            System.exit(0);
        else if(e.getSource()==Cut)
        {
            text=textArea.getSelectedText();
            textArea.replaceRange("",textArea.getSelectionStart(),textArea.getSelectionEnd());
        }
        else if(e.getSource()==Copy)
        {
            text=textArea.getSelectedText();
        }
        else if(e.getSource()==Paste)
        {
            textArea.paste();
        }
        if(e.getSource()==Mode)
        {
            if(textArea.getBackground()==Color.DARK_GRAY)
            {
                Mode.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Dark_mode.png").getImage(),15,15));
                textArea.setBackground(Color.WHITE);
                menubar.setBackground(Color.WHITE);
                textArea.setForeground(Color.BLACK);
            }
            else
            {
                textArea.setBackground(Color.DARK_GRAY);
                menubar.setBackground(Color.DARK_GRAY);
                textArea.setForeground(Color.WHITE);
                Mode.setIcon(resize(new ImageIcon("\\C:\\Users\\vishn\\Downloads\\Icons\\Light_mode.png").getImage(),15,15));
            }
        }
        if(e.getSource()==undo)
        {
            if(undoManager.canUndo())
                undoManager.undo();
        }
        if(e.getSource()==redo)
        {
            if(undoManager.canRedo())
                undoManager.redo();
        }
    }

    //Empty
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    //Used to hide and show the Header
    @Override
    public void mouseMoved(MouseEvent e)
    {
        checkMousePosition();
    }

    //Used to check the mouse position and hide the Header
    void checkMousePosition() {
        Point mousePosition = mainframe.getMousePosition();
        if (mousePosition != null) {
            menubar.setVisible(mousePosition.y < 70);
        }
    }
}
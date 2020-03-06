// Based on TellerApp
// https://github.students.cs.ubc.ca/CPSC210/TellerApp/blob/master/src/main/ca/ubc/cpsc210/bank/ui/TellerApp.java

package ui;

import model.*;
import persistence.Reader;
import persistence.Writer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MenuUI extends JPanel implements ListSelectionListener {
    private static final String SAVE_FILE = "./data/mazeSaveFile.txt";
    private static final String DEFAULT_SAVE_FILE = "./data/defaults/mazeDefaultData.txt";
    private static final String CREATE_STRING = "New Maze";
    private static final String DELETE_STRING = "Delete Maze";
    private static final String START_GAME_STRING = "Start Game";

    private JList list;
    private JButton createButton;
    private JButton deleteButton;
    private JButton startGameButton;
    private JTextField mazeName;
    // TODO: implement chooser for all maze sizes
    // for now, just create xs size mazes

    private MazeListModel mazeList;

    // EFFECTS: runs the app menu UI
    public MenuUI() {
        super(new BorderLayout());

        loadMazes();
        createListUI();
//        runApp();
    }

    // MODIFIES: this
    // EFFECTS: displays menus and processes user input
    private void runApp() {
//        boolean keepGoing = true;
//        String command;
//        mazeListPage = 0;
//        input = new Scanner(System.in);
//
//        loadMazes();
//
//        while (keepGoing) {
//            // Based on https://stackoverflow.com/a/6857936
//            System.out.println(String.join("", Collections.nCopies(25, "-")));
//            displayMazeList(mazeListPage);
//            displayMenu();
//            command = input.next().toLowerCase();
//
//            if (command.equals(QUIT_COMMAND)) {
//                saveMazes();
//                keepGoing = false;
//            } else {
//                processCommand(command);
//            }
//        }


    }

    // MODIFIES: this
    // EFFECTS: loads mazes from file and initializes mazeList
    private void loadMazes() {
        try {
            mazeList = Reader.readMazeList(new File(SAVE_FILE));
        } catch (IOException e) {
            System.out.println("Could not read save file");
            System.out.println("Trying default maze list");
            try {
                mazeList = Reader.readMazeList(new File(DEFAULT_SAVE_FILE));
            } catch (IOException defaultE) {
                System.out.println("Could not read default save file");
                System.out.println("Creating blank maze list");
                mazeList = new MazeListModel();
            }
        }
    }

    // EFFECTS: saves maze list state to SAVE_FILE
    private void saveMazes() {
        try {
            Writer writer = new Writer(new File(SAVE_FILE));
            writer.write(mazeList);
            writer.close();
            System.out.printf("Mazes saved to file %s%n", SAVE_FILE);
        } catch (FileNotFoundException e) {
            System.out.printf("Unable to save mazes to file%s%n. Check if file exists.", SAVE_FILE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates list of mazes and puts it in a scroll pane
    private void createListUI() {
        list = new JList(mazeList);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(8);

        JScrollPane listScrollPane = new JScrollPane(list);
        JPanel controlPane = createControlPane();
        add(listScrollPane, BorderLayout.CENTER);
        add(controlPane, BorderLayout.PAGE_END);
    }

    // EFFECTS: returns JPanel with controls to add/delete mazes and start games
    private JPanel createControlPane() {
        setupControls();

        JPanel controlPane = new JPanel();
        controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.LINE_AXIS));
        controlPane.add(startGameButton);
        addUIControlSeparator(controlPane, 5);
        controlPane.add(mazeName);
        controlPane.add(createButton);
        addUIControlSeparator(controlPane, 5);
        controlPane.add(deleteButton);
        controlPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return controlPane;
    }

    // MODIFIES: this
    // EFFECTS: sets up interface controls
    private void setupControls() {
        // create blank buttons first
        // use blank buttons to set up listeners that need button access
        // finalize button set up now that listeners are made
        createButton = new JButton();
        deleteButton = new JButton();
        startGameButton = new JButton();

        CreateListener createListener = new CreateListener(createButton);
        DeleteListener deleteListener = new DeleteListener();
        StartGameListener startGameListener = new StartGameListener();

        setupButton(createButton, createListener, CREATE_STRING, false);
        setupButton(deleteButton, deleteListener, DELETE_STRING, true);
        setupButton(startGameButton, startGameListener, START_GAME_STRING, false);

        mazeName = new JTextField(10);
        mazeName.addActionListener(createListener);
        mazeName.getDocument().addDocumentListener(createListener);
    }

    // MODIFIES: button
    // EFFECTS: sets up interface button with given string, listener, and enabled state
    private void setupButton(JButton button, ActionListener listener, String label, boolean enabled) {
        button.setText(label);
        button.setActionCommand(label);
        button.addActionListener(listener);
        button.setEnabled(enabled);
    }

    // MODIFIES: panel
    // EFFECTS: creates separator between horizontal ui controls with given spacing
    private void addUIControlSeparator(JPanel panel, int spacing) {
        panel.add(Box.createHorizontalStrut(spacing));
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(Box.createHorizontalStrut(spacing));
    }

    // EFFECTS: displays maze list to user, 1 page of 5 mazes at a time
    private void displayMazeList(int mazeListPage) {
//        if (mazeList.size() == 0) {
//            System.out.println("No mazes made yet!");
//        } else {
//            System.out.printf("Mazes Page %d%n", mazeListPage + 1);
//            for (int i = 0; i < 5; i++) {
//                int mazeNumber = mazeListPage * 5 + i;
//                if (mazeNumber < mazeList.size()) {
//                    System.out.printf("#%d: %s%n", i + 1, mazeList.readMaze(mazeNumber).getName());
//                }
//            }
//        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
//        System.out.println("\nSelect from:");
//        System.out.println("\tprev -> Show the 5 previous mazes");
//        System.out.println("\tnext -> Show the 5 next mazes");
//        System.out.println("\tadd -> Add a new random maze");
//        System.out.println("\tdel -> Delete a maze");
//        System.out.println("\tgame -> Start a game");
//        System.out.println("q -> quit Minoquar");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void processCommand(String command) {
//        switch (command) {
//            case "prev":
//                updateMazeListPage("prev");
//                break;
//            case "next":
//                updateMazeListPage("next");
//                break;
//            case "add":
//                createMaze();
//                break;
//            case "del":
//                deleteMaze();
//                break;
//            case "game":
//                chooseMaze();
//                break;
//            default:
//                System.out.println("I didn't understand that");
//        }
    }

    private void updateMazeListPage(String direction) {
//        switch (direction) {
//            case "prev":
//                mazeListPage -= 1;
//                if (mazeListPage < 0) {
//                    mazeListPage = mazeList.size() / 5;
//                }
//                break;
//            case "next":
//                mazeListPage += 1;
//                if (mazeListPage * 5 > mazeList.size()) {
//                    mazeListPage = 0;
//                }
//            default:
//                System.out.println("updateMazeListPage got a bad input!");
//        }
    }

    // MODIFIES: this
    // EFFECTS: adds a new random maze to maze list
    private void createMaze() {
//        String size = inputMazeSize();
//        if (!size.equals("back")) {
//            String name = inputMazeName();
//            switch (size) {
//                case "xs":
//                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.EXTRA_SMALL);
//                    break;
//                case "sm":
//                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.SMALL);
//                    break;
//                case "md":
//                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.MEDIUM);
//                    break;
//                case "lg":
//                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.LARGE);
//                    break;
//                case "xl":
//                    mazeList.createRandomMaze(name, MazeSizeModel.MazeSize.EXTRA_LARGE);
//                    break;
//                default:
//                    System.out.println("I didn't understand that");
//            }
//        }
    }

    // MODIFIES: this.input
    // EFFECTS: displays menu of maze sizes to user, and processes user input
    private String inputMazeSize() {
//        System.out.println("\nChoose maze size");
//        List<MazeSizeModel.MazeSize> sizes = new ArrayList<>(Arrays.asList(
//                MazeSizeModel.MazeSize.EXTRA_SMALL,
//                MazeSizeModel.MazeSize.SMALL,
//                MazeSizeModel.MazeSize.MEDIUM,
//                MazeSizeModel.MazeSize.LARGE,
//                MazeSizeModel.MazeSize.EXTRA_LARGE));
//        for (MazeSizeModel.MazeSize size : sizes) {
//            printMazeSizeInput(size);
//        }
//        System.out.println("back -> go back to menu");
//        return input.next().toLowerCase();
        return null; //stub
    }

    // EFFECTS: prints maze size selection string (format: [size code] -> [size name]
    private void printMazeSizeInput(MazeSizeModel.MazeSize size) {
//        System.out.printf("\t%s -> %s%n", MazeSizeModel.getSizeCode(size), MazeSizeModel.getSizeName(size));
    }

    // MODIFIES: this.input
    // EFFECTS: requests maze name, and processes user input
    private String inputMazeName() {
//        System.out.println("Name this maze:");
//        return input.next().toLowerCase();
        return null; //stub
    }

    // MODIFIES: this
    // EFFECTS: deletes a maze from maze list
    private void deleteMaze() {
//        displayMazeList(mazeListPage);
//        System.out.println("\nType a maze's number to delete it");
//        int mazeNumber = input.nextInt();
//        try {
//            System.out.printf("\nDeleting Maze %d%n", mazeNumber);
//            mazeList.deleteMaze(getListIndexFromDisplayIndex(mazeNumber));
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("That maze doesn't exist");
//        }
    }

    // MODIFIES: this.input
    // EFFECTS: displays maze list, processes user input, and starts game with chosen maze
    private void chooseMaze() {
//        displayMazeList(mazeListPage);
//        System.out.println("\nType a maze's number to choose it");
//        int mazeNumber = input.nextInt();
//        try {
//            MazeModel maze = mazeList.readMaze(getListIndexFromDisplayIndex(mazeNumber));
//            System.out.printf("\nNow playing Maze %d%n", mazeNumber);
//            startGame(maze);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("That maze doesn't exist");
//        }
    }

    // EFFECTS: starts game using given maze
    private void startGame(MazeModel maze) {
//        new GameUI(new GameModel(maze, new PositionModel(7, 0)));
    }




    // EFFECTS: returns maze's index in list given it's number in display
    private int getListIndexFromDisplayIndex(int displayIndex) {
//        return mazeListPage * 5 + displayIndex - 1;
        return 0; //stub
    }

    class CreateListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;  // allows access to enabled state

        // EFFECTS: create new CreateListener with given button
        public CreateListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: processes button press and maze name input, and creates new maze in mazeList if input is valid
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = mazeName.getText();

            if (name.equals("") || alreadyInList(name)) {
                Toolkit.getDefaultToolkit().beep();
                mazeName.requestFocusInWindow();
                mazeName.selectAll();
                return;
            }

            // Determine insertion index, after selected item or 0 if list empty
            int index = list.getSelectedIndex();
            if (index == -1) {
                index = 0;
            } else {
                index++;
            }

            mazeList.createRandomMaze(index, name, MazeSizeModel.MazeSize.EXTRA_SMALL);

            // Reset text field
            mazeName.requestFocusInWindow();
            mazeName.setText("");

            // Select new item and make it visible
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }

        // EFFECTS: returns true if maze with matching name exists in maze list
        protected boolean alreadyInList(String name) {
            return mazeList.containsSameName(name);
        }


        // EFFECTS: processes DocumentEvent sent when text added to text field
        @Override
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // EFFECTS: processes DocumentEvent sent when text deleted from text field
        @Override
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // EFFECTS: processes DocumentEvent sent when some document attribute changes
        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    class DeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            mazeList.deleteMaze(index);

            int size = mazeList.getSize();
            if (size == 0) {
                deleteButton.setEnabled(false);
            } else {
                if (index == mazeList.getSize()) {
                    index--;
                }
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }

    class StartGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: implement actual game start
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (list.getSelectedIndex() == -1) {
                deleteButton.setEnabled(false);
                startGameButton.setEnabled(false);
            } else {
                deleteButton.setEnabled(true);
                startGameButton.setEnabled(true);
            }
        }
    }
}

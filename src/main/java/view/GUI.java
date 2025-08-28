package view;

import handlers.Handler;
import handlers.JsonHandler;
import model.Monster;
import handlers.XmlHandler;
import handlers.YamlHandler;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree monsterTree;
    private List<Monster> monsters = new ArrayList<>();
    private Handler handlerChain;
    private DefaultMutableTreeNode jsonNode;
    private DefaultMutableTreeNode xmlNode;
    private DefaultMutableTreeNode yamlNode;
    
    public GUI() {
        super("Бестиарий");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        
        initHandlerChain();
        initUI();
    }
    
    private void initHandlerChain() {
        Handler jsonHandler = new JsonHandler();
        Handler xmlHandler = new XmlHandler();
        Handler yamlHandler = new YamlHandler();
        
        jsonHandler.setNext(xmlHandler);
        xmlHandler.setNext(yamlHandler);
        
        this.handlerChain = jsonHandler;
    }
    
    private void initUI() {
        JPanel buttonPanel = new JPanel();
        JButton importButton = new JButton("Импорт");
        JButton exportButton = new JButton("Экспорт");
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.setBackground(Color.decode("#333333"));
        importButton.setBackground(Color.decode("#333333"));
        exportButton.setBackground(Color.decode("#333333"));
        buttonPanel.setForeground(Color.decode("#ffffff"));
        importButton.setForeground(Color.decode("#ffffff"));
        exportButton.setForeground(Color.decode("#ffffff"));

        rootNode = new DefaultMutableTreeNode("Монстры");
        
        jsonNode = new DefaultMutableTreeNode("JSON");
        xmlNode = new DefaultMutableTreeNode("XML");
        yamlNode = new DefaultMutableTreeNode("YAML");
        
        rootNode.add(jsonNode);
        rootNode.add(xmlNode);
        rootNode.add(yamlNode);
        
        treeModel = new DefaultTreeModel(rootNode);
        monsterTree = new JTree(treeModel);

        JScrollPane treeScrollPane = new JScrollPane(monsterTree);
        
        importButton.addActionListener(e -> importMonsters());
        exportButton.addActionListener(e -> exportMonsters());
        
        monsterTree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();
                    if (node != null && node.getUserObject() instanceof Monster) {
                        showMonsterDetails((Monster) node.getUserObject());
                    }
                }
        });
        
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(treeScrollPane, BorderLayout.CENTER);
    }
    
    private void importMonsters() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON, XML, YAML файлы", "json", "xml", "yaml", "yml"));
        fileChooser.setMultiSelectionEnabled(true);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                List<Monster> importedMonsters = handlerChain.handleRequest(file);
                if (importedMonsters != null) {
                    monsters.addAll(importedMonsters);
                    updateTree();
                }
            }
        }
    }
    
    private void exportMonsters() {
    if (monsters.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Нет данных для экспорта", "Ошибка", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
    
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON файлы (*.json)", "json"));
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML файлы (*.xml)", "xml"));
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("YAML файлы (*.yaml, *.yml)", "yaml", "yml"));
    fileChooser.setAcceptAllFileFilterUsed(false);
    
    int result = fileChooser.showSaveDialog(this);
    if (result == JFileChooser.APPROVE_OPTION) {
        File file = fileChooser.getSelectedFile();
        FileNameExtensionFilter selectedFilter = (FileNameExtensionFilter) fileChooser.getFileFilter();
        String[] extensions = selectedFilter.getExtensions();
        String primaryExtension = extensions[0];
        
        boolean hasValidExtension = false;
    for (String ext : extensions) {
        if (file.getName().toLowerCase().endsWith("." + ext)) {
            hasValidExtension = true;
            break;
        }
    }
    if (!hasValidExtension) {
        file = new File(file.getAbsolutePath() + "." + primaryExtension);
}

        try {
            handlerChain.exportData(monsters, file);
            JOptionPane.showMessageDialog(this, 
                "Данные успешно экспортированы в " + file.getName(), 
                "Успех", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Ошибка при экспорте: " + e.getMessage(), 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    
        private void updateTree() {
        jsonNode.removeAllChildren();
        xmlNode.removeAllChildren();
        yamlNode.removeAllChildren();
        
        for (Monster monster : monsters) {
            DefaultMutableTreeNode monsterNode = new DefaultMutableTreeNode(monster);
            
            if (monster.getSource().startsWith("JSON")) {
                jsonNode.add(monsterNode);
            } 
            else if (monster.getSource().startsWith("XML")) {
                xmlNode.add(monsterNode);
            } 
            else if (monster.getSource().startsWith("YAML")) {
                yamlNode.add(monsterNode);
            }
        }
        
        treeModel.reload();
    }
    
    private void showMonsterDetails(Monster monster) {
        JFrame detailsFrame = new JFrame(monster.getName());
        detailsFrame.setSize(500, 400);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        panel.add(new JLabel("Имя:"));
        JTextField nameField = new JTextField(monster.getName());
        panel.add(nameField);
        
        panel.add(new JLabel("Описание:"));
        JTextArea descriptionArea = new JTextArea(monster.getDescription());
        descriptionArea.setLineWrap(true);
        panel.add(new JScrollPane(descriptionArea));
        
        panel.add(new JLabel("Уровень опасности:"));
        JSpinner dangerSpinner = new JSpinner(new SpinnerNumberModel(monster.getDangerLevel(), 1, 10, 1));
        panel.add(dangerSpinner);
        
        panel.add(new JLabel("Места обитания:"));
        JTextArea habitatsArea = new JTextArea(String.join("\n", monster.getHabitats()));
        habitatsArea.setLineWrap(true);
        panel.add(new JScrollPane(habitatsArea));
        
        panel.add(new JLabel("Первое упоминание:"));
        JTextField mentionField = new JTextField(monster.getFirstMention());
        panel.add(mentionField);
        
        panel.add(new JLabel("Иммунитеты:"));
        JTextArea immunitiesArea = new JTextArea(String.join("\n", monster.getImmunities()));
        immunitiesArea.setLineWrap(true);
        panel.add(new JScrollPane(immunitiesArea));
        
        panel.add(new JLabel("Время активности:"));
        JTextField activityField = new JTextField(monster.getActivityTime());
        panel.add(activityField);
        
        panel.add(new JLabel("Рецепт яда:"));
        JTextArea poisonArea = new JTextArea(monster.getPoisonRecipe());
        poisonArea.setLineWrap(true);
        panel.add(new JScrollPane(poisonArea));
        
        panel.add(new JLabel("Время приготовления:"));
        JSpinner prepTimeSpinner = new JSpinner(new SpinnerNumberModel(monster.getPreparationTime(), 1, 999, 1));
        panel.add(prepTimeSpinner);
        
        panel.add(new JLabel("Эффективность:"));
        JTextField effectivenessField = new JTextField(monster.getEffectiveness());
        panel.add(effectivenessField);
        
        panel.add(new JLabel("Рост:"));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(monster.getHeight(), 0.1, 100.0, 0.1));
        panel.add(heightSpinner);
        
        panel.add(new JLabel("Вес:"));
        JSpinner weightSpinner = new JSpinner(new SpinnerNumberModel(monster.getWeight(), 0.0, 1000.0, 0.1));
        panel.add(weightSpinner);
        
        panel.add(new JLabel("Источник:"));
        panel.add(new JLabel(monster.getSource()));
        
        JButton saveButton = new JButton("Сохранить изменения");
        saveButton.addActionListener(e -> {
            monster.setName(nameField.getText());
            monster.setDescription(descriptionArea.getText());
            monster.setDangerLevel((Integer) dangerSpinner.getValue());
            
            List<String> newHabitats = new ArrayList<>();
            for (String line : habitatsArea.getText().split("\n")) {
                if (!line.trim().isEmpty()) {
                    newHabitats.add(line.trim());
                }
            }
            monster.setHabitats(newHabitats);
            
            monster.setFirstMention(mentionField.getText());
            
            List<String> newImmunities = new ArrayList<>();
            for (String line : immunitiesArea.getText().split("\n")) {
                if (!line.trim().isEmpty()) {
                    newImmunities.add(line.trim());
                }
            }
            monster.setImmunities(newImmunities);
            
            monster.setActivityTime(activityField.getText());
            monster.setPoisonRecipe(poisonArea.getText());
            monster.setPreparationTime((Integer) prepTimeSpinner.getValue());
            monster.setEffectiveness(effectivenessField.getText());
            monster.setHeight((Double) heightSpinner.getValue());
            monster.setWeight((Double) weightSpinner.getValue());
            
            updateTree();
            detailsFrame.dispose();
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        
        detailsFrame.setLayout(new BorderLayout());
        detailsFrame.add(new JScrollPane(panel), BorderLayout.CENTER);
        detailsFrame.add(buttonPanel, BorderLayout.SOUTH);
        detailsFrame.setVisible(true);
    }
}
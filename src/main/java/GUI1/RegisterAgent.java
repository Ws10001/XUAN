package GUI1;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterAgent extends JPanel {
    private JComboBox<String> agentClassComboBox;
    private JComboBox<String> configMethodComboBox;

    private DefaultListModel<String> agentListModel;
    private JList<String> registeredAgentsList;
    private JLabel agentCountLabel;
    private Map<String, String> agentVarToLine;

    private static final String MAIN_GUI_FILE_PATH = "src/main/java/GUI1/MainGUI.java";
    private static final String CONFIG_DATA_FILE_PATH = "src/main/java/GUI1/ConfigData.java";
    private static final String AGENTS_PACKAGE = "Agents";

    private static final Pattern AGENT_LINE_PATTERN = Pattern.compile(
            "\\s*AgentInitiator\\s+(\\w+)\\s*=\\s*new\\s+AgentInitiator\\(ac,\\s*(\\w+)\\.class,\\s*config\\.(\\w+)\\(\\)\\);\\s*"
    );

    private static final Pattern CONFIG_METHOD_PATTERN = Pattern.compile(
            "\\s*(public|private|protected)\\s+(static\\s+)?[\\w<>\\[\\],\\s]+\\s+(\\w+)\\s*\\(\\s*\\)\\s*\\{?"
    );

    private static final Dimension COMBO_SIZE = new Dimension(220, 32);
    private static final Dimension BUTTON_SIZE = new Dimension(160, 38);
    private static final Dimension SMALL_BUTTON_SIZE = new Dimension(110, 32);

    public RegisterAgent() {
        agentVarToLine = new HashMap<>();
        initUI();
        loadData();
        refreshAgentList();
    }

    private void initUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        setFont(new Font("微软雅黑", Font.PLAIN, 12));

        JLabel titleLabel = new JLabel("智能体管理与注册", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createVerticalStrut(15));

        JPanel agentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        agentPanel.add(new JLabel("选择Agent类："));
        agentClassComboBox = new JComboBox<>();
        agentClassComboBox.setPreferredSize(COMBO_SIZE);
        agentClassComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        agentPanel.add(agentClassComboBox);
        add(agentPanel);
        add(Box.createVerticalStrut(15));

        JPanel configPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        configPanel.add(new JLabel("选择配置方法："));
        configMethodComboBox = new JComboBox<>();
        configMethodComboBox.setPreferredSize(COMBO_SIZE);
        configMethodComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        configPanel.add(configMethodComboBox);
        add(configPanel);
        add(Box.createVerticalStrut(20));

        JPanel managePanel = new JPanel();
        managePanel.setLayout(new BoxLayout(managePanel, BoxLayout.Y_AXIS));
        managePanel.setBorder(BorderFactory.createTitledBorder("已注册智能体管理"));
        managePanel.setPreferredSize(new Dimension(450, 180));

        agentCountLabel = new JLabel("当前已注册智能体数量：0");
        agentCountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        agentCountLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        managePanel.add(agentCountLabel);

        agentListModel = new DefaultListModel<>();
        registeredAgentsList = new JList<>(agentListModel);
        registeredAgentsList.setPreferredSize(new Dimension(400, 120));
        registeredAgentsList.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        registeredAgentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScroll = new JScrollPane(registeredAgentsList);
        listScroll.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        managePanel.add(listScroll);

        JPanel manageBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton refreshListBtn = createSmallButton("刷新已注册列表");
        refreshListBtn.addActionListener(e -> refreshAgentList());
        manageBtnPanel.add(refreshListBtn);

        JButton deleteBtn = createSmallButton("删除选中智能体");
        deleteBtn.addActionListener(e -> deleteSelectedAgent());
        manageBtnPanel.add(deleteBtn);
        managePanel.add(manageBtnPanel);
        add(managePanel);
        add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        JButton refreshComboBtn = createButton("刷新Agent/配置列表");
        refreshComboBtn.addActionListener(e -> refreshComboData());
        buttonPanel.add(refreshComboBtn);

        JButton saveBtn = createButton("保存生成代码");
        saveBtn.addActionListener(e -> saveCode());
        buttonPanel.add(saveBtn);

        JButton startBtn = createButton("启动MainGUI");
        startBtn.addActionListener(e -> startMainGUI());
        buttonPanel.add(startBtn);

        add(buttonPanel);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(BUTTON_SIZE);
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setBackground(new Color(60, 120, 180));
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton createSmallButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(SMALL_BUTTON_SIZE);
        btn.setFont(new Font("微软雅黑", Font.BOLD, 12));
        btn.setBackground(new Color(100, 149, 237));
        btn.setForeground(Color.black);
        btn.setFocusPainted(false);
        return btn;
    }

    private void refreshComboData() {
        agentClassComboBox.removeAllItems();
        configMethodComboBox.removeAllItems();
        loadAgentClasses();
        loadConfigMethods();
        showMessage("Agent类和配置方法已刷新！", "成功");
    }

    private void loadData() {
        loadAgentClasses();
        loadConfigMethods();
    }

    private void loadAgentClasses() {
        List<String> classNames = new ArrayList<>();
        try {
            File agentsDir = new File("src/main/java/" + AGENTS_PACKAGE.replace('.', '/'));
            if (agentsDir.exists() && agentsDir.isDirectory()) {
                File[] files = agentsDir.listFiles((dir, name) -> name.endsWith(".java"));
                if (files != null) {
                    for (File file : files) {
                        classNames.add(file.getName().replace(".java", ""));
                    }
                }
            }
        } catch (Exception e) {
            showMessage("加载Agent类失败：" + e.getMessage(), "错误");
        }

        Collections.sort(classNames);
        classNames.forEach(agentClassComboBox::addItem);
    }

    private void loadConfigMethods() {
        List<String> methodNames = new ArrayList<>();
        try {
            File configFile = new File(CONFIG_DATA_FILE_PATH);
            if (!configFile.exists()) {
                showMessage("未找到ConfigData源文件：" + CONFIG_DATA_FILE_PATH, "错误");
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(CONFIG_DATA_FILE_PATH), StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.startsWith("//") || trimmed.startsWith("*") || trimmed.startsWith("/*")) continue;
                if (trimmed.startsWith("if ") || trimmed.startsWith("if(") ||
                        trimmed.startsWith("for ") || trimmed.startsWith("for(") ||
                        trimmed.startsWith("while ") || trimmed.startsWith("while(") ||
                        trimmed.startsWith("switch ") || trimmed.startsWith("switch(") ||
                        trimmed.startsWith("catch ") || trimmed.startsWith("catch(")) continue;

                Matcher matcher = CONFIG_METHOD_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String methodName = matcher.group(3);
                    if (!"ConfigData".equals(methodName)) {
                        methodNames.add(methodName);
                    }
                }
            }
        } catch (Exception e) {
            showMessage("加载配置方法失败：" + e.getMessage(), "错误");
        }

        Collections.sort(methodNames);
        methodNames.forEach(configMethodComboBox::addItem);
    }

    private void parseRegisteredAgents() {
        agentVarToLine.clear();
        List<String> agentVars = new ArrayList<>();

        try {
            List<String> allLines = Files.readAllLines(Paths.get(MAIN_GUI_FILE_PATH), StandardCharsets.UTF_8);
            for (String line : allLines) {
                Matcher matcher = AGENT_LINE_PATTERN.matcher(line);
                if (matcher.matches()) {
                    String varName = matcher.group(1);
                    agentVarToLine.put(varName, line.trim());
                    agentVars.add(varName);
                }
            }
        } catch (Exception e) {
            showMessage("解析已注册智能体失败：" + e.getMessage(), "错误");
        }

        agentListModel.clear();
        agentVars.forEach(agentListModel::addElement);
        agentCountLabel.setText("当前已注册智能体数量：" + agentVars.size());
    }

    private void refreshAgentList() {
        parseRegisteredAgents();
    }

    private void deleteSelectedAgent() {
        String selectedVar = registeredAgentsList.getSelectedValue();
        if (selectedVar == null || selectedVar.isEmpty()) {
            showMessage("请先选中要删除的智能体！", "提示");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "确定要删除智能体 [" + selectedVar + "] 吗？",
                "删除确认",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String targetLine = agentVarToLine.get(selectedVar);
            if (targetLine == null) {
                showMessage("未找到该智能体的注册代码！", "错误");
                return;
            }

            List<String> allLines = Files.readAllLines(Paths.get(MAIN_GUI_FILE_PATH), StandardCharsets.UTF_8);
            List<String> newLines = new ArrayList<>();
            for (String line : allLines) {
                if (!line.trim().equals(targetLine)) {
                    newLines.add(line);
                }
            }
            Files.write(Paths.get(MAIN_GUI_FILE_PATH), newLines, StandardCharsets.UTF_8);

            refreshAgentList();
            showMessage("智能体 [" + selectedVar + "] 删除成功！", "成功");

        } catch (Exception e) {
            showMessage("删除失败：" + e.getMessage(), "错误");
        }
    }

    private void saveCode() {
        String agentClass = (String) agentClassComboBox.getSelectedItem();
        String configMethod = (String) configMethodComboBox.getSelectedItem();

        if (agentClass == null || configMethod == null) {
            showMessage("请先选择Agent类和配置方法！", "提示");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(MAIN_GUI_FILE_PATH), StandardCharsets.UTF_8);

            // 统一使用通配符导入
            ensureImport(lines, "import Agents.*;");
            ensureImport(lines, "import jade.wrapper.AgentContainer;");
            ensureImport(lines, "import utilities.Common.AgentInitiator;");
            ensureImport(lines, "import utilities.Common.Container;");

            int insertIndex = -1;
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().equals("ConfigData config = new ConfigData();")) {
                    insertIndex = i + 1;
                    break;
                }
            }

            if (insertIndex == -1) {
                showMessage("未找到ConfigData实例化行！", "错误");
                return;
            }

            String varName = agentClass.toLowerCase() + "_" + configMethod.toLowerCase() + "_control";
            String codeLine = String.format(
                    "        AgentInitiator %s = new AgentInitiator(ac, %s.class, config.%s());",
                    varName, agentClass, configMethod
            );

            boolean exists = lines.stream().anyMatch(line -> line.trim().equals(codeLine.trim()));
            if (exists) {
                showMessage("该智能体已注册！", "提示");
                return;
            }

            lines.add(insertIndex, codeLine);
            Files.write(Paths.get(MAIN_GUI_FILE_PATH), lines, StandardCharsets.UTF_8);

            refreshAgentList();
            showMessage("代码添加成功！", "成功");
        } catch (Exception e) {
            showMessage("保存失败：" + e.getMessage(), "错误");
        }
    }

    private void ensureImport(List<String> lines, String importStmt) {
        for (String line : lines) {
            if (line.trim().equals(importStmt.trim())) {
                return;
            }
        }

        int packageIndex = -1;
        int lastImportIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            String trim = lines.get(i).trim();
            if (trim.startsWith("package ")) {
                packageIndex = i;
            } else if (trim.startsWith("import ")) {
                lastImportIndex = i;
            }
        }

        int insertIndex = (lastImportIndex != -1) ? lastImportIndex + 1 : (packageIndex != -1 ? packageIndex + 1 : 0);
        lines.add(insertIndex, importStmt);
    }

    private void startMainGUI() {
        new Thread(() -> {
            try {
                GUI1.MainGUI.main(null);
            } catch (Exception e) {
                showMessage("启动失败：" + e.getMessage(), "错误");
            }
        }).start();
    }

    private void showMessage(String message, String title) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                this,
                message,
                title,
                title.equals("错误") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE
        ));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Agent注册工具");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(520, 480);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.add(new RegisterAgent());
            frame.setVisible(true);
        });
    }
}
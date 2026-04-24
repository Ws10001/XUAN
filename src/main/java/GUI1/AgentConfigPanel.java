package GUI1;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AgentConfigPanel extends JPanel {
    //配置面板所有组件
    private JTextField agentNameField;
    private JComboBox<String> agentTypeComboBox;
    private String currentTypeName;
    private JTextArea jsonArea;
    private JLabel configCountLabel;

    //配置方法列表
    private JList<String> configMethodList;

    //列表模型
    private DefaultListModel<String> listModel;

    private static final String CONFIG_DATA_PATH = "src/main/java/GUI1/ConfigData.java";

    public AgentConfigPanel() {
        initializeUI();
        refreshConfigList();
    }
    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel addConfigPanel = createAddConfigPanel();
        JPanel manageConfigPanel = createManageConfigPanel();

        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.Y_AXIS));
        totalPanel.add(addConfigPanel);
        totalPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        totalPanel.add(manageConfigPanel);
        add(totalPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("智能体参数配置", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * 创建“智能体配置”添加面板
     *
     * @return 添加配置面板
     */
    private JPanel createAddConfigPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createTitledBorder("智能体配置"));

        // 智能体名称输入区
        JPanel agentNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        agentNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel agentNameLabel = new JLabel("自定义智能体名称：");
        agentNameLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        agentNameLabel.setPreferredSize(new Dimension(180, 30));

        agentNameField = new JTextField();
        agentNameField.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        agentNameField.setPreferredSize(new Dimension(300, 30));

        agentNamePanel.add(agentNameLabel);
        agentNamePanel.add(agentNameField);
        mainPanel.add(agentNamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 智能体类型选择区
        JPanel agentTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        agentTypePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel agentTypeLabel = new JLabel("选择智能体类型");
        agentTypeLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        agentTypeLabel.setPreferredSize(new Dimension(180, 30));

        agentTypeComboBox = new JComboBox<>(new String[]{"Base", "AGV", "Arm", "Buffer", "Machine", "Warehouse"});
        agentTypeComboBox.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        agentTypeComboBox.addActionListener(e -> {
            currentTypeName = (String) agentTypeComboBox.getSelectedItem();
            if (currentTypeName != null) {
                setTypeDescription(currentTypeName);
            }
        });
        agentTypeComboBox.setPreferredSize(new Dimension(300, 30));

        agentTypePanel.add(agentTypeLabel);
        agentTypePanel.add(agentTypeComboBox);
        mainPanel.add(agentTypePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // JSON 编辑区
        JPanel jsonAreaPanel = new JPanel(new BorderLayout());
        jsonAreaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel jsonAreaLabel = new JLabel("自定义参数 (JSON格式)：");
        jsonAreaLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
        jsonAreaLabel.setPreferredSize(new Dimension(200, 25));

        jsonArea = new JTextArea(8, 30);
        jsonArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        jsonArea.setText("{\n    \n}");
        jsonArea.setLineWrap(true);
        jsonArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(jsonArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));

        jsonAreaPanel.add(jsonAreaLabel, BorderLayout.NORTH);
        jsonAreaPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(jsonAreaPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 保存按钮
        JButton saveButton = new JButton("保存配置");
        saveButton.addActionListener(new SaveButtonListener());
        saveButton.setPreferredSize(new Dimension(120, 35));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(buttonPanel);
        return mainPanel;
    }

    /**
     * 创建“已保存配置管理”面板
     *
     * @return 已保存配置管理面板
     */
    private JPanel createManageConfigPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "已保存配置管理",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Microsoft YaHei", Font.BOLD, 14),
                Color.DARK_GRAY
        ));

        configCountLabel = new JLabel("已保存配置数量：0");
        configCountLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 13));
        configCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(configCountLabel);
        panel.add(Box.createVerticalStrut(15));

        listModel = new DefaultListModel<>();
        configMethodList = new JList<>(listModel);
        configMethodList.setFont(new Font("Consolas", Font.PLAIN, 15));
        configMethodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(configMethodList);
        listScrollPane.setPreferredSize(new Dimension(400, 150));
        listScrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(listScrollPane);
        panel.add(Box.createVerticalStrut(10));

        JButton lookButton = new JButton("查看选中配置");
        lookButton.setPreferredSize(new Dimension(120, 35));
        lookButton.addActionListener(e -> {
            String selectedMethod = configMethodList.getSelectedValue();
            if (selectedMethod != null) {
                lookConfigMethod(selectedMethod);
            } else {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "请先选中要查看的配置", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton deleteButton = new JButton("删除选中配置");
        deleteButton.setPreferredSize(new Dimension(120, 35));
        deleteButton.addActionListener(new DeleteButtonListener());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(lookButton);
        buttonPanel.add(deleteButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(buttonPanel);

        return panel;
    }

    /**
     * 保存按钮事件监听器
     */
    private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String agentName = agentNameField.getText().trim();
            String customJson = jsonArea.getText().trim();

            if (agentName.isEmpty()) {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "请输入智能体名称", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (customJson.isEmpty()) {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "自定义JSON不能为空", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String configJson = buildConfigJson(customJson);

            if (appendToConfigData(agentName, configJson)) {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "配置保存成功！已在ConfigData类中添加[" + agentName + "]对应的方法",
                        "成功", JOptionPane.INFORMATION_MESSAGE);

                agentNameField.setText("");
                refreshConfigList();
                jsonArea.setText("{\n    \n}");
            } else {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "配置保存失败！请检查ConfigData文件路径或权限",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 删除按钮事件监听器
     */
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedMethod = configMethodList.getSelectedValue();
            if (selectedMethod == null || selectedMethod.isEmpty()) {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "请先选中要删除的配置", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    AgentConfigPanel.this,
                    "确定要删除[" + selectedMethod + "]对应的配置吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            if (deleteConfigMethod(selectedMethod)) {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "[" + selectedMethod + "]配置删除成功！",
                        "成功", JOptionPane.INFORMATION_MESSAGE);
                refreshConfigList();
            } else {
                JOptionPane.showMessageDialog(AgentConfigPanel.this,
                        "[" + selectedMethod + "]配置删除失败！请检查文件权限",
                        "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 构建最终写入 ConfigData.java 的 JSON 字符串
     *
     * @param customJson 用户输入的 JSON
     * @return 处理后的 JSON 字符串
     */
    private String buildConfigJson(String customJson) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\n");

        if (!customJson.equals("{\n    \n}")) {
            String trimmedCustom = customJson.trim()
                    .replaceFirst("^\\{", "")
                    .replaceFirst("}$", "")
                    .trim();

            if (!trimmedCustom.isEmpty()) {
                jsonBuilder.append(trimmedCustom.replace("\n", "\n    "));
            }
        }

        jsonBuilder.append("\n}");
        return jsonBuilder.toString();
    }

    /**
     * 将配置方法追加到 ConfigData.java 文件中
     *
     * @param agentName  智能体名称
     * @param configJson 配置 JSON
     * @return 添加成功返回 true，否则返回 false
     */
    private boolean appendToConfigData(String agentName, String configJson) {
        String methodContent = "\n    public JSONObject " + agentName + "() {\n" +
                "        String params = \"\"\"\n" +
                "                " + configJson.replace("\n", "\n                ") + "\n" +
                "                \"\"\";\n" +
                "        return new JSONObject(params);\n" +
                "    }\n";

        try {
            Path path = Paths.get(CONFIG_DATA_PATH);
            String originalContent = Files.readString(path);

            String newContent = originalContent.replaceAll("\\s*}$", "") + methodContent + "}";

            Files.writeString(path, newContent);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 删除指定的配置方法
     *
     * @param methodName 方法名
     * @return 删除成功返回 true，否则返回 false
     */
    private boolean deleteConfigMethod(String methodName) {
        Path path = Paths.get(CONFIG_DATA_PATH);
        try {
            String content = Files.readString(path);

            String methodStartPattern = "public\\s+JSONObject\\s+" + Pattern.quote(methodName) + "\\s*\\(";
            Pattern pattern = Pattern.compile(methodStartPattern);
            Matcher matcher = pattern.matcher(content);

            if (!matcher.find()) {
                return false;
            }

            int methodStartIndex = matcher.start();
            int braceStart = content.indexOf("{", matcher.end());
            if (braceStart == -1) {
                return false;
            }

            int braceCount = 0;
            int methodEndIndex = -1;

            for (int i = braceStart; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        methodEndIndex = i;
                        break;
                    }
                }
            }

            if (methodEndIndex == -1) {
                return false;
            }

            String newContent = content.substring(0, methodStartIndex)
                    + content.substring(methodEndIndex + 1);

            Files.writeString(path, newContent);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看指定配置方法
     *
     * @param methodName 方法名
     */
    private void lookConfigMethod(String methodName) {
        if (methodName == null || methodName.isEmpty()) {
            JOptionPane.showMessageDialog(AgentConfigPanel.this,
                    "请先选中要查看的配置", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Path path = Paths.get(CONFIG_DATA_PATH);
        try {
            String content = Files.readString(path);

            String methodStartPattern = "public\\s+JSONObject\\s+" + Pattern.quote(methodName) + "\\s*\\(";
            Pattern pattern = Pattern.compile(methodStartPattern);
            Matcher matcher = pattern.matcher(content);

            if (!matcher.find()) {
                JOptionPane.showMessageDialog(this, "没找到目标方法！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int methodStartIndex = matcher.start();
            int braceStart = content.indexOf("{", matcher.end());
            if (braceStart == -1) {
                JOptionPane.showMessageDialog(this, "方法体格式不正确！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int braceCount = 0;
            int methodEndIndex = -1;

            for (int i = braceStart; i < content.length(); i++) {
                char c = content.charAt(i);
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        methodEndIndex = i;
                        break;
                    }
                }
            }

            if (methodEndIndex == -1) {
                JOptionPane.showMessageDialog(this,
                        "未能正确找到方法结束位置！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String methodContent = content.substring(methodStartIndex, methodEndIndex + 1);

            JTextArea lookArea = new JTextArea(methodContent);
            lookArea.setFont(new Font("Monospaced", Font.BOLD, 14));
            lookArea.setEditable(false);
            lookArea.setTabSize(4);

            JScrollPane previewScrollPane = new JScrollPane(lookArea);
            previewScrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, previewScrollPane, "代码预览 - " + methodName, JOptionPane.PLAIN_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 刷新已保存配置列表
     */
    private void refreshConfigList() {
        listModel.clear();
        List<String> methodNames = parseConfigMethods();
        configCountLabel.setText("已保存配置数量：" + methodNames.size());

        for (String method : methodNames) {
            listModel.addElement(method);
        }
    }

    /**
     * 解析 ConfigData.java 文件中的所有配置方法名
     *
     * @return 方法名列表
     */
    private List<String> parseConfigMethods() {
        List<String> methodNames = new ArrayList<>();
        try {
            String content = Files.readString(Paths.get(CONFIG_DATA_PATH));
            Pattern pattern = Pattern.compile("public\\s+JSONObject\\s+(\\w+)\\s*\\(");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                methodNames.add(matcher.group(1));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return methodNames;
    }

    /**
     * 根据选中的智能体类型填充默认 JSON 内容
     *
     * @param baseAgent 智能体类型
     */
    private void setTypeDescription(String baseAgent) {
        String description;
        switch (baseAgent) {
            case "AGV":
                description = "{\n\"agent_name\":\"AGV\",\n" +
                        "\"ip\":\"127.0.0.1\",\n" +
                        "\"port\":10302,\n" +
                        "\"open_port\":11302,\n" +
                        "\"direction\":1,\n" +
                        "\"InfoService\":\"AGV_Info\",  \n}";
                break;
            case "Arm":
                description = "{\n\"agent_name\":\"arm_xi\",\n" +
                        "\"ip\":\"127.0.0.1\",\n" +
                        "\"port\":10105,\n" +
                        "\"DFServices\":{\n" +
                        "    \"B2L\":\"arm_buffer_milling\",\n" +
                        "    \"L2B\":\"arm_milling_buffer\",\n" +
                        "}, \n}";
                break;
            case "Buffer":
                description = "{\n\"agent_name\":\"buffer\",\n" +
                        "\"ip\":\"127.0.0.1\",\n" +
                        "\"port\":10201,\n" +
                        "\"open_port\":11201,  \n}";
                break;
            case "Machine":
                description = "{\n\"agent_name\":\"Machine\",\n" +
                        "\"ip\":\"127.0.0.1\",\n" +
                        "\"port\":10101,\n" +
                        "\"type\":\"lathe\",\n" +
                        "\"index\":1,\n" +
                        "\"DFServices\":{\n" +
                        "    \"process\":\"lathe_process\",\n" +
                        "}, \n}";
                break;
            case "Warehouse":
                description = "{\n\"agent_name\":\"warehouse\",\n" +
                        "\"ip\":\"127.0.0.1\",\n" +
                        "\"port\":10402,\n" +
                        "\"open_port\":11402,\n" +
                        "\"import_site\":0,\n" +
                        "\"export_site\":2,\n" +
                        "\"DFService\":\"warehouse\",\n" +
                        "\"is_finished_warehouse\":false,  \n}";
                break;
            case "Base":
            default:
                description = "{\n    \n}";
        }
        jsonArea.setText(description);
    }

    /**
     * 程序入口，用于独立运行测试
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("智能体配置");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 700);
            frame.setLocationRelativeTo(null);
            frame.add(new AgentConfigPanel());
            frame.setVisible(true);
        });
    }
}
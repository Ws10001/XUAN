package GUI1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class CustomBehaviorPanel extends JPanel {
    // 项目中已有智能体包的根路径
    private static final String EXISTING_BEHAVIOUR_ROOT_PATH = "src/main/java/Behaviours/";

    //定义全局静态变量,存储各智能体包下的模板行为
    private static final Map<String, List<String>> AGENT_TEMPLATE_BEHAVIORS = new HashMap<>();
    static {
        AGENT_TEMPLATE_BEHAVIORS.put("AGV", new ArrayList<>());
        AGENT_TEMPLATE_BEHAVIORS.put("Arm", new ArrayList<>());
        AGENT_TEMPLATE_BEHAVIORS.put("Buffer", new ArrayList<>());
        AGENT_TEMPLATE_BEHAVIORS.put("Machine", new ArrayList<>());
        AGENT_TEMPLATE_BEHAVIORS.put("Warehouse", new ArrayList<>());
        AGENT_TEMPLATE_BEHAVIORS.put("TongYong", new ArrayList<>());
    }
    private JTextField behaviorNameField;
    private JComboBox<String> baseBehaviorComboBox;
    private JComboBox<String> agentTypeComboBox;
    private JTextArea codeArea;
    private JButton saveButton;
    private JButton previewButton;
    private JList<String> behaviorList;
    private DefaultListModel<String> behaviorListModel;
    private JButton viewButton;
    private JButton deleteButton; // 删除按钮核心引用
    private JTextArea helpArea;

    public CustomBehaviorPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        initializeUI();
        setTemplateBehaviors();
        refreshBehaviorList();
    }




    private void initializeUI() {
        // 顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(10, 10));
        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 标题
        JLabel titleLabel = new JLabel("自定义行为编辑器", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // 基本信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("行为基本信息"));
        infoPanel.setLayout(new GridLayout(3, 2, 10, 10)); // 3行2列，间距10
        infoPanel.setPreferredSize(new Dimension(800, 120));

        // 行为名称
        infoPanel.add(new JLabel("行为名称：", JLabel.CENTER));
        behaviorNameField = new JTextField();
        behaviorNameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        infoPanel.add(behaviorNameField);

        // 智能体类型
        infoPanel.add(new JLabel("智能体类型：", JLabel.CENTER));
        agentTypeComboBox = new JComboBox<>(new String[]{"TongYong", "AGV", "Arm", "Buffer", "Machine", "Warehouse"});
        agentTypeComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        agentTypeComboBox.addActionListener(e -> refreshBehaviorList());
        infoPanel.add(agentTypeComboBox);

        // 基础行为类型
        infoPanel.add(new JLabel("基础行为类型：", JLabel.CENTER));
        baseBehaviorComboBox = new JComboBox<>(new String[]{
                "Function", "Communication", "Service", "Driver", "Driver_Once",
                "Inquire", "Listener_Information", "Listener_action", "CommunicationSpecial"
        });
        baseBehaviorComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        baseBehaviorComboBox.addActionListener(new BaseBehaviorListener());
        infoPanel.add(baseBehaviorComboBox);

        topPanel.add(infoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 中部面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(15, 10)); // 增加间距
        centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 代码编辑面板
        JPanel codePanel = new JPanel();
        codePanel.setLayout(new BorderLayout(10, 10));
        codePanel.setBorder(BorderFactory.createTitledBorder("行为方法编辑（需重写提示的方法）"));

        codeArea = new JTextArea();
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 14));//设置变成专用字体
        codeArea.setTabSize(4);//按下Tab键，缩进4字符
        codeArea.setLineWrap(false);//不自动换行
        codeArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));//设置灰色边框
        JScrollPane codeScrollPane = new JScrollPane(codeArea);//加上滚动条
        codeScrollPane.setPreferredSize(new Dimension(700, 350));
        codePanel.add(codeScrollPane, BorderLayout.CENTER);

        // 行为列表+操作按钮面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension(300, 350));

        // 行为列表
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BorderLayout(10, 10));
        listPanel.setBorder(BorderFactory.createTitledBorder("行为列表"));

        behaviorListModel = new DefaultListModel<>();
        behaviorList = new JList<>(behaviorListModel);
        behaviorList.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        behaviorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        behaviorList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane listScrollPane = new JScrollPane(behaviorList);
        listScrollPane.setPreferredSize(new Dimension(280, 250));
        listPanel.add(listScrollPane, BorderLayout.CENTER);

        // 操作按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0)); // 1行2列，间距10
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setPreferredSize(new Dimension(280, 60)); // 固定高度

        // 查看按钮
        viewButton = new JButton("查看自定义代码");
        viewButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        viewButton.setPreferredSize(new Dimension(130, 35)); // 固定按钮大小
        viewButton.addActionListener(e -> viewSelectedCustomBehavior());
        viewButton.setToolTipText("仅支持查看你创建的自定义行为代码");

        // 删除按钮
        deleteButton = new JButton("删除自定义行为");
        deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        deleteButton.setPreferredSize(new Dimension(130, 35)); // 固定按钮大小
        deleteButton.addActionListener(e -> deleteSelectedCustomBehavior());
        deleteButton.setToolTipText("仅支持删除你创建的自定义行为");

        // 按钮添加到面板
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);

        // 按钮面板添加到列表面板底部
        listPanel.add(buttonPanel, BorderLayout.SOUTH);
        rightPanel.add(listPanel, BorderLayout.CENTER);

        // 组装中部面板
        centerPanel.add(codePanel, BorderLayout.CENTER);
        centerPanel.add(rightPanel, BorderLayout.EAST);
        add(centerPanel, BorderLayout.CENTER);

        // 底部面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(10, 10));
        bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // 功能按钮面板
        JPanel funcButtonPanel = new JPanel();
        funcButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        previewButton = new JButton("预览代码");
        previewButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        previewButton.setPreferredSize(new Dimension(140, 35));
        previewButton.addActionListener(e -> previewCompleteCode());

        saveButton = new JButton("保存自定义行为");
        saveButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        saveButton.setPreferredSize(new Dimension(140, 35));
        saveButton.addActionListener(new SaveBehaviorListener());

        funcButtonPanel.add(previewButton);
        funcButtonPanel.add(saveButton);
        bottomPanel.add(funcButtonPanel, BorderLayout.NORTH);

        // 使用说明面板
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new BorderLayout());
        helpPanel.setBorder(BorderFactory.createTitledBorder("使用说明"));
        helpArea = new JTextArea();
        helpArea.setFont(new Font("宋体", Font.PLAIN, 15));
        helpArea.setEditable(false);//不可编辑
        helpArea.setLineWrap(true);//自动换行
        helpArea.setWrapStyleWord(true);//按单词或完整语句换行
        helpArea.setPreferredSize(null);
        helpArea.setText("1. 填写行为名称并选择智能体类型和基础行为类型\n" +
                        "2. 在编辑区编写需要重写的方法代码（仅自定义逻辑）\n" +
                        "3. 点击'预览代码'查看代码\n" +
                        "4. 点击'保存自定义行为'将你的行为保存到对应智能体包下"
        );
        helpPanel.add(helpArea, BorderLayout.CENTER);//给该区域加上滚动式条
        bottomPanel.add(helpPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 生成完整代码
     */
    private String generateCompleteCode() {
        String behaviorName = behaviorNameField.getText().trim();
        if (behaviorName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入自定义行为名称", "提示", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        String agentType = (String) agentTypeComboBox.getSelectedItem();
        String baseBehavior = (String) baseBehaviorComboBox.getSelectedItem();
        String userCode = codeArea.getText().trim();

        StringBuilder codeTemplate = new StringBuilder();
        codeTemplate.append("package Behaviours.").append(agentType).append(";\n\n");
        if (Objects.equals(baseBehavior, "Listener_action"))
            codeTemplate.append("import java.util.Objects;\n" + "import java.util.Scanner;\n");
        if (Objects.equals(baseBehavior, "CommunicationSpecial"))
            codeTemplate.append("import jade.lang.acl.ACLMessage;");
        codeTemplate.append("import Agents.").append(agentType).append(";\n");
        codeTemplate.append("import Behaviours.Base.").append(baseBehavior).append(";\n\n");
        codeTemplate.append("public class ").append(behaviorName).append(" extends ").append(baseBehavior).append(" {\n\n");
        codeTemplate.append("    private ").append(agentType).append(" agent;\n\n");
        codeTemplate.append("    public ").append(behaviorName).append("(").append(agentType).append(" agent) {\n");
        codeTemplate.append("        super(agent);\n");
        codeTemplate.append("        this.agent = agent;\n");
        codeTemplate.append("    }\n\n");
        codeTemplate.append(userCode).append("\n");
        codeTemplate.append("}\n");

        return codeTemplate.toString();
    }

    /**
     * 预览代码
     */
    private void previewCompleteCode() {
        String completeCode = codeArea.getText().trim();
        if (completeCode.isEmpty()) return;
        JTextArea previewArea = new JTextArea(completeCode);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        previewArea.setEditable(false);
        previewArea.setTabSize(4);
        JScrollPane previewScrollPane = new JScrollPane(previewArea);
        previewScrollPane.setPreferredSize(new Dimension(800, 500));
        JOptionPane.showMessageDialog(this, previewScrollPane, "代码预览", JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * 保存自定义行为
     */
    private class SaveBehaviorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String behaviorName = behaviorNameField.getText().trim();
            if (behaviorName.isEmpty()) {
                JOptionPane.showMessageDialog(CustomBehaviorPanel.this, "请输入自定义行为名称", "提示", JOptionPane.WARNING_MESSAGE);
                behaviorNameField.requestFocus();//让行为名称边框自动获得光标，不需要自行点击
                return;
            }

            String agentType = (String) agentTypeComboBox.getSelectedItem();
            List<String> templateBehaviors = AGENT_TEMPLATE_BEHAVIORS.getOrDefault(agentType, new ArrayList<>());//没有该类型就返回空列表，防止空指针报错
            if (templateBehaviors.contains(behaviorName)) {
                JOptionPane.showMessageDialog(CustomBehaviorPanel.this,
                        "错误：行为名称[" + behaviorName + "]是系统模板行为，禁止创建同名自定义行为！\n请更换行为名称。",
                        "名称冲突", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String completeCode = generateCompleteCode();
            if (completeCode == null) return;
            Path targetDirPath = Paths.get(EXISTING_BEHAVIOUR_ROOT_PATH + agentType);// 将字符串路径转为Java路径对象
            File targetDir = targetDirPath.toFile();
            if (!targetDir.exists() || !targetDir.isDirectory()) {
                JOptionPane.showMessageDialog(CustomBehaviorPanel.this,
                        "错误：智能体[" + agentType + "]的包路径不存在！\n路径：" + targetDirPath,
                        "路径错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Path targetFilePath = targetDirPath.resolve(behaviorName + ".java");//路径拼接,给文件夹拼接行为名称
            if (targetFilePath.toFile().exists()) {
                int confirm = JOptionPane.showConfirmDialog(CustomBehaviorPanel.this,
                        "自定义行为[" + behaviorName + "]已存在，是否覆盖？", "确认覆盖", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
            }

            try {
                Files.writeString(targetFilePath, completeCode);
                JOptionPane.showMessageDialog(CustomBehaviorPanel.this,
                        "自定义行为保存成功！\n保存路径：" + targetFilePath + "\n你可在右侧列表中选中该行为，点击删除按钮删除。",
                        "保存成功", JOptionPane.INFORMATION_MESSAGE);
                behaviorNameField.setText("");
                codeArea.setText("");
                refreshBehaviorList();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(CustomBehaviorPanel.this,
                        "保存失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 刷新行为列表
     */
    private void refreshBehaviorList() {
        behaviorListModel.clear();

        String currentAgentType = (String) agentTypeComboBox.getSelectedItem();
        List<String> templateBehaviors = AGENT_TEMPLATE_BEHAVIORS.getOrDefault(currentAgentType, new ArrayList<>());

        // 添加模板行为
        behaviorListModel.addElement("系统模板行为（不可操作）");
        if (templateBehaviors.isEmpty()) {
            behaviorListModel.addElement("该智能体暂无系统模板行为");
        } else {
            for (String template : templateBehaviors) {
                behaviorListModel.addElement(template);
            }
        }
        // 添加自定义行为
        behaviorListModel.addElement("自定义行为（可操作）");
        Path dirPath = Paths.get(EXISTING_BEHAVIOUR_ROOT_PATH + currentAgentType);
        File dir = dirPath.toFile();
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((d, name) -> {
                if (!name.endsWith(".java") || name.startsWith(".") || name.contains("~")) return false;
                String behaviorName = name.replace(".java", "");
                return !templateBehaviors.contains(behaviorName);
            });
            //文件不为空且有元素
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String behaviorName = file.getName().replace(".java", "");
                    behaviorListModel.addElement(behaviorName);
                }
            } else {
                behaviorListModel.addElement("暂无自定义行为");
            }
        } else {
            behaviorListModel.addElement("暂无自定义行为");
        }
    }
    /**
     * 查看自定义代码
     */
    private void viewSelectedCustomBehavior() {
        String selected = behaviorList.getSelectedValue();
        if (selected == null || AGENT_TEMPLATE_BEHAVIORS.getOrDefault((String)agentTypeComboBox.getSelectedItem(), new ArrayList<>()).contains(selected)) {
            JOptionPane.showMessageDialog(this, "仅支持查看你创建的自定义行为！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentAgentType = (String) agentTypeComboBox.getSelectedItem();
        Path filePath = Paths.get(EXISTING_BEHAVIOUR_ROOT_PATH + currentAgentType + "/" + selected + ".java");

        try {
            String fullCode = Files.readString(filePath, StandardCharsets.UTF_8);
            String userCodeOnly = extractUserCode(fullCode);

            JTextArea viewArea = new JTextArea(userCodeOnly);
            viewArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
            viewArea.setEditable(false);
            viewArea.setTabSize(4);
            viewArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JScrollPane viewScrollPane = new JScrollPane(viewArea);
            viewScrollPane.setPreferredSize(new Dimension(800, 450));

            JOptionPane.showMessageDialog(this, viewScrollPane,
                    "自定义代码查看 - " + selected, JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "读取自定义行为失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 提取用户编写的代码
     */
    private String extractUserCode(String fullCode) {
        List<String> overrideMethods = new ArrayList<>();
        int index = 0;//设置起始位置，扫描全部代码
        while (true) {
            int voidIndex = fullCode.indexOf("void", index);//记录动一次出现void的位置
           //如果没找到
            if (voidIndex == -1) {
                break;
            }
            int braceStart = fullCode.indexOf("{", voidIndex);
            //如果没找到
            if (braceStart == -1) {
                break;
            }
            int braceCount = 0;
            int endIndex = -1;
            for (int i = braceStart; i < fullCode.length(); i++) {
                char c = fullCode.charAt(i);
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        endIndex = i;
                        break;
                    }
                }
            }
            if (endIndex != -1) {
                String methodCode = fullCode.substring(voidIndex-8, endIndex + 1).trim();
                overrideMethods.add(methodCode);
                index = endIndex + 1;
            } else {
                break;
            }
        }

        if (overrideMethods.isEmpty()) {
            return "该自定义行为暂无你编写的重写方法代码！";
        }
        //给每个方法间加上两个空行
        return String.join("\n\n", overrideMethods);
    }

    //自定义行为删除功能
    private void deleteSelectedCustomBehavior() {
        // 1. 获取选中的自定义行为名称
        String selectedBehavior = behaviorList.getSelectedValue();
        if (selectedBehavior == null) {
            JOptionPane.showMessageDialog(this, "请先选中要删除的自定义行为！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. 校验是否是自定义行为
        List<String> templateBehaviors = AGENT_TEMPLATE_BEHAVIORS.getOrDefault((String)agentTypeComboBox.getSelectedItem(), new ArrayList<>());
        if (templateBehaviors.contains(selectedBehavior)
                || selectedBehavior.equals("暂无自定义行为")
                || selectedBehavior.equals("系统模板行为（不可操作）")
                || selectedBehavior.equals("自定义行为（可操作）")
                || selectedBehavior.equals("该智能体暂无系统模板行为")) {
            JOptionPane.showMessageDialog(this, "仅支持删除你创建的自定义行为！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. 二次确认（醒目警告）
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "⚠️ 警告！确认删除自定义行为：" + selectedBehavior + "吗？删除后文件将永久删除，无法恢复！",
                "删除确认",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE // 警告图标
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return; // 用户取消删除
        }

        // 4. 执行删除
        String currentAgentType = (String) agentTypeComboBox.getSelectedItem();
        Path behaviorFilePath = Paths.get(EXISTING_BEHAVIOUR_ROOT_PATH + currentAgentType + "/" + selectedBehavior + ".java");

        try {
            if (Files.exists(behaviorFilePath)) {
                Files.delete(behaviorFilePath);
                JOptionPane.showMessageDialog(this,
                        "自定义行为" + selectedBehavior + "已成功删除！",
                        "删除成功",
                        JOptionPane.INFORMATION_MESSAGE);

                // 5. 刷新列表
                refreshBehaviorList();
            } else {
                JOptionPane.showMessageDialog(this, "自定义行为文件不存在！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "删除失败：" + ex.getMessage() + "可能是文件被占用或权限不足。",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();//报错信息打印到控制台
        }
    }
    /**
     * 基础行为类型说明
     */
    private class BaseBehaviorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String baseBehavior = (String) baseBehaviorComboBox.getSelectedItem();
            if (baseBehavior != null) {
                showBehaviorTypeDescription(baseBehavior);
            }
        }
    }

    private void showBehaviorTypeDescription(String baseBehavior) {
        String description;
        String description1;
        switch (baseBehavior) {
            case "Function":
                description = "Function类：核心功能逻辑，重写onTick()方法实现定时执行逻辑";
                description1 = "@Override\n" +
                        "    public void onTick() {\n" +
                        "    }";
                break;
            case "Communication":
                description = "Communication类：智能体通信，重写Bind()/SomeOperations()/VariableReset()";
                description1 = "@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void SomeOperations() {\n"+
                        "    }\n" +
                        "@Override\n" +
                        "    public void VariableReset() {\n"+
                        "    }";
                break;
            case "Service":
                description = "Service类：外部设备通信，重写Bind()/SomeOperations()/VariableReset()";
                description1 = "@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void SomeOperations() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void VariableReset() {\n" +
                        "    }";
                break;
            case "Driver":
                description = "Driver类：设备驱动操作，重写Bind()/VariableReset()/VarChangeWhenCondition()";
                description1 = "@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void VariableReset() {\n" +
                        "    }";
                break;
            case "Driver_Once":
                description = "Driver_Once类：一次性驱动操作，重写Bind()/SomeOperations()";
                description1 = "@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void SomeOperations() {\n" +
                        "    }";
                break;
            case "Inquire":
                description = "Inquire类：信息查询，重写Bind()/SomeOperations()/VariableReset()";
                description1 ="@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void SomeOperations() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void VariableReset() {\n" +
                        "    }";
                break;
            case "Listener_Information":
                description = "Listener_Information类：监听信息消息，重写ActionSpace()";
                description1 = "@Override\n" +
                        "    public void ActionSpace() {\n" +
                        "    }";
                break;
            case "Listener_action":
                description = "Listener_action类：监听动作消息，重写ActionSpace()";
                description1 = "@Override\n" +
                        "    public void ActionSpace() {\n" +
                        "    }";
                break;
            case "CommunicationSpecial":
                description = "CommunicationSpecial类：单次通信，重写Bind()/SomeOperations()/VariableReset()";
                description1 = "@Override\n" +
                        "    public void Bind() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void SomeOperations() {\n" +
                        "    }\n" +
                        "@Override\n" +
                        "    public void VariableReset() {\n" +
                        "    }";
                break;
            default:
                description = "请编写该基础行为类型的重写方法";
                description1 = "";
        }
        helpArea.setText(
                        "\n当前基础行为类型说明：\n" + description
        );
        codeArea.setText(description1);
    }
    /**
     * 设置模板行为
     */
    private void setTemplateBehaviors(){
        String[] agvClassNames = {
                "Call_Buffer_Import",
                "Call_Other_Avoid",
                "Control_Export",
                "Call_Product_Warehouse_Import",
                "Control_Import",
                "Function_Export",
                "Control_GetPosition",
                "Control_Move",
                "Function_Obs_avoid",
                "Inquire_Other_Path_Info",
                "Listen_Information",
                "Listen_Action",
                "Function_Move",
                "ManuallyOrder",
                "Service_PathPlanning",
                "Service_ObstacleAvoid"
        };
        String[] armClassNames = {
                "Call_Buffer_Process_complete",
                "Control_Transfer",
                "Call_Machine_Process",
                "Listen_Action",
                "Listen_Information"
        };
        String[] bufferClassNames = {
                "Call_AGV_Import",
                "Call_AGV_Transfer",
                "Call_AGV_Ready",
                "Call_Machine_Ready",
                "Control_Export",
                "Control_Import",
                "Function_CheckTask",
                "Function_Che_Process",
                "Function_Export",
                "Inquire_Self_Free_Location",
                "Listen_Action",
                "Listen_Information",
                "ManuallyOrder",
                "Function_Xi_Process",
                "Call_Arm_Trans"
        };
        String[] machineClassNames = {
                "Call_Arm_Transfer",
                "Control_Chuck",
                "Listen_Information",
                "Listen_Action",
                "Control_Process"
        };
        String[] warehouseClassNames = {
                "Call_AGV_Ready",
                "Call_AGV_Import",
                "Call_AGV_Transfer",
                "Control_Convey_Export",
                "Control_Convey_Import",
                "Control_Move",
                "Function_Export",
                "Inquire_Buffer_Free_Location",
                "Listen_Action",
                "Listen_Information",
                "ManuallyOrder",
                "QueryOrderBehaviour"
        };

        AGENT_TEMPLATE_BEHAVIORS.put("AGV", new ArrayList<>(Arrays.asList(agvClassNames)));
        AGENT_TEMPLATE_BEHAVIORS.put("Arm", new ArrayList<>(Arrays.asList(armClassNames)));
        AGENT_TEMPLATE_BEHAVIORS.put("Buffer", new ArrayList<>(Arrays.asList(bufferClassNames)));
        AGENT_TEMPLATE_BEHAVIORS.put("Machine", new ArrayList<>(Arrays.asList(machineClassNames)));
        AGENT_TEMPLATE_BEHAVIORS.put("Warehouse", new ArrayList<>(Arrays.asList(warehouseClassNames)));
    }
    // 测试入口
    public static void main(String[] args) {
        //启动界面，防止界面卡死
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("自定义行为编辑器");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700); // 调整窗口大小，确保所有控件显示
            frame.setLocationRelativeTo(null); // 居中显示
            frame.add(new CustomBehaviorPanel());
            frame.setVisible(true);
        });
    }
}
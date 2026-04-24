package GUI1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AgentDesignerPanel extends JPanel {
    private final ArrayList<String> inputRecords = new ArrayList<>();
    private JTextField agentNameField;
    private JComboBox<String> baseAgentComboBox;

    // 行为选择相关：双列表
    private DefaultListModel<String> availableBehaviorModel;
    private JList<String> availableBehaviorList;
    private DefaultListModel<String> selectedBehaviorModel;
    private JList<String> selectedBehaviorList;

    private JButton createAgentButton;
    private JButton refreshButton;
    private JButton defineflagbit;
    private List<String> currentBehaviors;
    public String currentBaseAgent;

    // 智能体管理相关
    private DefaultListModel<String> agentListModel;
    private JList<String> agentList;
    private JButton deleteAgentButton;
    private JButton refreshAgentListButton;

    // 标志位管理相关
    private DefaultListModel<String> flagBitListModel;
    private JList<String> flagBitList;
    private JButton deleteFlagBitButton;

    // 模板库智能体常量
    private static final String[] TEMPLATE_AGENTS = {"AGV", "Arm", "BaseAgent", "Buffer", "Machine", "Warehouse"};

    public AgentDesignerPanel() {
        currentBehaviors = new ArrayList<>();
        currentBaseAgent = "BaseAgent";

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        initializeUI();
        updateBehaviorsForAgentType(currentBaseAgent);
        loadCreatedAgents();
        updateFlagBitList();
    }

    private void initializeUI() {
        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        // 1. 标题区域
        JLabel titleLabel = new JLabel("智能体低代码设计平台", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 2. 中间核心区域：左右分割
        JSplitPane centerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);//水平分割为两块区域
        centerSplitPane.setDividerLocation(650);//分割线初始位置
        centerSplitPane.setResizeWeight(0.65);

        // 左侧面板：基础信息 + 双列表行为选择
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));//盒式布局，竖直排列
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // 2.1 智能体基本信息面板
        JPanel agentInfoPanel = new JPanel(new GridBagLayout());
        agentInfoPanel.setBorder(BorderFactory.createTitledBorder("智能体基本信息"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // 智能体名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("智能体名称:");
        nameLabel.setPreferredSize(new Dimension(120, 30));
        nameLabel.setFont(new Font("微软雅黑",Font.PLAIN ,14));
        agentInfoPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;//水平方向填充
        gbc.weightx = 1.0;
        agentNameField = new JTextField();
        agentNameField.setPreferredSize(new Dimension(0, 30));
        agentInfoPanel.add(agentNameField, gbc);

        // 基础智能体类型
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        JLabel typeLabel = new JLabel("基础智能体类型:");
        typeLabel.setPreferredSize(new Dimension(120, 30));
        typeLabel.setFont(new Font("微软雅黑",Font.PLAIN ,14));
        agentInfoPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        baseAgentComboBox = new JComboBox<>(new String[]{"Base", "AGV", "Arm", "Buffer", "Machine", "Warehouse"});
        baseAgentComboBox.setPreferredSize(new Dimension(0, 30));
        baseAgentComboBox.addActionListener(e -> {
            currentBaseAgent = (String) baseAgentComboBox.getSelectedItem();
            updateBehaviorsForAgentType(currentBaseAgent);
        });
        agentInfoPanel.add(baseAgentComboBox, gbc);
        leftPanel.add(agentInfoPanel);

        // 分隔符
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2.2 双列表行为选择面板
        JPanel behaviorPanel = new JPanel(new BorderLayout(10, 10));
        behaviorPanel.setBorder(BorderFactory.createTitledBorder("行为配置（双击快速添加/移除）"));

        // 行为选择主面板：左右列表 + 中间按钮
        JPanel behaviorMainPanel = new JPanel(new BorderLayout(10, 10));

        // 左侧：可用行为列表
        JPanel availablePanel = new JPanel(new BorderLayout(5, 5));
        availablePanel.add(new JLabel("可用行为（双击添加）", JLabel.CENTER), BorderLayout.NORTH);
        availableBehaviorModel = new DefaultListModel<>();
        availableBehaviorList = new JList<>(availableBehaviorModel);
        availableBehaviorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//可一次性多选
        availableBehaviorList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        // 双击添加行为
        availableBehaviorList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    List<String> selected = availableBehaviorList.getSelectedValuesList();
                    for (String behavior : selected) {
                        addBehaviorToSelected(behavior);
                    }
                }
            }
        });
        JScrollPane availableScrollPane = new JScrollPane(availableBehaviorList);//给可用行为列表添加滚动条
        availableScrollPane.setPreferredSize(new Dimension(250, 250));
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);
        behaviorMainPanel.add(availablePanel, BorderLayout.WEST);

        // 中间：添加/移除按钮
        JPanel behaviorButtonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints btnGbc = new GridBagConstraints();
        btnGbc.insets = new Insets(10, 5, 10, 5);//设置组件与周围设置间距
        btnGbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addBtn = new JButton("添加 →");
        addBtn.addActionListener(e -> {
            List<String> selected = availableBehaviorList.getSelectedValuesList();
            for (String behavior : selected) {
                addBehaviorToSelected(behavior);
            }
        });
        btnGbc.gridy = 0;
        behaviorButtonPanel.add(addBtn, btnGbc);

        JButton removeBtn = new JButton("← 移除");
        removeBtn.addActionListener(e -> {
            List<String> selected = selectedBehaviorList.getSelectedValuesList();
            for (String behavior : selected) {
                removeBehaviorFromSelected(behavior);
            }
        });
        btnGbc.gridy = 1;
        behaviorButtonPanel.add(removeBtn, btnGbc);
        behaviorMainPanel.add(behaviorButtonPanel, BorderLayout.CENTER);

        // 右侧：已选行为列表
        JPanel selectedPanel = new JPanel(new BorderLayout(5, 5));
        selectedPanel.add(new JLabel("已选行为（双击移除）", JLabel.CENTER), BorderLayout.NORTH);
        selectedBehaviorModel = new DefaultListModel<>();
        selectedBehaviorList = new JList<>(selectedBehaviorModel);
        selectedBehaviorList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedBehaviorList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        // 双击移除行为
        selectedBehaviorList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    List<String> selected = selectedBehaviorList.getSelectedValuesList();
                    for (String behavior : selected) {
                        removeBehaviorFromSelected(behavior);
                    }
                }
            }
        });
        JScrollPane selectedScrollPane = new JScrollPane(selectedBehaviorList);
        selectedScrollPane.setPreferredSize(new Dimension(250, 250));
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);
        behaviorMainPanel.add(selectedPanel, BorderLayout.EAST);

        behaviorPanel.add(behaviorMainPanel, BorderLayout.CENTER);

        // 行为面板底部：刷新按钮
        JPanel behaviorBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshButton = new JButton("刷新可用行为列表");
        refreshButton.addActionListener(e -> {
            updateBehaviorsForAgentType(currentBaseAgent);
            JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                    "可用行为列表已刷新！\n当前可用行为: " + availableBehaviorModel.size() + " 个",
                    "刷新成功",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        behaviorBottomPanel.add(refreshButton);
        behaviorPanel.add(behaviorBottomPanel, BorderLayout.SOUTH);

        leftPanel.add(behaviorPanel);
        centerSplitPane.setLeftComponent(leftPanel);

        // 右侧面板：智能体管理 + 标志位管理（仅替换按钮，布局完全保留）
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // 2.3 已创建智能体管理面板
        JPanel agentManagePanel = new JPanel(new BorderLayout(10, 10));
        agentManagePanel.setBorder(BorderFactory.createTitledBorder("智能体管理"));
        agentManagePanel.setPreferredSize(new Dimension(0, 200));//首选高度200，宽度自适应

        // 智能体列表
        agentListModel = new DefaultListModel<>();
        agentList = new JList<>(agentListModel);
        agentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//设置单选
        agentList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane agentScrollPane = new JScrollPane(agentList);
        agentManagePanel.add(agentScrollPane, BorderLayout.CENTER);

        // 智能体操作按钮
        JPanel agentButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        refreshAgentListButton = new JButton("刷新列表");
        refreshAgentListButton.addActionListener(e -> loadCreatedAgents());

        deleteAgentButton = new JButton("删除智能体");
        deleteAgentButton.addActionListener(e -> {
            String selectedAgent = agentList.getSelectedValue();
            if (selectedAgent == null || selectedAgent.isEmpty()) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "请选择要删除的智能体！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 检查是否为模板库智能体
            if (isTemplateAgent(selectedAgent)) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "【" + selectedAgent + "】是系统模板库智能体，禁止删除！",
                        "操作受限",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(AgentDesignerPanel.this,
                    "确定要删除智能体【" + selectedAgent + "】吗？\n删除后无法恢复！",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    deleteAgent(selectedAgent);
                    agentListModel.removeElement(selectedAgent);
                    JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                            "智能体【" + selectedAgent + "】已成功删除！",
                            "删除成功",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                            "删除失败：" + ex.getMessage(),
                            "错误",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 按钮添加
        agentButtonPanel.add(refreshAgentListButton);
        agentButtonPanel.add(deleteAgentButton);
        agentManagePanel.add(agentButtonPanel, BorderLayout.SOUTH);
        rightPanel.add(agentManagePanel);

        // 分隔符
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2.4 自定义标志位管理面板
        JPanel flagBitManagePanel = new JPanel(new BorderLayout(10, 10));
        flagBitManagePanel.setBorder(BorderFactory.createTitledBorder("自定义标志位管理"));
        flagBitManagePanel.setPreferredSize(new Dimension(0, 180)); // 保留原始尺寸

        // 标志位列表
        flagBitListModel = new DefaultListModel<>();
        flagBitList = new JList<>(flagBitListModel);
        flagBitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        flagBitList.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        JScrollPane flagBitScrollPane = new JScrollPane(flagBitList);
        flagBitManagePanel.add(flagBitScrollPane, BorderLayout.CENTER);

        // 标志位操作按钮
        JPanel flagBitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        defineflagbit = new JButton("添加标志位");
        defineflagbit.addActionListener(new DefineFlagBit());

        deleteFlagBitButton = new JButton("删除标志位");
        deleteFlagBitButton.addActionListener(e -> {
            String selectedFlag = flagBitList.getSelectedValue();
            if (selectedFlag == null || selectedFlag.isEmpty()) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "请选择要删除的标志位！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(AgentDesignerPanel.this,
                    "确定要删除标志位【" + selectedFlag + "】吗？",
                    "删除确认",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteFlagBit(selectedFlag);
                flagBitListModel.removeElement(selectedFlag);
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "标志位【" + selectedFlag + "】已成功删除！",
                        "删除成功",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        flagBitButtonPanel.add(defineflagbit);
        flagBitButtonPanel.add(deleteFlagBitButton);
        flagBitManagePanel.add(flagBitButtonPanel, BorderLayout.SOUTH);
        rightPanel.add(flagBitManagePanel);

        centerSplitPane.setRightComponent(rightPanel);
        mainPanel.add(centerSplitPane, BorderLayout.CENTER);

        // 3. 底部区域：创建按钮 + 状态信息
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        // 创建智能体按钮面板
        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createAgentButton = new JButton("创建智能体");
        createAgentButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        createAgentButton.setPreferredSize(new Dimension(150, 35));
        createAgentButton.addActionListener(new CreateAgentListener());
        createPanel.add(createAgentButton);
        bottomPanel.add(createPanel, BorderLayout.WEST);

        // 状态信息面板
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createTitledBorder("状态信息"));
        JLabel statusLabel = new JLabel("已选择 " + selectedBehaviorModel.size() + " 个行为");
        statusPanel.add(statusLabel);
        bottomPanel.add(statusPanel, BorderLayout.CENTER);

        // 监听已选行为列表变化更新状态,创建一个匿名监听器对象,直接重写方法,不用单独写类
        selectedBehaviorModel.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent e) {
                statusLabel.setText("已选择 " + selectedBehaviorModel.size() + " 个行为");
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent e) {
                statusLabel.setText("已选择 " + selectedBehaviorModel.size() + " 个行为");
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent e) {
                statusLabel.setText("已选择 " + selectedBehaviorModel.size() + " 个行为");
            }
        });

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * 将行为从可用列表添加到已选列表
     */
    private void addBehaviorToSelected(String behavior) {
        if (behavior == null || behavior.isEmpty()) {
            return;
        }
        if (!selectedBehaviorModel.contains(behavior)) {
            selectedBehaviorModel.addElement(behavior);
            availableBehaviorModel.removeElement(behavior);
        }
    }

    /**
     * 将行为从已选列表移除回可用列表
     */
    private void removeBehaviorFromSelected(String behavior) {
        if (behavior == null || behavior.isEmpty()) {
            return;
        }
        if (!availableBehaviorModel.contains(behavior)) {
            availableBehaviorModel.addElement(behavior);
            selectedBehaviorModel.removeElement(behavior);
        }
    }

    /**
     * 检查是否为模板库智能体
     */
    private boolean isTemplateAgent(String agentName) {
        for (String template : TEMPLATE_AGENTS) {
            if (template.equals(agentName) || agentName.endsWith(" (模板库)")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加载已创建的智能体列表
     */
    private void loadCreatedAgents() {
        agentListModel.clear();
        // 加载用户创建的智能体
        String agentsDirPath = "src/main/java/Agents";
        File agentsDir = new File(agentsDirPath);
        //判断路径是否存在，并且是否是文件夹
        if (agentsDir.exists() && agentsDir.isDirectory()) {
            //列出当前文件夹下的所有文件和子文件夹,并配合Lambda过滤器,保留符合条件的文件
            File[] files = agentsDir.listFiles((dir, name) -> name.endsWith(".java"));
            if (files != null) {
                for (File file : files) {
                    //获取文件名
                    String agentName = file.getName().replace(".java", "");
                    // 排除模板库智能体
                    if (!isTemplateAgent(agentName)) {
                        agentListModel.addElement(agentName);
                    }
                }
            }
        }
    }

    /**
     * 删除指定智能体文件
     */
    private void deleteAgent(String agentName) throws IOException {
        String agentDir = "src/main/java/Agents";
        //拼接路径
        Path agentPath = Paths.get(agentDir, agentName + ".java");
        if (Files.exists(agentPath)) {
            Files.delete(agentPath);
        } else {
            throw new IOException("智能体文件不存在：" + agentPath);
        }
    }

    /**
     * 更新标志位列表显示
     */
    private void updateFlagBitList() {
        flagBitListModel.clear();
        for (String record : inputRecords) {
            flagBitListModel.addElement(record);
        }
    }

    /**
     * 删除指定自定义标志位
     */
    private void deleteFlagBit(String flagBit) {
        inputRecords.remove(flagBit);
        updateFlagBitList();
    }

    /**
     * 更新指定智能体类型对应的可用行为列表
     */
    public void updateBehaviorsForAgentType(String agentType) {
        currentBehaviors.clear();
        String behaviorDirPath = "src/main/java/Behaviours/" + agentType;
        File behaviorDir = new File(behaviorDirPath);

        if (behaviorDir.exists() && behaviorDir.isDirectory()) {
            File[] files = behaviorDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".java")) {
                        String behaviorName = file.getName().replace(".java", "");
                        if (!currentBehaviors.contains(behaviorName)) {
                            currentBehaviors.add(behaviorName);
                        }
                    }
                }
            }
        }
        //按首字母排序
        currentBehaviors.sort(String::compareTo);
        // 刷新可用行为列表，同时清空已选列表
        availableBehaviorModel.clear();
        selectedBehaviorModel.clear();
        for (String behavior : currentBehaviors) {
            availableBehaviorModel.addElement(behavior);
        }
    }

    /**
     * 标志位添加监听器
     */
    private class DefineFlagBit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = JOptionPane.showInputDialog(
                    AgentDesignerPanel.this,
                    "请输入自定义标志位内容：",
                    "自定义标志位",
                    JOptionPane.PLAIN_MESSAGE  //弹窗样式常量
            );

            if (userInput != null && !userInput.trim().isEmpty()) {
                String flagBit = userInput.trim();
                inputRecords.add(flagBit);
                updateFlagBitList();
                JOptionPane.showMessageDialog(
                        AgentDesignerPanel.this,
                        "标志位添加成功！当前共" + inputRecords.size() + "条记录",
                        "操作成功",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else if (userInput != null) {
                JOptionPane.showMessageDialog(
                        AgentDesignerPanel.this,
                        "输入内容不能为空，请重新输入！",
                        "提示",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    /**
     * 创建智能体监听器
     */
    private class CreateAgentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String agentName = agentNameField.getText().trim();
            if (agentName.isEmpty()) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this, "请输入智能体名称");
                return;
            }

            // 检查是否与模板库重名
            if (isTemplateAgent(agentName)) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "智能体名称【" + agentName + "】与系统模板库重名，请更换名称！",
                        "名称冲突",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String baseAgent = (String) baseAgentComboBox.getSelectedItem();
            if (baseAgent == null) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this, "请选择基础智能体类型");
                return;
            }

            // 获取已选行为列表
            List<String> selectedBehaviors = new ArrayList<>();

            for (int i = 0; i < selectedBehaviorModel.size(); i++) {
                selectedBehaviors.add(selectedBehaviorModel.getElementAt(i));
            }

            try {
                createAgentClass(agentName, baseAgent, selectedBehaviors);
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "智能体创建成功！\n文件名: " + agentName + ".java",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);

                // 清空表单并刷新
                agentNameField.setText("");
                selectedBehaviorModel.clear();
                updateBehaviorsForAgentType(currentBaseAgent);
                loadCreatedAgents();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AgentDesignerPanel.this,
                        "创建智能体失败: " + ex.getMessage(),
                        "错误",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 创建智能体类文件
     */
    private void createAgentClass(String agentName, String baseAgent, List<String> behaviors) throws IOException {
        //拼接路径
        String agentDir = "src/main/java/Agents/" ;
        Path agentPath = Paths.get(agentDir, agentName + ".java");

        StringBuilder agentContent = new StringBuilder();
        agentContent.append("package Agents;\n\n");
        agentContent.append("import jade.core.AID;\n");
        agentContent.append("import jade.core.behaviours.*;\n");
        agentContent.append("import org.json.JSONObject;\n");
        agentContent.append("import utilities.Common.DFServiceRegister;\n");

        switch (baseAgent) {
            case "AGV":
                agentContent.append("import Behaviours.AGV.*;\n");
                agentContent.append("import CommunProtocols.AGVProtocol;\n");
                break;
            case "Arm":
                agentContent.append("import Behaviours.Arm.*;\n");
                agentContent.append("import CommunProtocols.ArmProtocol;\n");
                break;
            case "Buffer":
                agentContent.append("import Behaviours.Buffer.*;\n");
                agentContent.append("import CommunProtocols.BufferProtocol;\n");
                break;
            case "Machine":
                agentContent.append("import Behaviours.Machine.*;\n");
                agentContent.append("import CommunProtocols.MachineProtocol;\n");
                break;
            case "Warehouse":
                agentContent.append("import Behaviours.Warehouse.*;\n");
                agentContent.append("import CommunProtocols.WarehouseProtocol;\n");
                break;
            default:
                agentContent.append("import Behaviours.*;\n");
                break;
        }
        agentContent.append("\n");
        agentContent.append("public class ").append(agentName).append(" extends ").append(baseAgent).append(" {\n\n");
/*        if (!inputRecords.isEmpty()) {
            for (String record : inputRecords) {
                agentContent.append("    ").append(record).append("\n");
            }
            agentContent.append("\n");
        }*/
        inputRecords.clear();
        updateFlagBitList();
        agentContent.append("    @Override\n");
        agentContent.append("    protected void addBehaviours(ThreadedBehaviourFactory tbf) {\n\n");
        for (String behavior : behaviors) {
            agentContent.append("        // 添加").append(behavior).append("行为\n");
            agentContent.append("        addBehaviour(tbf.wrap(new ").append(behavior).append("(this)));\n\n");
        }
        agentContent.append("    }\n\n");
        agentContent.append("}\n");
        //生成代码文件
        Files.createDirectories(agentPath.getParent());
        Files.writeString(agentPath, agentContent.toString());
    }
}